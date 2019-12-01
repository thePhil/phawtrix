package ch.phildev.springphawtrix.service;

import ch.phildev.springphawtrix.communicator.ConnectToMatrixHandler;
import ch.phildev.springphawtrix.domain.PhawtrixCommand;
import ch.phildev.springphawtrix.communicator.PhawtrixMqttHandler;
import com.google.common.collect.ImmutableList;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3PublishResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@Slf4j
public class MatrixServiceImpl implements MatrixService {

    private final CommandEncoder commandEncoder;
    private final ByteHandler byteHandler;
    private final ColorHandler colorHandler;
    private final ConnectToMatrixHandler handler;
    private final PhawtrixMqttHandler publishHandler;

    public MatrixServiceImpl(CommandEncoder commandEncoder,
                             ByteHandler byteHandler,
                             ColorHandler colorHandler,
                             ConnectToMatrixHandler handler,
                             PhawtrixMqttHandler publishHandler) {
        this.commandEncoder = commandEncoder;
        this.byteHandler = byteHandler;
        this.colorHandler = colorHandler;
        this.handler = handler;
        this.publishHandler = publishHandler;
    }

    @Override
    public Mono<byte[]> setBrightness(int brightness) {
        return handler.connectScenario().checkpoint()
                .then(
                        publishScenario(this.getBrightnessPayloadForMatrix(brightness))
                );
    }

    private Flux<byte[]> getBrightnessPayloadForMatrix(int brightness) {
        return Flux.just(
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.CLEAR),
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.SET_BRIGHTNESS, byteHandler.intToByteArray(brightness)),
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.SHOW)
        );
    }

    @Override
    public Mono<byte[]> fillWithColor(String htmlHex) {
        byte[] colorPayload = colorHandler.getHexColorAsPayloadArray(htmlHex);

        return handler.connectScenario().checkpoint()
                .then(
                        publishScenario(getColorPayloadForMatrix(colorPayload))
                );
    }

    private Flux<byte[]> getColorPayloadForMatrix(byte[] colorPayload) {
        return Flux.just(
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.CLEAR),
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.FILL_MATRIX, colorPayload),
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.SHOW)
        );
    }

    @Override
    public Mono<byte[]> console(String consoleCommand) {
        String trimmedCommand = consoleCommand.trim();
        List<String> splittedPayloads = ImmutableList.copyOf(trimmedCommand.split(";"));
        PhawtrixCommand cmd = decodeCommand(splittedPayloads);

        return handler.connectScenario().checkpoint()
                .then(
                        publishScenario(Flux.just(commandEncoder.getPayloadForMatrix(cmd)))
                );
    }

    private PhawtrixCommand decodeCommand(List<String> splittedPayloads) {
        PhawtrixCommand command;
        if (splittedPayloads.get(0).trim().length() > 1) {
            command = PhawtrixCommand.valueOf(splittedPayloads.get(0).trim().toUpperCase());
        } else {
            command = PhawtrixCommand.of(splittedPayloads.get(0));
        }
        return command;
    }

    private Mono<byte[]> convertPublishResultsToByteArray(Flux<Mqtt3PublishResult> pubResults) {
        return pubResults
                .map(pub -> pub.getPublish().getPayloadAsBytes())
                .collect(ByteArrayOutputStream::new,
                        ByteArrayOutputStream::writeBytes)
                .map(ByteArrayOutputStream::toByteArray);
    }

    private Mono<byte[]> publishScenario(Flux<byte[]> publishPayload) {
        return publishPayload
                .transformDeferred(publishHandler::publishToMatrix)
                .transformDeferred(this::convertPublishResultsToByteArray)
                .single();
    }
}

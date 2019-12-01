package ch.phildev.springphawtrix.web.rest;

import ch.phildev.springphawtrix.communicator.ConnectToMatrixHandler;
import ch.phildev.springphawtrix.communicator.MatrixStreamHolder;
import ch.phildev.springphawtrix.communicator.PhawtrixMqttHandler;
import ch.phildev.springphawtrix.domain.PhawtrixCommand;
import ch.phildev.springphawtrix.service.ByteHandler;
import ch.phildev.springphawtrix.service.CommandEncoder;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/matrix-info",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MatrixInfoResource {

    private final ByteHandler byteHandler;
    private final ConnectToMatrixHandler connectHandler;
    private final PhawtrixMqttHandler publishHandler;
    private final MatrixStreamHolder streamHolder;
    private final CommandEncoder commandEncoder;

    public MatrixInfoResource(ByteHandler byteHandler, ConnectToMatrixHandler connectHandler, PhawtrixMqttHandler publishHandler, MatrixStreamHolder streamHolder, CommandEncoder commandEncoder) {
        this.byteHandler = byteHandler;
        this.connectHandler = connectHandler;
        this.publishHandler = publishHandler;
        this.streamHolder = streamHolder;
        this.commandEncoder = commandEncoder;
    }

    @GetMapping(value = "/", consumes = MediaType.ALL_VALUE)
//    public Mono<MatrixInfoDto> getMatrixInfo() {
    public Mono<String> getMatrixInfo() {

        Flux<byte[]> payload = Flux.just(commandEncoder.getPayloadForMatrix(PhawtrixCommand.GET_MATRIX_INFO));

        Flux<Mqtt3Publish> infoStreamFromMatrix = Flux.defer(() ->
                streamHolder.getStreamHolder().orElse(Flux.error(new RuntimeException("No subscription"))));

//        infoStreamFromMatrix.subscribe(mqtt3Publish -> log.debug(new String(mqtt3Publish.getPayloadAsBytes())));

        // getPayload
        return connectHandler.connectScenario()
                .then(
                        publishScenario(payload).doOnSuccess(bytes -> log.debug("Published finished")))
                .thenMany(infoStreamFromMatrix
                        .map(Mqtt3Publish::getPayloadAsBytes))
                .map(String::new)
                .takeUntil(s -> s.contains("MatrixInfo"))
                .single();
//                        .map(bytes -> new Gson().fromJson(new String(bytes), MatrixInfoDto.class)))

    }

    private Flux<byte[]> getPublishPayload() {
        return Flux.from(
                streamHolder.getStreamHolder().get())
//                .retryBackoff(2, Duration.ofSeconds(1))
                .map(Mqtt3Publish::getPayloadAsBytes);
    }

    private Mono<byte[]> publishScenario(Flux<byte[]> publishPayload) {
        return publishPayload
                .transformDeferred(publishHandler::publishToMatrix)
                .transformDeferred(PhawtrixMqttHandler::convertPublishResultsToByteArray)
                .single();
    }
}

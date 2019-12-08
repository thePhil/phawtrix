package ch.phildev.springphawtrix.web.rest;

import ch.phildev.springphawtrix.communicator.ConnectToMatrixHandler;
import ch.phildev.springphawtrix.communicator.PublishToMatrixHandler;
import ch.phildev.springphawtrix.domain.PhawtrixCommand;
import ch.phildev.springphawtrix.service.ColorHandler;
import ch.phildev.springphawtrix.service.CommandEncoder;
import ch.phildev.springphawtrix.service.CoordinateDecoder;
import ch.phildev.springphawtrix.web.rest.dto.AnswerDto;
import ch.phildev.springphawtrix.web.rest.dto.DrawDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@RestController
@Slf4j
@RequestMapping(value = "/draw",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class DrawMatrixResource {


    private final ColorHandler colorHandler;
    private final ConnectToMatrixHandler connectHandler;
    private final PublishToMatrixHandler publishHandler;
    private final CommandEncoder commandEncoder;
    private final CoordinateDecoder coordinateDecoder;

    public DrawMatrixResource(ColorHandler colorHandler,
                              ConnectToMatrixHandler connectHandler,
                              PublishToMatrixHandler publishHandler,
                              CommandEncoder commandEncoder,
                              CoordinateDecoder coordinateDecoder) {
        this.colorHandler = colorHandler;
        this.connectHandler = connectHandler;
        this.publishHandler = publishHandler;
        this.commandEncoder = commandEncoder;
        this.coordinateDecoder = coordinateDecoder;
    }

    @PostMapping("/text")
    public Mono<AnswerDto> printText(@RequestBody DrawDto drawDto) {
        log.debug("Received: " + drawDto);

        Flux<byte[]> cmdPayload = Flux.just(
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.CLEAR),
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.DRAW_TEXT,
                        coordinateDecoder.getPayloadFromCoordinates(drawDto.getCoordinates()),
                        colorHandler.getHexColorAsPayloadArray(drawDto.getHexTextColor()),
                        drawDto.getText().trim().getBytes(StandardCharsets.UTF_8)
                ),
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.SHOW));

        return connectHandler.connectScenario()
                .then(
                        publishHandler.publishScenario(cmdPayload)
                ).map(AnswerUtil::payloadAsHexStringDto);

    }
}

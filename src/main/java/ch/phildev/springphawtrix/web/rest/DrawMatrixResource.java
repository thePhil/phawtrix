package ch.phildev.springphawtrix.web.rest;

import ch.phildev.springphawtrix.communicator.ConnectToMatrixHandler;
import ch.phildev.springphawtrix.communicator.PublishToMatrixHandler;
import ch.phildev.springphawtrix.domain.PhawtrixCommand;
import ch.phildev.springphawtrix.domain.PhawtrixMqttConfig;
import ch.phildev.springphawtrix.service.CommandEncoder;
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

@RestController
@Slf4j
@RequestMapping(value = "/draw",
        produces = MediaType.APPLICATION_JSON_VALUE)
public class DrawMatrixResource {


    private final PhawtrixMqttConfig cfg;
    private final ConnectToMatrixHandler connectHandler;
    private final PublishToMatrixHandler publishHandler;
    private final CommandEncoder commandEncoder;

    public DrawMatrixResource(PhawtrixMqttConfig cfg, ConnectToMatrixHandler connectHandler, PublishToMatrixHandler publishHandler, CommandEncoder commandEncoder) {
        this.cfg = cfg;
        this.connectHandler = connectHandler;
        this.publishHandler = publishHandler;
        this.commandEncoder = commandEncoder;
    }

    @PostMapping("/text")
    public Mono<AnswerDto> printText(@RequestBody DrawDto drawDto) {

        Flux<byte[]> cmdPayload = Flux.just(commandEncoder.getPayloadForMatrix(PhawtrixCommand.CLEAR),
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.DRAW_TEXT),
                commandEncoder.getPayloadForMatrix(PhawtrixCommand.SHOW));

        return connectHandler.connectScenario()
                .then(
                        publishHandler.publishScenario(cmdPayload)
                ).map(AnswerUtil::payloadAsHexStringDto);

    }
}

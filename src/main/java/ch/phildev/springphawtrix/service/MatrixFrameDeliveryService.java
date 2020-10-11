package ch.phildev.springphawtrix.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import ch.phildev.springphawtrix.communicator.ConnectToMatrixHandler;
import ch.phildev.springphawtrix.communicator.PublishToMatrixHandler;
import ch.phildev.springphawtrix.domain.MatrixFrame;
import ch.phildev.springphawtrix.domain.PhawtrixCommand;
import ch.phildev.springphawtrix.web.rest.dto.AnswerDto;

@Service
@Slf4j
public class MatrixFrameDeliveryService {
    private final ConnectToMatrixHandler connectHandler;
    private final PublishToMatrixHandler publishHandler;
    private final CommandEncoder commandEncoder;

    public MatrixFrameDeliveryService(ConnectToMatrixHandler connectHandler, PublishToMatrixHandler publishHandler, CommandEncoder commandEncoder) {
        this.connectHandler = connectHandler;
        this.publishHandler = publishHandler;
        this.commandEncoder = commandEncoder;
    }

    public Mono<Void> publishFramesToMatrix(Flux<MatrixFrame> frames) {
        return Mono.empty();
    }

    public Mono<AnswerDto> publishFrameToMatrix(Flux<byte[]> payloadForMatrix) {

        return connectAndPublishAndGetAnswer(
                Flux.just(commandEncoder.getPayloadForMatrix(PhawtrixCommand.CLEAR))
                        .concatWith(payloadForMatrix)
                        .concatWithValues(commandEncoder.getPayloadForMatrix(PhawtrixCommand.SHOW)));
    }

    private Mono<AnswerDto> connectAndPublishAndGetAnswer(Flux<byte[]> matrixPayload) {
        return connectHandler.connectScenario()
                .thenMany(publishHandler.publishScenarioWithString(matrixPayload))
                .reduce((prev, current) -> prev + "\n" + current)
                .doOnNext(answer -> log.debug("\n{}", answer))
                .map(summary -> AnswerDto.builder().payLoadToMatrix(summary).build());
    }
}

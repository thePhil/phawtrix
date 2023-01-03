package ch.phildev.springphawtrix.app.clock;

import ch.phildev.springphawtrix.app.domain.AppRegistration;
import ch.phildev.springphawtrix.app.domain.PhawtrixApp;
import ch.phildev.springphawtrix.app.management.AppDrawingComponentHolder;
import ch.phildev.springphawtrix.domain.Coordinates;
import ch.phildev.springphawtrix.domain.MatrixFrame;
import ch.phildev.springphawtrix.service.MatrixTextCommandProvider;
import ch.phildev.springphawtrix.web.rest.dto.DrawTextDto;
import com.google.common.io.BaseEncoding;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
public class EasyClock implements PhawtrixApp {

    private final AppRegistration appRegistration;
    private final MatrixTextCommandProvider matrixTextCommandProvider;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_INSTANT;

    public EasyClock(AppRegistration appRegistration,
                     AppDrawingComponentHolder appDrawingComponentHolder) {

        this.appRegistration = appRegistration;
        this.matrixTextCommandProvider = appDrawingComponentHolder.getMatrixTextCommandProvider();
    }

    @Override
    public AppRegistration getAppRegistration() {
        return appRegistration;
    }

    @Override
    public void init() {

    }

    @Override
    public Flux<MatrixFrame> execute() {
        Coordinates clockCoordinates = Coordinates.builder()
                .x(0).y(0).build();

        DrawTextDto.DrawTextDtoBuilder drawTextDtoTemplate = DrawTextDto.builder()
                .coordinates(clockCoordinates)
                .hexTextColor("#000000");

        MatrixFrame emptyFrame = MatrixFrame.builder().build();

        Flux<MatrixFrame> theFlux = Flux.generate(() -> emptyFrame, (f, matrixFrameSynchronousSink) -> {
            String frameContent = getFormattedCurrentTime();
            log.debug(frameContent);
            DrawTextDto dto = drawTextDtoTemplate.text(frameContent).build();
            log.debug(dto.toString());
            byte[] del = matrixTextCommandProvider.textDeliverFrame(dto);
            log.debug(BaseEncoding.base16().encode(del));

            MatrixFrame frame = MatrixFrame.builder()
                    .frameBuffer(List.of(del))
                    .build();
            matrixFrameSynchronousSink.next(frame);
            return frame;
        });

        return theFlux
                .doOnSubscribe(sub -> log.debug("Got Subscribed by {} ", sub))
                .doOnNext(data -> log.debug("Frame: {}", data));

//        return Flux.just(MatrixFrame.builder()
//                .frameBuffer(
//                        List.of(matrixTextCommandProvider.textDeliverFrame(
//                                drawTextDtoTemplate.text(getFormattedCurrentTime())
//                                        .build()
//                        ))
//                ).build())
//                .doOnSubscribe(sub -> log.debug("Got Subscribed by {} ", sub))
//                .doOnNext(data -> log.debug("Frame: {}", data))
//                .share();
//
//        return Flux.fromIterable(List.of(
//                MatrixFrame.builder()
//                        .frameBuffer(
//                                List.of(matrixTextCommandProvider.textDeliverFrame(
//                                        drawTextDtoTemplate.text(timeFormatter.format(Instant.now().truncatedTo(ChronoUnit.SECONDS)))
//                                                .build()
//                                ))
//                        ).build())
//        ).share();

    }

    @Override
    public Mono<Void> stop() {
        return Mono.empty();
    }

    private String getFormattedCurrentTime() {
//        return timeFormatter.format(Instant.now().truncatedTo(ChronoUnit.SECONDS));
        String o = timeFormatter.format(Instant.now());
        return o;
    }
}

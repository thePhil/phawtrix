package ch.phildev.springphawtrix.app.clock;

import ch.phildev.springphawtrix.app.domain.AppRegistration;
import ch.phildev.springphawtrix.app.domain.PhawtrixApp;
import ch.phildev.springphawtrix.app.management.AppDrawingComponentHolder;
import ch.phildev.springphawtrix.domain.Coordinates;
import ch.phildev.springphawtrix.domain.MatrixFrame;
import ch.phildev.springphawtrix.service.MatrixTextCommandProvider;
import ch.phildev.springphawtrix.web.rest.dto.DrawTextDto;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
public class SimpleClock implements PhawtrixApp {

    private final AppRegistration appRegistration;
    private final MatrixTextCommandProvider matrixTextCommandProvider;
    private Flux<MatrixFrame> intervalFlux;

    public SimpleClock(AppRegistration appRegistration,
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
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_INSTANT;
        Coordinates clockCoordinates = Coordinates.builder()
                .x(0).y(0).build();

        DrawTextDto.DrawTextDtoBuilder drawTextDtoTemplate = DrawTextDto.builder()
                .coordinates(clockCoordinates)
                .hexTextColor("#000000");
        if(intervalFlux == null) {
            this.intervalFlux = Flux.interval(Duration.ofSeconds(1L), Schedulers.single())
                    .doOnSubscribe(sub -> log.debug("I'm subscription {} on this object {} on flux {}", sub, this, intervalFlux))
                    .map(interval -> MatrixFrame.builder()
                            .frameNumber(interval)
                            .frameBuffer(
                                    List.of(matrixTextCommandProvider.textDeliverFrame(
                                            drawTextDtoTemplate.text(timeFormatter.format(Instant.now().truncatedTo(ChronoUnit.SECONDS)))
                                                    .build()
                                    ))
                            ).build())
                    .share();
        }
        return intervalFlux;
    }

    @Override
    public Mono<Void> stop() {
        return Mono.empty();
    }
}

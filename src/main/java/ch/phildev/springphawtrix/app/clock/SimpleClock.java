package ch.phildev.springphawtrix.app.clock;

import ch.phildev.springphawtrix.app.domain.AppRegistration;
import ch.phildev.springphawtrix.app.domain.PhawtrixApp;
import ch.phildev.springphawtrix.app.management.AppDrawingComponentHolder;
import ch.phildev.springphawtrix.domain.Coordinates;
import ch.phildev.springphawtrix.domain.MatrixFrame;
import ch.phildev.springphawtrix.service.MatrixTextCommandProvider;
import ch.phildev.springphawtrix.web.rest.dto.DrawTextDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class SimpleClock implements PhawtrixApp {

    private final AppRegistration appRegistration;
    private final MatrixTextCommandProvider matrixTextCommandProvider;
    private Flux<Long> intervalFlux;

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
        this.intervalFlux = Flux.interval(Duration.ofSeconds(1L), Schedulers.single());
        return intervalFlux
                .map(interval -> MatrixFrame.builder()
                        .frameNumber(interval)
                        .frameBuffer(
                                List.of(matrixTextCommandProvider.textDeliverFrame(
                                        drawTextDtoTemplate.text(timeFormatter.format(Instant.now().truncatedTo(ChronoUnit.SECONDS)))
                                                .build()
                                ))
                        ).build());
    }

    @Override
    public Mono<Void> stop() {
        return Mono.empty();
    }
}

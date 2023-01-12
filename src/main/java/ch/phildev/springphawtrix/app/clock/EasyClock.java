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
import java.time.temporal.ChronoUnit;
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
            log.trace(frameContent);
            DrawTextDto dto = drawTextDtoTemplate.text(frameContent).build();
            log.trace(dto.toString());
            byte[] del = matrixTextCommandProvider.textDeliverFrame(dto);
            log.trace(BaseEncoding.base16().encode(del));

            MatrixFrame frame = MatrixFrame.builder()
                    .frameBuffer(List.of(del))
                    .build();
            matrixFrameSynchronousSink.next(frame);
            return frame;
        });

        return theFlux
                .doOnSubscribe(sub -> log.debug("Got Subscribed by {} ", sub))
                .doOnNext(data -> log.debug("Frame: {}", data));
    }

    @Override
    public Mono<Void> stop() {
        log.warn("Easy Clock is being stopped!");
        return Mono.empty();
    }

    private String getFormattedCurrentTime() {
        return timeFormatter.format(Instant.now().truncatedTo(ChronoUnit.SECONDS));
    }
}

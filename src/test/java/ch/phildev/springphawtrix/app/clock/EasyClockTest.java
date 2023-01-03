package ch.phildev.springphawtrix.app.clock;

import ch.phildev.springphawtrix.app.domain.AppRegistration;
import ch.phildev.springphawtrix.app.management.AppDrawingComponentHolder;
import ch.phildev.springphawtrix.domain.MatrixFrame;
import ch.phildev.springphawtrix.service.ColorHandler;
import ch.phildev.springphawtrix.service.CommandEncoder;
import ch.phildev.springphawtrix.service.MatrixTextCommandProvider;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicReference;

@ExtendWith(MockitoExtension.class)
@Slf4j
class EasyClockTest {


    private final static String APP_NAME = "testClock";
    private final static String AUTHOR_NAME = "me";


    private EasyClock testClock;

    private AppRegistration easyClockRegistration;

    @Mock
    private MatrixTextCommandProvider textProvider;
    @Mock
    private ColorHandler colorHandler;
    @Mock
    private CommandEncoder commandEncoder;

    @BeforeEach
    void setUp() {

        easyClockRegistration = AppRegistration.builder()
                .appAuthor(AUTHOR_NAME)
                .appName(APP_NAME)
                .build();

        AppDrawingComponentHolder componentHolder = AppDrawingComponentHolder.builder()
                .colorHandler(colorHandler)
                .commandEncoder(commandEncoder)
                .matrixTextCommandProvider(textProvider).build();


        testClock = new EasyClock(easyClockRegistration, componentHolder);
    }

    @Test
    void testEasyClockMoreThenOneValue() {
        Mockito.when(textProvider.textDeliverFrame(Mockito.any()))
                .thenReturn(DateTimeFormatter.ISO_INSTANT.format(Instant.now()).getBytes(StandardCharsets.UTF_8))
                .thenReturn(DateTimeFormatter.ISO_INSTANT.format(Instant.now()).getBytes(StandardCharsets.UTF_8));

        AtomicReference<MatrixFrame> firstFrame = new AtomicReference<>();
        StepVerifier.withVirtualTime(() -> testClock.execute().take(2))
                .expectSubscription()
                .thenAwait(Duration.ofSeconds(5))
                .consumeNextWith(frame -> {
                    firstFrame.set(frame);
                })
                .thenAwait(Duration.ofSeconds(5))
                .assertNext(f -> Assertions.assertThat(f).isNotEqualTo(firstFrame.get()))
                .expectComplete()
                .verify();
    }

    @Test
    void testWithSyncInterval() {

        Mockito.when(textProvider.textDeliverFrame(Mockito.any()))
                .thenReturn(DateTimeFormatter.ISO_INSTANT.format(Instant.now()).getBytes(StandardCharsets.UTF_8))
                .thenReturn(DateTimeFormatter.ISO_INSTANT.format(Instant.now()).getBytes(StandardCharsets.UTF_8));

        Flux<Long> intervalFlux = Flux.interval(Duration.ofMillis(500)).share();
        Flux<String> frameFlux = Flux.combineLatest(intervalFlux, testClock.execute(), (i, t) -> i + " " + t.toString())
                .doOnNext(dat -> log.debug(dat));

        StepVerifier.withVirtualTime(() -> frameFlux.take(2L))
                .expectSubscription()
                .expectNextCount(2L)
                .expectComplete()
                .verify();
    }
}
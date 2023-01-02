package ch.phildev.springphawtrix.app.clock;

import ch.phildev.springphawtrix.app.domain.AppRegistration;
import ch.phildev.springphawtrix.app.management.AppDrawingComponentHolder;
import ch.phildev.springphawtrix.service.ColorHandler;
import ch.phildev.springphawtrix.service.CommandEncoder;
import ch.phildev.springphawtrix.service.MatrixTextCommandProvider;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@ExtendWith(MockitoExtension.class)
class SimpleClockTest {

    private final static String APP_NAME = "testClock";
    private final static String AUTHOR_NAME = "me";


    private SimpleClock testClock;

    private AppRegistration simpleAppRegistration;

    @Mock
    private MatrixTextCommandProvider textProvider;
    @Mock
    private ColorHandler colorHandler;
    @Mock
    private CommandEncoder commandEncoder;

    @BeforeEach
    void setUp() {

        simpleAppRegistration = AppRegistration.builder()
                .appAuthor(AUTHOR_NAME)
                .appName(APP_NAME)
                .build();

        AppDrawingComponentHolder componentHolder = AppDrawingComponentHolder.builder()
                .colorHandler(colorHandler)
                .commandEncoder(commandEncoder)
                .matrixTextCommandProvider(textProvider).build();


        testClock = new SimpleClock(simpleAppRegistration, componentHolder);
    }

    @Test
    void getAppRegistration() {
        Assertions.assertThat(testClock.getAppRegistration()).isEqualTo(simpleAppRegistration);
    }

    @Test
    void execute() {
        Mockito.when(textProvider.textDeliverFrame(Mockito.any())).thenReturn("something".getBytes(StandardCharsets.UTF_8));

        StepVerifier.withVirtualTime(() -> testClock.execute().take(3))
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(1))
                .expectNextMatches(frame -> frame.getFrameNumber() == 0L)
                .thenAwait(Duration.ofSeconds(1))
                .expectNextMatches(frame -> frame.getFrameNumber() == 1L)
                .thenAwait(Duration.ofSeconds(1))
                .expectNextCount(1L)
                .expectComplete()
                .verify(Duration.ofMillis(200));
    }
}

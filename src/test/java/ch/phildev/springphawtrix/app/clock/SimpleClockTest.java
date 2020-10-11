package ch.phildev.springphawtrix.app.clock;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import ch.phildev.springphawtrix.app.domain.AppRegistration;
import ch.phildev.springphawtrix.service.MatrixTextCommandProvider;

@ExtendWith(MockitoExtension.class)
class SimpleClockTest {

    private final static String APP_NAME = "testClock";
    private final static String AUTHOR_NAME = "me";


    private SimpleClock testClock;

    private AppRegistration simpleAppRegistration;

    @Mock
    private MatrixTextCommandProvider textProvider;

    @BeforeEach
    void setUp() {

        simpleAppRegistration = AppRegistration.builder()
                .appAuthor(AUTHOR_NAME)
                .appName(APP_NAME)
                .build();


        testClock = new SimpleClock(simpleAppRegistration, textProvider);
    }

    @Test
    void getAppRegistration() {
        Assertions.assertThat(testClock.getAppRegistration()).isEqualTo(simpleAppRegistration);
    }

    @Test
    void execute() {
        Mockito.when(textProvider.textDeliverFrame(Mockito.any())).thenReturn("something".getBytes(StandardCharsets.UTF_8));

        StepVerifier.create(testClock.execute().take(3L))
                .expectSubscription()
                .expectNextMatches(frame -> frame.getFrameNumber() == 0L)
                .thenAwait(Duration.ofSeconds(1L))
                .expectNextMatches(frame -> frame.getFrameNumber() == 1L)
                .expectNextCount(1L)
                .verifyComplete();
    }
}

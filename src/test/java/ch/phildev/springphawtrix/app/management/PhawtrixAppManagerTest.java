package ch.phildev.springphawtrix.app.management;

import ch.phildev.springphawtrix.app.clock.SimpleClock;
import ch.phildev.springphawtrix.app.domain.AppRegistration;
import ch.phildev.springphawtrix.app.domain.PhawtrixApp;
import ch.phildev.springphawtrix.domain.PhawtrixMqttConfig;
import ch.phildev.springphawtrix.testutils.TestPhawtrixConfigBuilderFactory;
import com.hivemq.client.mqtt.mqtt3.reactor.Mqtt3ReactorClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class PhawtrixAppManagerTest {

    private static final String AUTHOR_NAME = "me";
    private static final String APP_NAME = "SimpleClock";
    private static final String WRONG_APP_NAME = "Not a clock";
    @Mock
    AppDrawingComponentHolder appDrawingComponentHolder;

    @Mock
    ReactivePhawtrixAppRepositoryService<PhawtrixApp> appRepositoryService;
    @Mock
    AppRegistrationRepository appRegistrationRepository;

    @Mock
    Mqtt3ReactorClient client;

    PhawtrixMqttConfig cfg;

    AppRegistration simpleAppRegistration;

    @BeforeEach
    void setUp() {

        cfg = TestPhawtrixConfigBuilderFactory.aDefaultConfig().build();

        simpleAppRegistration = AppRegistration.builder()
                .appAuthor(AUTHOR_NAME)
                .appName(APP_NAME)
                .build();
    }

    @Test
    void initAppWithAppRegistrationNotPresent() {

        // given
        PhawtrixAppManager appManager = new PhawtrixAppManagerImpl(appDrawingComponentHolder,
                appRepositoryService,
                appRegistrationRepository,
                client,
                cfg);
        Mockito.when(appRegistrationRepository.findByName(WRONG_APP_NAME))
                .thenReturn(Mono.empty());
        // when
        StepVerifier.withVirtualTime(() -> appManager.initApp(WRONG_APP_NAME))
        // then
                .expectSubscription()
                .expectError(IllegalStateException.class)
                .verify();
    }

    @Test
    void initApp() {

        // given
        PhawtrixAppManager appManager = new PhawtrixAppManagerImpl(appDrawingComponentHolder,
                appRepositoryService,
                appRegistrationRepository,
                client,
                cfg);
        Mockito.when(appRegistrationRepository.findByName(APP_NAME))
                .thenReturn(Mono.just(simpleAppRegistration));
        Mockito.when(appRepositoryService.loadPhawtrixApp(APP_NAME))
                .thenReturn(Mono.empty());
        Mockito.when(appRepositoryService.savePhawtrixApp(Mockito.any()))
                .thenReturn(Mono.empty());

        Mockito.when(appRepositoryService.numberOfStoredApps())
                .thenReturn(42);

        // when
        StepVerifier.withVirtualTime(() -> appManager.initApp(APP_NAME))

                //then
                .expectSubscription()
                .expectComplete()
                .verify();

        Mockito.verify(appRepositoryService, Mockito.atMostOnce() ).savePhawtrixApp(Mockito.any());
    }

    @Test
    void validateThatAnAppIsProducedOnlyOnce() {
        // given
        SimpleClock testClock = new SimpleClock(simpleAppRegistration, appDrawingComponentHolder);
        PhawtrixAppManager appManager = new PhawtrixAppManagerImpl(appDrawingComponentHolder,
                appRepositoryService,
                appRegistrationRepository,
                client,
                cfg);

        Mockito.when(appRegistrationRepository.findByName(APP_NAME))
                .thenReturn(Mono.just(simpleAppRegistration));
        Mockito.when(appRepositoryService.loadPhawtrixApp(APP_NAME))
                .thenReturn(Mono.just(testClock));
        // when
        StepVerifier.withVirtualTime(() -> appManager.initApp(APP_NAME))
                .expectSubscription()
                .expectComplete()
                .verify();
        // then

        Mockito.verify(appRepositoryService, Mockito.never()).savePhawtrixApp(Mockito.any());

    }
}
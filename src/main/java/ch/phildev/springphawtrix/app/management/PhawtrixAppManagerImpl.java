package ch.phildev.springphawtrix.app.management;

import ch.phildev.springphawtrix.app.clock.EasyClock;
import ch.phildev.springphawtrix.app.clock.SimpleClock;
import ch.phildev.springphawtrix.app.domain.AppRegistration;
import ch.phildev.springphawtrix.app.domain.PhawtrixApp;
import ch.phildev.springphawtrix.communicator.PublishToMatrixHandler;
import ch.phildev.springphawtrix.domain.MatrixFrame;
import ch.phildev.springphawtrix.domain.PhawtrixMqttConfig;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import com.hivemq.client.mqtt.mqtt3.reactor.Mqtt3ReactorClient;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscription;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Slf4j
public class PhawtrixAppManagerImpl implements PhawtrixAppManager {

    private static final String APP_REG_KEY = "RUNNING_APP_REG";

    private final AppDrawingComponentHolder appDrawingComponentHolder;
    private final ReactivePhawtrixAppRepositoryService<PhawtrixApp> appRepositoryService;
    private final AppRegistrationRepository appRegistrationRepository;

    // to be moved to a different component e.g. a publishHandler or similar
    private final Mqtt3ReactorClient client;
    private final PhawtrixMqttConfig cfg;

    private final ConcurrentHashMap<String, AtomicInteger> subsPerApp;
    private Subscription subscription;


    public PhawtrixAppManagerImpl(AppDrawingComponentHolder appDrawingComponentHolder,
                                  ReactivePhawtrixAppRepositoryService<PhawtrixApp> appRepositoryService,
                                  AppRegistrationRepository appRegistrationRepository,
                                  Mqtt3ReactorClient client,
                                  PhawtrixMqttConfig cfg) {
        this.appDrawingComponentHolder = appDrawingComponentHolder;
        this.appRepositoryService = appRepositoryService;
        this.appRegistrationRepository = appRegistrationRepository;
        this.client = client;
        this.cfg = cfg;

        subsPerApp = new ConcurrentHashMap<>();
    }

    @Override
    public Mono<Void> initApp(String appName) {
        Assert.hasText(appName, "A valid app name must be given");

        return appRegistrationRepository.findByName(appName)
                .doOnSubscribe(sub -> log.debug("Did subscribe to app registration"))
                .switchIfEmpty(
                        Mono.error(new IllegalStateException(String.format("App %s should have been returned", appName))))
                .doOnSuccess(appRegistration -> log.debug("Found AppRegistration {} for initialize", appRegistration.getAppName()))
                .checkpoint()
                .flatMap(reg -> appRepositoryService.loadPhawtrixApp(reg.getAppName())
                        .doOnSuccess(a -> {
                            if (a == null) {
                                log.debug("App {} has not been found in the repository", reg.getAppName());
                            }
                        })
                        .switchIfEmpty(Mono.just(this.produceApp(reg))
                                .switchIfEmpty(Mono.error(new IllegalStateException(String.format("App %s has not been produced.", appName))))
                                .flatMap(appRepositoryService::savePhawtrixApp)
                                .checkpoint()
                                .doOnSuccess(num -> log.debug("saved successfully with total {} elements", appRepositoryService.numberOfStoredApps())))
                )
                .doOnSuccess(data -> log.debug("We have {} apps in the repository", appRepositoryService.numberOfStoredApps()))
                .then();
    }

    @Override
    public Flux<String> executeApp(String appName) {
        Assert.hasText(appName, "A valid app name must be given");
        Flux<MatrixFrame> appFlux = appRegistrationRepository
                .findByName(appName)
                .flatMapMany(reg -> handleExecute(reg, appName));

        return appFlux.transform(this::publishToMatrix)
                .doOnSubscribe(sub -> {
                    AtomicInteger numSubs = subsPerApp.computeIfAbsent(appName, k -> new AtomicInteger(0));
                    if (numSubs.incrementAndGet() <= 1) {
                        this.subscription = sub;
                    } else {
                        log.warn("To many subscribers, cancelling subscription");
                        sub.cancel();
                    }
                });

    }

    private Flux<MatrixFrame> handleExecute(AppRegistration reg, String appName) {
        return appRepositoryService.loadPhawtrixApp(reg.getAppName())
                .switchIfEmpty(
                        Mono.error(new IllegalStateException(String.format("App %s should have been returned", appName))))
                .checkpoint()
                .doOnSuccess(app -> log.debug("App {} has been loaded, start execution.", app.getAppRegistration().getAppName()))
                .flatMapMany(PhawtrixApp::execute)
                .doOnNext(frame -> log.debug("Frame number: {}", frame.getFrameNumber()))
                .limitRate(1)
                .delayElements(Duration.ofMillis(reg.getMilliInterval()));
    }

    @Override
    public Mono<Void> stopApp(String appName) {
        Assert.hasText(appName, "A valid app name must be given");
        return appRepositoryService.loadPhawtrixApp(appName)
                .flatMap(PhawtrixApp::stop)
                .doOnSuccess(unused -> {
                    subscription.cancel();
                    subsPerApp.compute(appName, (s, ai) -> new AtomicInteger(0));
                });
    }

    protected PhawtrixApp produceApp(AppRegistration appRegistration) {
        // TODO replace with dynamic mechanism for loading apps
        return switch (appRegistration.getAppName()) {
            case "SimpleClock" -> new SimpleClock(appRegistration, appDrawingComponentHolder);
            case "EasyClock" -> new EasyClock(appRegistration, appDrawingComponentHolder);
            default -> new SimpleClock(appRegistration, appDrawingComponentHolder);
        };
    }


    private Flux<String> publishToMatrix(Flux<MatrixFrame> executionFrame) {
        return executionFrame
                .flatMapIterable(MatrixFrame::getFrameBuffer)
                .map(frameBytes -> Mqtt3Publish.builder()
                        .topic(cfg.getMatrixPublishTopic())
                        .qos(MqttQos.AT_LEAST_ONCE)
                        .payload(ByteBuffer.wrap(frameBytes))
                        .build())
                .publish(client::publish)
                .doOnNext(pub -> log.debug("Publish result: {}", pub.getPublish()))
                .transform(PublishToMatrixHandler::convertPublishResultsToReadableString);
    }
}

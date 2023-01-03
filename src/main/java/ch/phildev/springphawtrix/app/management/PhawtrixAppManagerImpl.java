package ch.phildev.springphawtrix.app.management;

import ch.phildev.springphawtrix.app.clock.EasyClock;
import ch.phildev.springphawtrix.app.clock.SimpleClock;
import ch.phildev.springphawtrix.app.domain.AppRegistration;
import ch.phildev.springphawtrix.app.domain.PhawtrixApp;
import ch.phildev.springphawtrix.domain.MatrixFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Component
@Slf4j
public class PhawtrixAppManagerImpl implements PhawtrixAppManager {

    private final AppDrawingComponentHolder appDrawingComponentHolder;
    private final ReactivePhawtrixAppRepositoryService<PhawtrixApp> appRepositoryService;
    private final AppRegistrationRepository appRegistrationRepository;

    private final Flux<Long> intervalFlux;

    public PhawtrixAppManagerImpl(AppDrawingComponentHolder appDrawingComponentHolder,
                                  ReactivePhawtrixAppRepositoryService<PhawtrixApp> appRepositoryService,
                                  AppRegistrationRepository appRegistrationRepository) {
        this.appDrawingComponentHolder = appDrawingComponentHolder;
        this.appRepositoryService = appRepositoryService;
        this.appRegistrationRepository = appRegistrationRepository;

        this.intervalFlux = Flux.interval(Duration.ofSeconds(1), Schedulers.single()).share();
    }

    @Override
    public Mono<Void> initApp(String appName) {
        Assert.hasText(appName, "A valid app name must be given");

        return appRegistrationRepository.findByName(appName)
                .doOnSubscribe(sub -> log.debug("Did subscribe to app registration"))
                .switchIfEmpty(
                        Mono.error(new IllegalStateException(String.format("App %s should have been returned", appName))))
                .doOnSuccess(appRegistration -> log.debug("Found AppRegistration {} for initialize", appRegistration.getAppName()))
                .log()
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
                .log()
                .doOnSuccess(data -> log.debug("We have {} apps in the repository", appRepositoryService.numberOfStoredApps()))
                .then();
    }


    @Override
    public Flux<MatrixFrame> executeApp(String appName) {
        Assert.hasText(appName, "A valid app name must be given");

        Flux<MatrixFrame> appFlux = Mono.just(appName)
                .flatMap(appRepositoryService::loadPhawtrixApp)
                .switchIfEmpty(
                        Mono.error(new IllegalStateException(String.format("App %s should have been returned", appName))))
                .checkpoint()
                .doOnSuccess(app -> log.debug("App {} has been loaded, start execution.", app.getAppRegistration().getAppName()))
                .flatMapMany(PhawtrixApp::execute)
                .doOnNext(frame -> log.debug("Frame number: {}", frame.getFrameNumber()));

        return intervalFlux.zipWith(appFlux, 1, (interval, frame) -> frame.toBuilder()
                        .frameNumber(interval)
                        .build())
                .doOnNext(frame -> log.debug("Frame number: {}", frame.getFrameNumber()));
    }

    @Override
    public Mono<Void> stopApp(String appName) {
        Assert.hasText(appName, "A valid app name must be given");
        return appRepositoryService.loadPhawtrixApp(appName)
                .flatMap(PhawtrixApp::stop);
    }

    protected PhawtrixApp produceApp(AppRegistration appRegistration) {
        // TODO replace with dynamic mechanism for loading apps
        return switch (appRegistration.getAppName()) {
            case "SimpleClock" -> new SimpleClock(appRegistration, appDrawingComponentHolder);
            case "EasyClock" -> new EasyClock(appRegistration, appDrawingComponentHolder);
            default -> new SimpleClock(appRegistration, appDrawingComponentHolder);
        };
    }
}

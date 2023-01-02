package ch.phildev.springphawtrix.app.management;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import ch.phildev.springphawtrix.app.clock.SimpleClock;
import ch.phildev.springphawtrix.app.domain.AppRegistration;
import ch.phildev.springphawtrix.app.domain.PhawtrixApp;
import ch.phildev.springphawtrix.domain.MatrixFrame;

@Component
@Slf4j
public class PhawtrixAppManagerImpl implements PhawtrixAppManager {

    private final AppDrawingComponentHolder appDrawingComponentHolder;
    private final ReactivePhawtrixAppRepositoryService<PhawtrixApp> appRepositoryService;
    private final AppRegistrationRepository appRegistrationRepository;

    public PhawtrixAppManagerImpl(AppDrawingComponentHolder appDrawingComponentHolder,
                                  ReactivePhawtrixAppRepositoryService<PhawtrixApp> appRepositoryService,
                                  AppRegistrationRepository appRegistrationRepository) {
        this.appDrawingComponentHolder = appDrawingComponentHolder;
        this.appRepositoryService = appRepositoryService;
        this.appRegistrationRepository = appRegistrationRepository;
    }

    @Override
    public Mono<Void> initApp(String appName) {
        Assert.hasText(appName, "A valid app name must be given");

        return appRegistrationRepository.findByName(appName)
                .switchIfEmpty(
                        Mono.error(new IllegalStateException(String.format("App %s should have been returned", appName))))
                .doOnSuccess(appRegistration -> log.debug("Found AppRegistration {} for initialize", appRegistration.getAppName()))
                .map(this::produceApp)
                .switchIfEmpty(
                        Mono.error(new IllegalStateException(String.format("App %s has not been produced.", appName))))
                .flatMap(appRepositoryService::savePhawtrixApp)
                .doOnSuccess(num -> log.debug("saved successfully with total {} elements",
                        appRepositoryService.numberOfStoredApps()))
                .then();
    }


    @Override
    public Flux<MatrixFrame> executeApp(String appName) {
        Assert.hasText(appName, "A valid app name must be given");

        return Mono.just(appName)
                .flatMap(appRepositoryService::loadPhawtrixApp)
                .switchIfEmpty(
                        Mono.error(new IllegalStateException(String.format("App %s should have been returned", appName))))
                .checkpoint()
                .doOnSuccess(app -> log.debug("App {} has been loaded, start execution.", app.getAppRegistration().getAppName()))
                .flatMapMany(PhawtrixApp::execute)
                .doOnNext(frame -> log.debug("Frame number: {}", frame.getFrameNumber()));
    }

    @Override
    public Mono<Void> stopApp(String appName) {
        Assert.hasText(appName, "A valid app name must be given");
        return appRepositoryService.loadPhawtrixApp(appName)
                .flatMap(PhawtrixApp::stop);
    }

    private PhawtrixApp produceApp(AppRegistration appRegistration) {

        // replace with dynamic mechanism for loading apps
        return switch (appRegistration.getAppName()) {
            case "SimpleClock" -> new SimpleClock(appRegistration, appDrawingComponentHolder);
            default -> new SimpleClock(appRegistration, appDrawingComponentHolder);
        };
    }
}

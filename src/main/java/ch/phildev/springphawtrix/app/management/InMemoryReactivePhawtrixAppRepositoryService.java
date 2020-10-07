package ch.phildev.springphawtrix.app.management;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import reactor.core.publisher.Mono;

import ch.phildev.springphawtrix.app.domain.PhawtrixApp;

/**
 * The default repository to store apps in between their executions.
 */
public class InMemoryReactivePhawtrixAppRepositoryService implements ReactivePhawtrixAppRepositoryService<PhawtrixApp> {

    private final Map<String, PhawtrixApp> apps = new ConcurrentHashMap<>();

    @Override
    public  Mono<PhawtrixApp> loadPhawtrixApp(String appName) {
        PhawtrixApp app = apps.get(appName);
        return Mono.justOrEmpty(app);
    }

    @Override
    public Mono<Void> savePhawtrixApp(PhawtrixApp app) {
        return Mono.fromRunnable(() -> {
            this.apps.put(app.getAppRegistration().getAppName(), app);
        });
    }

}

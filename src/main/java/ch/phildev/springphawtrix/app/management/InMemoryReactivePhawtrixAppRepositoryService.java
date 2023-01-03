package ch.phildev.springphawtrix.app.management;

import ch.phildev.springphawtrix.app.domain.PhawtrixApp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The default repository to store apps in between their executions.
 */
@Service
@Slf4j
public class InMemoryReactivePhawtrixAppRepositoryService implements ReactivePhawtrixAppRepositoryService<PhawtrixApp> {

    private final Map<String, PhawtrixApp> apps = new ConcurrentHashMap<>();

    @Override
    public  Mono<PhawtrixApp> loadPhawtrixApp(String appName) {
        return Mono.justOrEmpty(apps.get(appName));
    }

    @Override
    public Mono<PhawtrixApp> savePhawtrixApp(PhawtrixApp app) {
            log.debug("Before app save, and {} stored", apps.size());
            this.apps.put(app.getAppRegistration().getAppName(), app);
            log.debug("Saved app {} with key {} and {} stored.", app, app.getAppRegistration().getAppName(),
                    apps.size());
            log.debug(apps.toString());
            return Mono.just(app);
    }

    @Override
    public int numberOfStoredApps() {
        return apps.size();
    }

}

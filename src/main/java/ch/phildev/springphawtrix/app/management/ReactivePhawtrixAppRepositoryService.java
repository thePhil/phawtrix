package ch.phildev.springphawtrix.app.management;

import reactor.core.publisher.Mono;

import ch.phildev.springphawtrix.app.domain.PhawtrixApp;

/**
 * Implementations of this interface store and persist {@link PhawtrixApp}s between
 * runs.
 *
 * @see PhawtrixAppManager
 */
public interface ReactivePhawtrixAppRepositoryService<T extends PhawtrixApp> {

    /**
     * Returns an initialized {@link PhawtrixApp} from the repository
     *
     * @param <T>     the type of the app
     * @param appName the name of the app to retrieve
     * @return the {@link PhawtrixApp} or an empty {@link Mono}
     */
    Mono<T> loadPhawtrixApp(String appName);


    /**
     * Saves a ready to go {@link PhawtrixApp} into the AppRepository.
     *
     * @param app the app to save
     */
    Mono<PhawtrixApp> savePhawtrixApp(T app);

    int numberOfStoredApps();
}

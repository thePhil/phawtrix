package ch.phildev.springphawtrix.app.management;

import ch.phildev.springphawtrix.app.domain.PhawtrixApp;
import reactor.core.publisher.Mono;

public interface ReactivePhawtrixAppRepositoryService {

    /**
     * Returns an initialized {@link PhawtrixApp} from the repository
     *
     * @param appName the name of the app to retrieve
     * @param <T> the type of the app
     * @return  the {@link PhawtrixApp} or an empty {@link Mono}
     */
    <T extends PhawtrixApp> Mono<T> loadPhawtrixApp(String appName);

    /**
     * Saves a ready to go {@link PhawtrixApp} into the AppRepository.
     *
     * @param app the app to save
     */
    Mono<Void> savePhawtrixApp(PhawtrixApp app);
}

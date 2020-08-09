package ch.phildev.springphawtrix.app.management;

import ch.phildev.springphawtrix.app.domain.AppRegistration;
import reactor.core.publisher.Mono;

/**
 * This repository holds initialized Apps, which are ready to run, independent of they were previous
 */
public interface AppRepository {

    /**
     * Find an App by it appName
     *
     * @param appName the name of the application
     *
     * @return the app
     */
    Mono<AppRegistration> findByName(String appName);

}

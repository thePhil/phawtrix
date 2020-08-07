package ch.phildev.springphawtrix.app.management;

import reactor.core.publisher.Mono;

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

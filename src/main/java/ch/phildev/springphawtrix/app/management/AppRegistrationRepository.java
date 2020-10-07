package ch.phildev.springphawtrix.app.management;

import ch.phildev.springphawtrix.app.domain.AppRegistration;
import reactor.core.publisher.Mono;

public interface AppRegistrationRepository {

    /**
     * Find an App Registration by it appName
     *
     * @param appName the name of the application
     *
     * @return the app
     */
    Mono<AppRegistration> findByName(String appName);

}

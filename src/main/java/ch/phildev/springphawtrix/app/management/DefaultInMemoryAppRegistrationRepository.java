package ch.phildev.springphawtrix.app.management;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ch.phildev.springphawtrix.app.domain.AppRegistration;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.Assert;
import reactor.core.publisher.Mono;

@Getter
public class DefaultInMemoryAppRegistrationRepository implements AppRegistrationRepository, Iterable<AppRegistration>{

    private final Map<String,AppRegistration> appRegistrations;

    public DefaultInMemoryAppRegistrationRepository(List<AppRegistration> appRegistrations) {
        this.appRegistrations = toUnmodifiableConcurrentMap(appRegistrations);
    }

    private static Map<String, AppRegistration> toUnmodifiableConcurrentMap(List<AppRegistration> appRegistrations) {

        ConcurrentHashMap<String, AppRegistration> theMap = new ConcurrentHashMap<>();

        appRegistrations.forEach(app -> {
            Assert.notNull(appRegistrations, "AppRegistration must not be null");
            if (theMap.putIfAbsent(app.getAppName(), app) == null) {
                throw new IllegalStateException(String.format("App already registered %s", app.getAppName()));
            }
        });

        return theMap;
    }

    @Override
    public Mono<AppRegistration> findByName(String appName) {
        return Mono.justOrEmpty(appRegistrations.get(appName));
    }

    @NotNull
    @Override
    public Iterator<AppRegistration> iterator() {
        return appRegistrations.values().iterator();
    }
}

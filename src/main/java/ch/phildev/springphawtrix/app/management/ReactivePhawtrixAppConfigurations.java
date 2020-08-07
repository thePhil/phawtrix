package ch.phildev.springphawtrix.app.management;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class ReactivePhawtrixAppConfigurations {

    @Configuration(proxyBeanMethods = false)
    static class ReactiveAppRegistrationRepositoryConfiguration {

        @Bean
        DefaultInMemoryAppRepository appRegistrationRepository(PhawtrixAppProperties properties) {
            List<AppRegistration> registrations =
                    new ArrayList<>(PhawtrixAppPropertiesAppRegistrationAdapter.getAppRegistrations(properties).values());

            return new DefaultInMemoryAppRepository(registrations);
        }
    }
}

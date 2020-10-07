package ch.phildev.springphawtrix.app.management.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.phildev.springphawtrix.app.domain.AppRegistration;
import ch.phildev.springphawtrix.app.management.DefaultInMemoryAppRegistrationRepository;
import ch.phildev.springphawtrix.app.management.PhawtrixAppPropertiesAppRegistrationAdapter;

public class ReactivePhawtrixAppConfigurations {

    @Configuration(proxyBeanMethods = false)
    static class ReactiveAppRegistrationRepositoryConfiguration {

        @Bean
        DefaultInMemoryAppRegistrationRepository appRegistrationRepository(PhawtrixAppProperties properties) {
            List<AppRegistration> registrations =
                    new ArrayList<>(PhawtrixAppPropertiesAppRegistrationAdapter.getAppRegistrations(properties).values());

            return new DefaultInMemoryAppRegistrationRepository(registrations);
        }
    }
}

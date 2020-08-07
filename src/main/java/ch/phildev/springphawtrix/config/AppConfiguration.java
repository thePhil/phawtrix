package ch.phildev.springphawtrix.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import ch.phildev.springphawtrix.app.management.PhawtrixAppProperties;
import ch.phildev.springphawtrix.app.management.ReactivePhawtrixAppConfigurations;

@EnableConfigurationProperties(PhawtrixAppProperties.class)
@Import(ReactivePhawtrixAppConfigurations.class)
public class AppConfiguration {
}

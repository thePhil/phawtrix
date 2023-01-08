package ch.phildev.springphawtrix;

import ch.phildev.springphawtrix.domain.PhawtrixMqttConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import ch.phildev.springphawtrix.app.management.config.PhawtrixAppProperties;

@SpringBootApplication
@ComponentScan(basePackages = "ch.phildev")
@EnableConfigurationProperties({PhawtrixAppProperties.class, PhawtrixMqttConfig.class})
public class SpringPhawtrixApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringPhawtrixApplication.class, args);

    }
}

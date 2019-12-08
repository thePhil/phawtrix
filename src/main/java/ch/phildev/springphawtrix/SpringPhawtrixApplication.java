package ch.phildev.springphawtrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Hooks;

@SpringBootApplication(
        exclude = JacksonAutoConfiguration.class
)
@Configuration
//@Import({
//		AopAutoConfiguration.class,
//		ClientHttpConnectorAutoConfiguration.class,
//        CodecsAutoConfiguration.class,
//        EmbeddedWebServerFactoryCustomizerAutoConfiguration.class,
//        ErrorWebFluxAutoConfiguration.class,
//        CacheAutoConfiguration.class,
//        GsonAutoConfiguration.class,
//        HttpHandlerAutoConfiguration.class,
//        PropertyPlaceholderAutoConfiguration.class,
//        ReactiveWebServerFactoryAutoConfiguration.class,
//		SpringDocConfiguration.class,
//        TaskExecutionAutoConfiguration.class,
//        ValidationAutoConfiguration.class,
////		WebClientAutoConfiguration.class,
//        WebFluxAutoConfiguration.class
//})
@ComponentScan(basePackages = "ch.phildev")
public class SpringPhawtrixApplication {

    public static void main(String[] args) {
        Hooks.onOperatorDebug();
        SpringApplication.run(SpringPhawtrixApplication.class, args);
    }

}

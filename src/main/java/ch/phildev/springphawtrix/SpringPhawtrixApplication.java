package ch.phildev.springphawtrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "ch.phildev")
public class SpringPhawtrixApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringPhawtrixApplication.class, args);
    }
}

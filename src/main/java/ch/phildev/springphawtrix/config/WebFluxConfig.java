package ch.phildev.springphawtrix.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
@RequiredArgsConstructor
public class WebFluxConfig implements WebFluxConfigurer {

    private final ObjectMapper objectMapper;

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders(HttpHeaders.CONTENT_TYPE, HttpHeaders.ACCEPT)
                .allowedMethods(HttpMethod.POST.name())
        .allowedOrigins("http://localhost:4200");
    }
}

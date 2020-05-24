package ch.phildev.springphawtrix.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.Value;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.lang.NonNull;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
public class JacksonConfig {

    @Bean
    WebFluxConfigurer httpMessageCodecsConfigurer(ObjectMapper objectMapper) {
        return new WebFluxConfigurer() {
            @Override
            public void configureHttpMessageCodecs(@NonNull ServerCodecConfigurer configurer) {
                configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
                configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
            }
        };
    }

    @Bean
    Jackson2ObjectMapperBuilderCustomizer customObjectMapper(JacksonAnnotationIntrospector annotationIntrospector) {
        return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.annotationIntrospector(annotationIntrospector);
    }

    @Bean
    JacksonAnnotationIntrospector lombokPojoBuilderConfig() {
        return new JacksonAnnotationIntrospector() {
            @Override
            public JsonPOJOBuilder.Value findPOJOBuilderConfig(AnnotatedClass ac) {
                if (ac.hasAnnotation(JsonPOJOBuilder.class)) {
                    return super.findPOJOBuilderConfig(ac);
                }
                return new Value("build", "");
            }
        };
    }
}

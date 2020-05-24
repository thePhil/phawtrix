package ch.phildev.springphawtrix.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
@RequiredArgsConstructor
public class WebFluxConfig implements WebFluxConfigurer {

    private final ObjectMapper objectMapper;

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {

//        objectMapper.setAnnotationIntrospector(
//                new JacksonAnnotationIntrospector() {
//                    @Override
//                    public JsonPOJOBuilder.Value findPOJOBuilderConfig(AnnotatedClass ac) {
//                        if (ac.hasAnnotation(JsonPOJOBuilder.class)) {
//                            return super.findPOJOBuilderConfig(ac);
//                        }
//
//                        return new JsonPOJOBuilder.Value("build", "");
//                    }
//                });
//        configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
//        configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));

    }

}

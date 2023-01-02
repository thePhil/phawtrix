package ch.phildev.springphawtrix.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityWebFilterChain(ServerHttpSecurity httpSecurity) {

        httpSecurity
                .csrf().disable()
                .authorizeExchange().anyExchange().permitAll();

        return httpSecurity.build();
    }
}

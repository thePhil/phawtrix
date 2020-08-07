package ch.phildev.springphawtrix.config;

import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

public class WebSecurityConfig {
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                .authorizeExchange().pathMatchers("/**").permitAll();


       return  httpSecurity.build();
    }
}

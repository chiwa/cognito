package com.zengcode.gateway.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // ✅ ปิด CSRF
                .authorizeExchange(exchange -> exchange
                        .pathMatchers("/auth/login").permitAll()  // ✅ เว้น Login
                        .anyExchange().authenticated()            // ✅ นอกนั้นต้อง Auth
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults())); // ✅ แบบใหม่ ไม่ deprecated

        return http.build();
    }
}
package com.hope_health.doctor_service.config;

import com.hope_health.doctor_service.util.JwtAuthConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/doctors/public/**").permitAll() // if you have public endpoints
                        .requestMatchers("/api/doctors/register-doctor").hasRole("admin") // require admin role
                        .anyRequest().authenticated()
                )
                .csrf()
                    .disable()
                .headers(headers -> headers.frameOptions().disable())
                .oauth2ResourceServer().jwt().jwtAuthenticationConverter(jwtAuthConverter);
        http.cors(Customizer.withDefaults());

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }

}


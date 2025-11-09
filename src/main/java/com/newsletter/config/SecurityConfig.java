package com.newsletter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless API
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                // Public read-only endpoints
                .requestMatchers(HttpMethod.GET, "/api/topics/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/subscribers").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/subscribers/**").permitAll()

                // Protected write endpoints (require authentication in production)
                .requestMatchers(HttpMethod.POST, "/api/topics").permitAll()
                .requestMatchers(HttpMethod.PUT, "/api/topics/**").permitAll()
                .requestMatchers(HttpMethod.DELETE, "/api/topics/**").permitAll()
                .requestMatchers("/api/content/**").permitAll()

                // All other requests require authentication
                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

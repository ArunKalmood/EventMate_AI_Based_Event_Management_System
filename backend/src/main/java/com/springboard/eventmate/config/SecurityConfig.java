package com.springboard.eventmate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth

                // =====================
                // PUBLIC
                // =====================
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/events", "/events/**").permitAll()

                // =====================
                // USER ACTIONS
                // =====================
                .requestMatchers(HttpMethod.POST, "/events/*/book").hasRole("USER")
                .requestMatchers(HttpMethod.POST, "/events/*/lost").hasRole("USER")
                .requestMatchers("/payments/**").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/users/me/**").hasRole("USER")
                .requestMatchers(HttpMethod.GET, "/bookings/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/events").hasRole("ORGANIZER")
                
                // =====================
                // REVIEW ACTIONS
                // =====================
                
                .requestMatchers(HttpMethod.GET, "/reviews/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/reviews/**").hasRole("USER")
                .requestMatchers(HttpMethod.PUT, "/reviews/**").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "/reviews/**").hasRole("USER")
                

 
                // =====================
                // ORGANIZER ACTIONS
                // =====================
                .requestMatchers("/organizer/**").hasRole("ORGANIZER")

                // =====================
                // FALLBACK
                // =====================
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

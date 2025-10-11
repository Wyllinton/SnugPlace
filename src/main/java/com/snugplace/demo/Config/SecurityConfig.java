package com.snugplace.demo.Config;

import com.snugplace.demo.Security.JWTAuthenticationEntryPoint;
import com.snugplace.demo.Security.JWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(req -> req
                        // Public Endpoints
                        .requestMatchers("/auth/**", "/users/register").permitAll()
                        .requestMatchers(HttpMethod.GET, "/accommodations/**").permitAll()

                        // Endpoints bookings
                        .requestMatchers(HttpMethod.GET, "/bookings/{id}/detail-user").hasAnyRole("USER","GUEST")
                        .requestMatchers(HttpMethod.GET, "/bookings/{id}/detail").hasRole("HOST")
                        .requestMatchers(HttpMethod.POST, "/bookings", "/bookings/**").hasAnyRole("USER","HOST","ADMIN","GUEST")
                        .requestMatchers(HttpMethod.PUT, "/bookings/{id}/cancel").hasAnyRole("USER","HOST","ADMIN","GUEST")
                        .requestMatchers(HttpMethod.PUT, "/bookings/{id}/confirm").hasRole("HOST")
                        .requestMatchers("/bookings/my-bookings-host/**").hasRole("HOST")
                        .requestMatchers("/bookings/my-bookings-user/**").hasAnyRole("USER","ADMIN","GUEST")

                        // Endpoints accommodations
                        .requestMatchers(HttpMethod.POST, "/accommodations/**").hasRole("HOST")
                        .requestMatchers(HttpMethod.PATCH, "/accommodations/**").hasRole("HOST")
                        .requestMatchers(HttpMethod.DELETE, "/accommodations/**").hasRole("HOST")
                        .requestMatchers("/accommodations/my-accomodations").hasRole("HOST")

                        //Endpoints for Comments
                        .requestMatchers(HttpMethod.POST, "/comments").hasAnyRole("USER","HOST","ADMIN","GUEST")
                        .requestMatchers(HttpMethod.POST, "/comments/answer").hasRole("HOST")

                        //Endpoints Metrics
                        .requestMatchers(HttpMethod.GET, "/metrics/accommodations/{id}").hasRole("HOST")
                        .requestMatchers(HttpMethod.GET, "/metrics/summary").hasRole("HOST")

                        //Endpoints Notification
                        .requestMatchers(HttpMethod.GET, "/notifications/**").hasAnyRole("USER","HOST","ADMIN","GUEST")

                        // Endpoints Images
                        .requestMatchers(HttpMethod.POST, "/images").permitAll()

                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new JWTAuthenticationEntryPoint()))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .securityContext(securityContext -> securityContext.requireExplicitSave(false));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }
}

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

import java.util.Arrays;
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
                        // 🔥 TODOS LOS PERMITALL AL PRINCIPIO Y MÁS ESPECÍFICOS

                        // Public Endpoints - AUTENTICACIÓN
                        .requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/users/register").permitAll()

                        // Public Endpoints - IMÁGENES
                        .requestMatchers(HttpMethod.POST, "/images").permitAll()

                        // 🔥 CORRECCIÓN: Especificar exactamente qué endpoints GET de accommodations son públicos
                        .requestMatchers(HttpMethod.GET, "/accommodations").permitAll()
                        .requestMatchers(HttpMethod.POST, "/accommodations/cards").permitAll()
                        .requestMatchers(HttpMethod.GET, "/accommodations/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/accommodations/search").permitAll()

                        // El resto de tus reglas de autorización...
                        .requestMatchers(HttpMethod.GET, "/bookings/{id}/detail-user").hasAnyRole("USER","GUEST")
                        .requestMatchers(HttpMethod.GET, "/bookings/{id}/detail").hasRole("HOST")
                        .requestMatchers(HttpMethod.POST, "/bookings", "/bookings/**").hasAnyRole("USER","HOST","ADMIN","GUEST")
                        // ... resto de tus reglas actuales

                        .requestMatchers(HttpMethod.POST, "/accommodations/**").hasRole("HOST")
                        .requestMatchers(HttpMethod.PATCH, "/accommodations/**").hasRole("HOST")
                        .requestMatchers(HttpMethod.DELETE, "/accommodations/**").hasRole("HOST")
                        .requestMatchers("/accommodations/my-accomodations").hasRole("HOST")

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

        // ✅ CONFIGURACIÓN ACTUALIZADA CON localhost:4200

        // 1. Orígenes permitidos (incluyendo tu frontend en :4200)
        config.setAllowedOrigins(Arrays.asList(
                "http://localhost:4200",  // ✅ Tu frontend Angular
                "http://localhost:3000",  // React development
                "http://localhost:5173",  // Vite development
                "http://127.0.0.1:4200",  // Alternativa para Angular
                "http://127.0.0.1:3000",
                "https://tudominio.com"   // Tu dominio en producción
        ));

        // 2. Métodos HTTP permitidos
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));

        // 3. Headers permitidos
        config.setAllowedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Requested-With",
                "Accept",
                "Origin",
                "Access-Control-Request-Method",
                "Access-Control-Request-Headers"
        ));

        // 4. Headers expuestos al frontend
        config.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Disposition"
        ));

        // 5. Configuración de credenciales
        config.setAllowCredentials(true);

        // 6. Tiempo de cache para preflight requests
        config.setMaxAge(3600L); // 1 hora

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
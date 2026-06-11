package pl.krasmap.common.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class PermitSecurity {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                // 1. CORS configuration
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 2. Development phase - permit all
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()
                // // Map viewing is completely public
                // .requestMatchers("/api/krasnal/get/all").permitAll()

                // // Only Administrators can delete a Krasnal
                // .requestMatchers("/api/krasnal/delete/**").hasRole("ADMIN")

                // // Adding reviews requires at least a WANDERER account
                // .requestMatchers("/api/reviews/new").hasAnyRole("WANDERER", "EDITOR",
                // "ADMIN")

                // // Block everything else by default just to be safe
                // .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // Allow only queries from local frontend
        configuration.setAllowedOrigins(List.of("http://localhost:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
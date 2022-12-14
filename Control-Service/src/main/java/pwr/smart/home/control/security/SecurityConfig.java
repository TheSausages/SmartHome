package pwr.smart.home.control.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pwr.smart.home.common.configuration.security.KeycloakJwtAuthenticationConverter;

import java.util.Arrays;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                .mvcMatchers("/api/control/air-quality").permitAll()
                .mvcMatchers("/api/control/temperature").permitAll()
                .mvcMatchers("/api/control/humidity").permitAll()
                .mvcMatchers("/api/control/weather").authenticated()
                .mvcMatchers("/api/control/air").authenticated()
                .mvcMatchers("/api/control/*/activate").authenticated()
                .mvcMatchers("/api/control/*/deactivate").authenticated()
                .mvcMatchers("/api/control/*/turnOff").authenticated()
//                .mvcMatchers("/api/control/*/temperature").authenticated()
//                .mvcMatchers("/api/control/*/humidity").authenticated()
//                .mvcMatchers("/api/control/*/air-quality").authenticated()
                .mvcMatchers("/api/**").authenticated()
                .anyRequest().denyAll()
                .and()
                .oauth2ResourceServer()
                .jwt().jwtAuthenticationConverter(new KeycloakJwtAuthenticationConverter());

        return http.build();
    }

    /**
     * Default CORS configuration enabling cors for all endpoints
     * @return CORS configuration
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://frontend", "http://frontend:80", "http://localhost:8180", "http://localhost:3000", "http://192.168.0.245:3000", "http://localhost:8080", "http://192.168.0.245:8080"));
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "OPTIONS", "DELETE", "PUT"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList(HttpHeaders.CONTENT_TYPE, HttpHeaders.AUTHORIZATION));
        configuration.addExposedHeader(HttpHeaders.AUTHORIZATION);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

package pwr.smart.home.data.security;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
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
import pwr.smart.home.data.repository.SensorRepository;

import java.util.Arrays;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {
    private final SensorAuthenticationFilter sensorAuthenticationFilter;

    SecurityConfig(SensorRepository sensorRepository) {
        this.sensorAuthenticationFilter = new SensorAuthenticationFilter(sensorRepository);
    }

    @Bean
    public FilterRegistrationBean<SensorAuthenticationFilter> sensorAuthenticationFilter() {
        FilterRegistrationBean<SensorAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(sensorAuthenticationFilter);
        registrationBean.addUrlPatterns("/api/data/filter");
        registrationBean.addUrlPatterns("/api/data/temperature");
        registrationBean.addUrlPatterns("/api/data/humidifier");
        registrationBean.addUrlPatterns("/api/data/consumption");
        return registrationBean;
    }

    @Bean
    public SecurityFilterChain jwtTokenFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().and()
                .authorizeRequests()
                // The filter will check the security, so we permit the endpoint for all
                .mvcMatchers("/api/data/air-quality").permitAll()
                .mvcMatchers("/api/data/temperature").permitAll()
                .mvcMatchers("/api/data/humidity").permitAll()
                .mvcMatchers("/api/data/consumption").permitAll()

                // Actuator endpoints are for all
                .mvcMatchers("/actuator/**").permitAll()

                // Routes used mainly by control
                .mvcMatchers("/api/data/homes").authenticated()
                .mvcMatchers("/api/data/home/*").authenticated()
                .mvcMatchers("/api/data/latlong/*").authenticated()
                .mvcMatchers("/api/data/homeFunctionalDevices/*").authenticated()
                .mvcMatchers("/api/data/inactive/*").authenticated()
                .mvcMatchers("/api/data/active/*").authenticated()
                .mvcMatchers("/api/data/homeFunctionalDevice/*").authenticated()

                // Mostly by user
                .mvcMatchers("/api/data/measurements").authenticated()
                .mvcMatchers("/api/data/lastAirFilterMeasurements").authenticated()
                .mvcMatchers("/api/data/allAirFilterMeasurements").authenticated()
                .mvcMatchers("/api/data/lastAirConditionerMeasurement").authenticated()
                .mvcMatchers("/api/data/allAirConditionerMeasurements").authenticated()
                .mvcMatchers("/api/data/lastAirHumidifierMeasurements").authenticated()
                .mvcMatchers("/api/data/allAirHumidifierMeasurements").authenticated()
                .mvcMatchers("/api/data/home").authenticated()
                .mvcMatchers("/api/data/setHouseTemperature").authenticated()
                .mvcMatchers("/api/data/setHouseHumidity").authenticated()
                .mvcMatchers("/api/data/*").authenticated()
                .mvcMatchers("/api/**").authenticated()
                .mvcMatchers("/api/data/addSensor").authenticated()
                .mvcMatchers("/api/data/addFunctionalDevice").authenticated()
                .mvcMatchers("/api/data/editAddress").authenticated()
                .mvcMatchers("/v2/api-docs",
                                        "/configuration/ui",
                                        "/swagger-resources/**",
                                        "/configuration/security",
                                        "/swagger-ui.html",
                                        "/webjars/**",
                                        "/swagger-ui/**").permitAll()
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

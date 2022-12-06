package pwr.smart.home.control.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;
import pwr.smart.home.common.error.ErrorDTO;
import pwr.smart.home.control.model.Endpoint;
import pwr.smart.home.control.model.Location;
import pwr.smart.home.control.service.DataService;
import pwr.smart.home.control.weather.OpenMeteo;
import pwr.smart.home.control.weather.model.request.AirQualityRequest;
import pwr.smart.home.control.weather.model.request.ForecastWeatherRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestControllerWithBasePath
public class WeatherController {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeatherController.class);

    @Autowired
    private OpenMeteo openMeteo;

    @Autowired
    private DataService dataService;

    @GetMapping("/weather")
    public ResponseEntity<?> getTodayAndTomorrowWeather(@AuthenticationPrincipal Jwt principal) {
        Optional<Location> location = dataService.getLongLat(principal.getSubject());
        if (location.isPresent()) {
            ForecastWeatherRequest forecastWeatherRequest = new ForecastWeatherRequest(
                    location.get().getLatitude(),
                    location.get().getLongitude(),
                    ForecastWeatherRequest.dailyParameters
            );
            return ResponseEntity.ok(openMeteo.getTodayAndTomorrowWeather(forecastWeatherRequest));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO.builder().message("Wrong location").status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping("/air")
    public ResponseEntity<?> getTodaysAirCondition(@AuthenticationPrincipal Jwt principal) {
        Optional<Location> location = dataService.getLongLat(principal.getSubject());
        if (location.isPresent()) {
            AirQualityRequest airQualityRequest = new AirQualityRequest(
                    location.get().getLatitude(),
                    location.get().getLongitude()
            );
            return ResponseEntity.ok(openMeteo.getCurrentAirCondition(airQualityRequest));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO.builder().message("Wrong location").status(HttpStatus.BAD_REQUEST).build());
    }

}

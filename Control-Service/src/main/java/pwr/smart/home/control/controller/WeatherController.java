package pwr.smart.home.control.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;
import pwr.smart.home.control.model.Endpoint;
import pwr.smart.home.control.model.Location;
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
    OpenMeteo openMeteo;

    @GetMapping("/weather/{house_id}")
    public ResponseEntity<?> getTodayAndTomorrowWeather(@PathVariable Long house_id) {
        Optional<Location> location = getLongLat(house_id);
        if (location.isPresent()) {
            ForecastWeatherRequest forecastWeatherRequest = new ForecastWeatherRequest(
                    location.get().getLatitude(),
                    location.get().getLongitude(),
                    ForecastWeatherRequest.dailyParameters
            );
            return ResponseEntity.ok(openMeteo.getTodayAndTomorrowWeather(forecastWeatherRequest));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong location");
    }

    @GetMapping("/air/{house_id}")
    public ResponseEntity<?> getTodaysAirCondition(@PathVariable Long house_id) {
        Optional<Location> location = getLongLat(house_id);
        if (location.isPresent()) {
            AirQualityRequest airQualityRequest = new AirQualityRequest(
                    location.get().getLatitude(),
                    location.get().getLongitude()
            );
            return ResponseEntity.ok(openMeteo.getCurrentAirCondition(airQualityRequest));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong location");
    }

    private Optional<Location> getLongLat(Long houseId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        try {
            Map<String, Long> params = new HashMap<>();
            params.put("house_id", houseId);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Location> response = restTemplate.exchange(Endpoint.DATA_SERVICE_URL.url + "/latlong/{house_id}", HttpMethod.GET, entity, Location.class, params);
            Location returned = Objects.requireNonNull(response.getBody());
            LOGGER.info(returned.getLongitude() + String.valueOf(returned.getLatitude()));
            return Optional.of(returned);
        } catch (ResourceAccessException e) {
            LOGGER.error(e.getMessage());
        }
        return Optional.empty();
    }
}

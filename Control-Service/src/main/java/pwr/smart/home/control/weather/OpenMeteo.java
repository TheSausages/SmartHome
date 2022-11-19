package pwr.smart.home.control.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pwr.smart.home.control.weather.model.request.AirQualityRequest;
import pwr.smart.home.control.weather.model.request.ForecastWeatherRequest;
import pwr.smart.home.control.weather.model.response.AirQualityResponse;
import pwr.smart.home.control.weather.model.response.ForecastWeatherResponse;

import java.net.URI;
import java.util.Objects;

@Service
public class OpenMeteo {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMeteo.class);
    private final static String WEATHER_URL = "https://api.open-meteo.com";
    private final static String FORECAST_PATH = "/v1/forecast/";

    private final static String AIR_QUALITY_URL = "https://air-quality-api.open-meteo.com";
    private final static String AIR_QUALITY_PATH = "/v1/air-quality";


    public ForecastWeatherResponse getTodayAndTomorrowWeather(ForecastWeatherRequest value) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            URI uri = UriComponentsBuilder.fromUriString(WEATHER_URL + FORECAST_PATH + value.toString()).build().encode().toUri();

            ResponseEntity<ForecastWeatherResponse> response = restTemplate.exchange(uri, HttpMethod.GET, entity, ForecastWeatherResponse.class);
            ForecastWeatherResponse returned = Objects.requireNonNull(response.getBody());
            LOGGER.info(String.valueOf(returned));
            return returned;
        } catch (ResourceAccessException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    public AirQualityResponse getCurrentAirCondition(AirQualityRequest airQualityRequest) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            URI uri = UriComponentsBuilder.fromUriString(AIR_QUALITY_URL + AIR_QUALITY_PATH + airQualityRequest.toString()).build().encode().toUri();

            ResponseEntity<AirQualityResponse> response = restTemplate.exchange(uri, HttpMethod.GET, entity, AirQualityResponse.class);
            AirQualityResponse returned = Objects.requireNonNull(response.getBody());
            LOGGER.info(returned.toString());
            return returned;
        } catch (ResourceAccessException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }
}

package pwr.smart.home.control.service;

import dev.failsafe.Failsafe;
import dev.failsafe.RetryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import pwr.smart.home.control.model.Endpoint;
import pwr.smart.home.control.model.FunctionalDeviceWithMeasurementsDTO;
import pwr.smart.home.control.model.Home;
import pwr.smart.home.common.model.Location;

import java.time.Duration;
import java.util.*;

@Service
public class DataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataService.class);

    private final RetryPolicy<Object> retryPolicy = RetryPolicy.builder()
            .onRetry(e -> LOGGER.info("Try " + e.getAttemptCount() + " to communicate with data service with message: " + e.toString()))
            .withDelay(Duration.ofSeconds(30))
            .withMaxRetries(3)
            .build();

    @Autowired
    private Endpoint endpoint;

    public Optional<Location> getLongLat(String userId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        try {
            Map<String, String> params = new HashMap<>();
            params.put("userId", userId);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Location> response = Failsafe.with(retryPolicy).get(() -> restTemplate.exchange(endpoint.getDataServiceUrl() + "/latlong/{userId}", HttpMethod.GET, entity, Location.class, params));
            Location returned = Objects.requireNonNull(response.getBody());
            return Optional.of(returned);
        } catch (ResourceAccessException e) {
            LOGGER.error(e.getMessage());
        }
        return Optional.empty();
    }

    public List<Home> getHomes() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        try {
            // IMPORTANT - the type must be present in the ParameterizedTypeReference - will not compile without it
            ResponseEntity<List<Home>> response = Failsafe.with(retryPolicy).get(() -> restTemplate.exchange(endpoint.getDataServiceUrl() + "/homes", HttpMethod.GET, null, new ParameterizedTypeReference<List<Home>>() {}));
            return Objects.requireNonNull(response.getBody());
        } catch (ResourceAccessException e) {
            LOGGER.error(e.getMessage());
        }
        return List.of();
    }

    public Home getHome(String serialNumber) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        try {
            Map<String, String> params = new HashMap<>();
            params.put("serialNumber", serialNumber);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            // IMPORTANT - the type must be present in the ParameterizedTypeReference - will not compile without it
            ResponseEntity<Home> response = Failsafe.with(retryPolicy).get(() -> restTemplate.exchange(endpoint.getDataServiceUrl() + "/home/{serialNumber}", HttpMethod.GET, entity, new ParameterizedTypeReference<Home>() {}, params));
            return Objects.requireNonNull(response.getBody());
        } catch (ResourceAccessException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }
    
    public List<FunctionalDeviceWithMeasurementsDTO> getFunctionalDevicesWithMeasurementsForHome(Home home) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        try {
            Map<String, String> params = new HashMap<>();
            params.put("homeId", home.getId().toString());
            HttpEntity<String> entity = new HttpEntity<>(headers);
            // IMPORTANT - the type must be present in the ParameterizedTypeReference - will not compile without it
            ResponseEntity<List<FunctionalDeviceWithMeasurementsDTO>> response = Failsafe.with(retryPolicy).get(() -> restTemplate.exchange(endpoint.getDataServiceUrl() + "/homeFunctionalDevices/{homeId}", HttpMethod.GET, entity, new ParameterizedTypeReference<List<FunctionalDeviceWithMeasurementsDTO>>() {}, params));
            return Objects.requireNonNull(response.getBody());
        } catch (ResourceAccessException e) {
            LOGGER.error(e.getMessage());
        }
        return List.of();
    }

    public void markDeviceAsInactive(String serialNumber) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            Map<String, String> params = new HashMap<>();
            params.put("serialNumber", serialNumber);
            ResponseEntity<Void> response = Failsafe.with(retryPolicy).get(() -> restTemplate.getForEntity(endpoint.getDataServiceUrl() + "/inactive/{serialNumber}", Void.TYPE, params));

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ResourceAccessException("Could not set device " + serialNumber + " as inactive");
            }
        } catch (ResourceAccessException e) {
            LOGGER.error(e.getMessage());
        }
    }

    public void markDeviceAsActive(String serialNumber) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            Map<String, String> params = new HashMap<>();
            params.put("serialNumber", serialNumber);
            ResponseEntity<Void> response = Failsafe.with(retryPolicy).get(() -> restTemplate.getForEntity(endpoint.getDataServiceUrl() + "/active/{serialNumber}", Void.TYPE, params));

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new ResourceAccessException("Could not set device " + serialNumber + " as inactive");
            }
        } catch (ResourceAccessException e) {
            LOGGER.error(e.getMessage());
        }
    }
}

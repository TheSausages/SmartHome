package pwr.smart.home.control.service;

import lombok.Data;
import lombok.NoArgsConstructor;
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
import pwr.smart.home.control.model.Location;

import java.util.*;

@Service
public class DataService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataService.class);

    @Autowired
    private Endpoint endpoint;

    public Optional<Location> getLongLat(String userId) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        try {
            Map<String, String> params = new HashMap<>();
            params.put("userId", userId);
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Location> response = restTemplate.exchange(endpoint.getDataServiceUrl() + "/latlong/{userId}", HttpMethod.GET, entity, Location.class, params);
            Location returned = Objects.requireNonNull(response.getBody());
            LOGGER.info(returned.getLongitude() + String.valueOf(returned.getLatitude()));
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
            ResponseEntity<List<Home>> response = restTemplate.exchange(endpoint.getDataServiceUrl() + "/homes", HttpMethod.GET, null, new ParameterizedTypeReference<>() {});
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
            ResponseEntity<Home> response = restTemplate.exchange(endpoint.getDataServiceUrl() + "/home/{serialNumber}", HttpMethod.GET, entity, new ParameterizedTypeReference<>() {}, params);
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
            ResponseEntity<List<FunctionalDeviceWithMeasurementsDTO>> response = restTemplate.exchange(endpoint.getDataServiceUrl() + "/homeFunctionalDevices/{homeId}", HttpMethod.GET, entity, new ParameterizedTypeReference<>() {}, params);
            return Objects.requireNonNull(response.getBody());
        } catch (ResourceAccessException e) {
            LOGGER.error(e.getMessage());
        }
        return List.of();
    }
}

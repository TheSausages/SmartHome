package pwr.smart.home.data.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import pwr.smart.home.data.model.Location;

import java.net.URI;
import java.util.Objects;

public class AddressConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddressConverter.class);
    private static final String URL = "https://nominatim.openstreetmap.org/search?";

    public static Location convertToLatLong(String postalCode) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        try {
            HttpEntity<String> entity = new HttpEntity<>(headers);
            URI uri = UriComponentsBuilder.fromUriString(URL + "q=" + postalCode + "&format=json").build().encode().toUri();

            ResponseEntity<?> response = restTemplate.exchange(uri , HttpMethod.GET, entity, String.class);
            String responseString = Objects.requireNonNull(response.getBody()).toString();
            JSONObject jsonObject = new JSONObject(responseString.substring(1, responseString.length() - 1));
            String latitude = jsonObject.getString("lat");
            String longitude = jsonObject.getString("lon");
            Location location = new Location(Float.parseFloat(latitude), Float.parseFloat(longitude));
            return location;
        } catch (ResourceAccessException | JSONException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }
}
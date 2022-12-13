package pwr.smart.home.control.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;


@Service
public class DataEmitter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataEmitter.class);

    public String callForAction(String body, String endpoint, int level) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Basic dXNlcjpwYXNzd29yZA==");
        headers.set("PowerLevel", String.valueOf(level));

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<?> response = restTemplate.exchange(endpoint, HttpMethod.POST, entity, String.class);
            return Objects.requireNonNull(response.getBody()).toString();
        } catch (ResourceAccessException e) {
            LOGGER.error(e.getMessage());
        }
        return "";
    }
}

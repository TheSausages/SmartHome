package pwr.smart.home.control.service;

import dev.failsafe.Failsafe;
import dev.failsafe.RetryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Objects;


@Service
public class DataEmitter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataEmitter.class);

    @Autowired
    DataService dataService;

    private final RetryPolicy<Object> retryPolicy = RetryPolicy.builder()
            .onRetry(e -> LOGGER.info("Try " + e.getAttemptCount() + " to send target with result: " + e.toString()))
            .withDelay(Duration.ofSeconds(30))
            .withMaxRetries(3)
            .build();

    public String callForAction(String body, String endpoint, int level, String serialNumber) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.set("Authorization", "Basic dXNlcjpwYXNzd29yZA==");
        headers.set("PowerLevel", String.valueOf(level));

        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<?> response = Failsafe.with(retryPolicy).get(() -> restTemplate.exchange(endpoint, HttpMethod.POST, entity, String.class));

            return Objects.requireNonNull(response.getBody()).toString();
        } catch (Exception e) {
            dataService.markDeviceAsInactive(serialNumber);

            LOGGER.error(e.getMessage());
        }
        return "";
    }
}

package pwr.smart.home.control.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;

@RestControllerWithBasePath
public class ActionEndpoints {

    @PostMapping("/temperature")
    public ResponseEntity<?> setTargetTemperature(@RequestParam double targetTemperature) {
        return ResponseEntity.ok(targetTemperature);
    }

    @PostMapping("/air-quality")
    public ResponseEntity<?> setTargetAirQuality(@RequestParam int targetAirQuality) {
        return ResponseEntity.ok(targetAirQuality);
    }

    @PostMapping("/humidity")
    public ResponseEntity<?> setTargetHumidity(@RequestParam int targetHumidity) {
        return ResponseEntity.ok(targetHumidity);
    }
}

package pwr.smart.home.air.humidifier.device.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pwr.smart.home.air.humidifier.sensor.service.StatusService;

@RestController
public class AirHumidifierActionController {

    @Autowired
    StatusService statusService;

    @PostMapping("/setTarget")
    public ResponseEntity<String> setTargetHumidity(@RequestBody String targetHumidity) {
        int targetHumidityInt = Integer.parseInt(targetHumidity);

        if (targetHumidityInt < 0 || targetHumidityInt > 100) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Humidity out of range.");
        } else {

            try {
                statusService.setTargetHumidity(targetHumidityInt);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return ResponseEntity.status(HttpStatus.OK).body(statusService.getState().name());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        return ResponseEntity.ok(String.format("%.0f", StatusService.getCurrentHumidity()));
    }
}

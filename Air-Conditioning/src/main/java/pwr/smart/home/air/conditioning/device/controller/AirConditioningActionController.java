package pwr.smart.home.air.conditioning.device.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pwr.smart.home.air.conditioning.sensor.service.StatusService;

@RestController
public class AirConditioningActionController {

    @Autowired
    StatusService statusService;

    @PostMapping("/setTarget")
    public ResponseEntity<String> setTargetTemperature(@RequestBody String targetTemperature) {
        double targetTemperatureDouble = Double.parseDouble(targetTemperature);

        if (targetTemperatureDouble > 40 || targetTemperatureDouble < 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Temperature out of range.");
        } else {

            try {
                statusService.setTargetTemperature(targetTemperatureDouble);
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            return ResponseEntity.status(HttpStatus.OK).body(statusService.getState().name());
        }
    }
}

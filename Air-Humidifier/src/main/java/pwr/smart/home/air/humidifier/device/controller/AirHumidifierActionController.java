package pwr.smart.home.air.humidifier.device.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pwr.smart.home.air.humidifier.device.model.State;
import pwr.smart.home.air.humidifier.sensor.service.StatusService;

@RestController
public class AirHumidifierActionController {

    private final static Logger LOGGER = LoggerFactory.getLogger(AirHumidifierActionController.class);

    @Autowired
    StatusService statusService;

    @PostMapping("/setTarget")
    public ResponseEntity<String> setTargetHumidity(@RequestBody String targetHumidity, @RequestHeader("PowerLevel") int powerLevel) {

        LOGGER.info("Received action request. Target humidity: " + targetHumidity + ". Power level: " + powerLevel);

        if (statusService.getState() != State.OFF) {
            LOGGER.info("Action in progress. Ignoring action request.");
            return ResponseEntity.status(HttpStatus.OK).body(statusService.getState().name());
        } else {

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
    }
}

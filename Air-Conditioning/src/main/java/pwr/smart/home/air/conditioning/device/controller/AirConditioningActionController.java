package pwr.smart.home.air.conditioning.device.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pwr.smart.home.air.conditioning.device.model.State;
import pwr.smart.home.air.conditioning.sensor.service.StatusService;

@RestController
public class AirConditioningActionController {

    private final static Logger LOGGER = LoggerFactory.getLogger(AirConditioningActionController.class);

    @Autowired
    StatusService statusService;

    @PostMapping("/setTarget")
    public ResponseEntity<String> setTargetTemperature(@RequestBody String targetTemperature, @RequestHeader("PowerLevel") int powerLevel) {

        LOGGER.info("Received action request. Target temperature: " + targetTemperature + ". Power level: " + powerLevel);

        if (statusService.getState() != State.OFF) {
            LOGGER.info("Action in progress. Ignoring action request.");
            return ResponseEntity.status(HttpStatus.OK).body(statusService.getState().name());
        } else {

            double targetTemperatureDouble = Double.parseDouble(targetTemperature);

            if (targetTemperatureDouble > 40 || targetTemperatureDouble < 5) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Temperature out of range.");
            } else {

                try {
                    statusService.setTargetTemperature(targetTemperatureDouble, powerLevel);
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                return ResponseEntity.status(HttpStatus.OK).body(statusService.getState().name());
            }
        }
    }
}

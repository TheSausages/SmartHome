package pwr.smart.home.air.filter.device.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.smart.home.air.filter.device.model.State;
import pwr.smart.home.air.filter.sensor.service.StatusService;


@RestController
public class AirFilterActionController {

    private final static Logger LOGGER = LoggerFactory.getLogger(AirFilterActionController.class);

    @Autowired
    StatusService statusService;

    @PostMapping("/setTarget")
    public ResponseEntity<String> setTargetAirQuality(@RequestBody String targetAirQuality, @RequestHeader("PowerLevel") int powerLevel) {

        LOGGER.info("Received action request. Target air quality: " + targetAirQuality + ". Power level: " + powerLevel);

        switch (statusService.getState()) {
            case OFF:
                int targetAirQualityInt = Integer.parseInt(targetAirQuality);

                if (targetAirQualityInt < 1) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Air Quality value out of range.");
                } else {

                    try {
                        statusService.setAirQualityValue(targetAirQualityInt);
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    return ResponseEntity.status(HttpStatus.OK).body(statusService.getState().name());
                }

            case WORKING:
                LOGGER.info("Action in progress. Ignoring action request.");
                return ResponseEntity.status(HttpStatus.OK).body(statusService.getState().name());

            case PERMANENT_OFF:
                LOGGER.info("Permanently off. Ignoring action request.");
                return ResponseEntity.status(HttpStatus.OK).body(statusService.getState().name());

            default:
                LOGGER.info("Unknown State {}. Ignoring action request.", statusService.getState());
                return ResponseEntity.status(HttpStatus.OK).body(statusService.getState().name());
        }
    }

    @GetMapping("/activate")
    public ResponseEntity<Void> activateDevice() {
        statusService.setState(State.OFF);
        LOGGER.info("Activate device");

        return ResponseEntity.ok(null);
    }

    @GetMapping("/deactivate")
    public ResponseEntity<Void> deactivateDevice() {
        statusService.setState(State.PERMANENT_OFF);
        LOGGER.info("Deactivate device");

        return ResponseEntity.ok(null);
    }
}

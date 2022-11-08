package pwr.smart.home.air.filter.device.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pwr.smart.home.air.filter.sensor.service.StatusService;


@RestController
public class AirFilterActionController {

    @Autowired
    StatusService statusService;

    @PostMapping("/setTarget")
    public ResponseEntity<String> setTargetAirQuality(@RequestBody String targetAirQuality) {
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
    }
}

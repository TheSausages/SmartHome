package pwr.smart.home.data.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;
import pwr.smart.home.data.model.AirConditionerData;
import pwr.smart.home.data.service.AirConditionerService;

@RestControllerWithBasePath
public class AirConditioningController {
    private static final Logger logger = LoggerFactory.getLogger(AirConditioningController.class);

    @Autowired
    private AirConditionerService airConditionerService;

    @PostMapping("/temperature")
    public ResponseEntity<?> getAirConditionerMeasurements(@RequestBody AirConditionerData airConditionerData) {
        logger.info(airConditionerData.toString());
        airConditionerService.addAirConditionerMeasurements(airConditionerData);
        return ResponseEntity.ok().build();
    }
}

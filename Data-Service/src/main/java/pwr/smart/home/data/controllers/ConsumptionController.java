package pwr.smart.home.data.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;

import pwr.smart.home.data.model.ConsumptionData;
import pwr.smart.home.data.service.ConsumptionService;

@RestControllerWithBasePath
public class ConsumptionController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AirFilterController.class);

    @Autowired
    ConsumptionService consumptionService;

    @PostMapping("/consumption")
    public ResponseEntity<?> receiveConsumption(@RequestBody ConsumptionData consumptionData) {
        LOGGER.info("Received consumption: " + consumptionData.toString());
        consumptionService.addConsumption(consumptionData);
        return ResponseEntity.ok().build();
    }
}

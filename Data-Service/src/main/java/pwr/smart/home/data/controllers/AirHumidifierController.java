package pwr.smart.home.data.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;
import pwr.smart.home.data.dao.Measurement;

import pwr.smart.home.data.model.AirHumidifierData;
import pwr.smart.home.data.service.AirHumidifierDataService;

import java.util.List;

@RestControllerWithBasePath
public class AirHumidifierController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AirFilterController.class);

    @Autowired
    AirHumidifierDataService airHumidifierDataService;

    @PostMapping("/humidifier")
    public ResponseEntity<?> getHumidifierMeasurements(@RequestBody AirHumidifierData humidifierData) {
        LOGGER.info(humidifierData.toString());
        airHumidifierDataService.addAirHumidifierMeasurements(humidifierData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/lastAirHumidifierMeasurements")
    @ResponseBody
    public List<Measurement> getLastAirHumidifierMeasurements(String sensorSerialNumber) {
        return airHumidifierDataService.getLastAirHumidifierMeasurements(sensorSerialNumber);
    }
}

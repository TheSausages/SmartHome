package pwr.smart.home.data.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;

import pwr.smart.home.data.model.AirHumidifierData;
import pwr.smart.home.data.model.enums.SensorType;
import pwr.smart.home.data.service.AirHumidifierService;
import pwr.smart.home.data.service.MeasurementService;

@RestControllerWithBasePath
public class AirHumidifierController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AirFilterController.class);

    @Autowired
    AirHumidifierService airHumidifierService;

    @Autowired
    private MeasurementService measurementService;

    @PostMapping("/humidifier")
    public ResponseEntity<?> getHumidifierMeasurements(@RequestBody AirHumidifierData humidifierData) {
        LOGGER.info(humidifierData.toString());
        airHumidifierService.addAirHumidifierMeasurements(humidifierData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/lastAirHumidifierMeasurement")
    public ResponseEntity<?> getLastAirHumidifierMeasurement(String sensorSerialNumber) {
        if (measurementService.isSensorCompatibleType(sensorSerialNumber, SensorType.AIR_HUMIDITY)) {
            return ResponseEntity.ok(airHumidifierService.getLastAirHumidifierMeasurement(sensorSerialNumber));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incompatible sensor");
        }
    }

    @GetMapping("/allAirHumidifierMeasurements")
    public ResponseEntity<?> getAllAirHumidifierMeasurements(@RequestParam String sensorSerialNumber, @RequestParam Integer page, @RequestParam Integer size) {
        Pageable pageableSetting = Pageable.unpaged();
        if (page != null && size != null) {
            pageableSetting = PageRequest.of(page, size, Sort.by("createdAt"));
        }

        if (measurementService.isSensorCompatibleType(sensorSerialNumber, SensorType.AIR_HUMIDITY)) {
            return ResponseEntity.ok(measurementService.getAllMeasurements(sensorSerialNumber, pageableSetting));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incompatible sensor");
        }
    }
}

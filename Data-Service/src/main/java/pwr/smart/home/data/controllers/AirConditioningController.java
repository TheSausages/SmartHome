package pwr.smart.home.data.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;
import pwr.smart.home.data.model.AirConditionerData;
import pwr.smart.home.data.model.enums.SensorType;
import pwr.smart.home.data.service.AirConditionerService;
import pwr.smart.home.data.service.MeasurementService;

@RestControllerWithBasePath
public class AirConditioningController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AirConditioningController.class);

    @Autowired
    private AirConditionerService airConditionerService;

    @Autowired
    private MeasurementService measurementService;

    @PostMapping("/temperature")
    public ResponseEntity<?> getAirConditionerMeasurements(@RequestBody AirConditionerData airConditionerData) {
        LOGGER.info(airConditionerData.toString());
        airConditionerService.addAirConditionerMeasurements(airConditionerData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/lastAirConditionerMeasurement")
    public ResponseEntity<?> getLastAirConditionerMeasurement(@RequestParam String sensorSerialNumber) {
        if (measurementService.isSensorCompatibleType(sensorSerialNumber, SensorType.TEMPERATURE)) {
            return ResponseEntity.ok(airConditionerService.getLastAirConditionerMeasurement(sensorSerialNumber));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incompatible sensor");
        }
    }

    @GetMapping("/allAirConditionerMeasurements")
    public ResponseEntity<?> getAllAirConditionerMeasurements(@RequestParam String sensorSerialNumber, @RequestParam Integer page, @RequestParam Integer size) {
        Pageable pageableSetting = Pageable.unpaged();
        if (page != null && size != null) {
            pageableSetting = PageRequest.of(page, size, Sort.by("createdAt"));
        }

        if (measurementService.isSensorCompatibleType(sensorSerialNumber, SensorType.TEMPERATURE)) {
            return ResponseEntity.ok(measurementService.getAllMeasurements(sensorSerialNumber, pageableSetting));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incompatible sensor");
        }

    }
}

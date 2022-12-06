package pwr.smart.home.data.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;
import pwr.smart.home.common.error.ErrorDTO;
import pwr.smart.home.data.dao.User;
import pwr.smart.home.data.model.AirConditionerData;
import pwr.smart.home.data.model.enums.SensorType;
import pwr.smart.home.data.service.AirConditionerService;
import pwr.smart.home.data.service.MeasurementService;
import pwr.smart.home.data.service.SensorService;
import pwr.smart.home.data.service.UserService;

import java.util.UUID;

@RestControllerWithBasePath
public class AirConditioningController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AirConditioningController.class);

    @Autowired
    private AirConditionerService airConditionerService;

    @Autowired
    private MeasurementService measurementService;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private UserService userService;

    @PostMapping("/temperature")
    public ResponseEntity<?> receiveAirConditionerMeasurements(@RequestBody AirConditionerData airConditionerData) {
        LOGGER.info(airConditionerData.toString());
        airConditionerService.addAirConditionerMeasurements(airConditionerData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/lastAirConditionerMeasurement")
    public ResponseEntity<?> getLastAirConditionerMeasurement(@AuthenticationPrincipal Jwt principal,
                                                              @RequestParam String sensorSerialNumber) {
        if (!hasAccess(principal, sensorSerialNumber))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorDTO.builder().message("This is not your sensor!").status(HttpStatus.UNAUTHORIZED).build());
        if (measurementService.isSensorCompatibleType(sensorSerialNumber, SensorType.TEMPERATURE)) {
            return ResponseEntity.ok(airConditionerService.getLastAirConditionerMeasurement(sensorSerialNumber));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO.builder().message("Incompatible sensor").status(HttpStatus.BAD_REQUEST).build());
        }
    }

    private boolean hasAccess(Jwt principal, String sensorSerialNumber) {
        return sensorService.isSensorInHome(sensorSerialNumber,
                userService.findHomeByUserId(UUID.fromString(principal.getSubject())).map(User::getHome).orElse(null));
    }
}

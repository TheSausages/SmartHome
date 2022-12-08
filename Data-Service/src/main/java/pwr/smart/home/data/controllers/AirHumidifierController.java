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
import pwr.smart.home.data.model.AirHumidifierData;
import pwr.smart.home.common.model.enums.SensorType;
import pwr.smart.home.data.service.AirHumidifierService;
import pwr.smart.home.data.service.MeasurementService;
import pwr.smart.home.data.service.SensorService;
import pwr.smart.home.data.service.UserService;

import java.util.UUID;

@RestControllerWithBasePath
public class AirHumidifierController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AirHumidifierController.class);

    @Autowired
    private AirHumidifierService airHumidifierService;

    @Autowired
    private MeasurementService measurementService;

    @Autowired
    private UserService userService;

    @Autowired
    private SensorService sensorService;

    @PostMapping("/humidity")
    public ResponseEntity<?> receiveHumidifierMeasurements(@RequestBody AirHumidifierData humidifierData) {
        LOGGER.info(humidifierData.toString());
        airHumidifierService.addAirHumidifierMeasurements(humidifierData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/lastAirHumidifierMeasurement")
    public ResponseEntity<?> getLastAirHumidifierMeasurement(@AuthenticationPrincipal Jwt principal,
                                                             @RequestParam String sensorSerialNumber) {
        if (!hasAccess(principal, sensorSerialNumber))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorDTO.builder().message("This is not your sensor!").status(HttpStatus.UNAUTHORIZED).build());
        if (measurementService.isSensorCompatibleType(sensorSerialNumber, SensorType.AIR_HUMIDITY)) {
            return ResponseEntity.ok(airHumidifierService.getLastAirHumidifierMeasurement(sensorSerialNumber));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO.builder().message("Incompatible sensor").status(HttpStatus.BAD_REQUEST).build());
        }
    }

    private boolean hasAccess(Jwt principal, String sensorSerialNumber) {
        return sensorService.isSensorInHome(sensorSerialNumber,
                userService.findHomeByUserId(UUID.fromString(principal.getSubject())).map(User::getHome).orElse(null));
    }
}

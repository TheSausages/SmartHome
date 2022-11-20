package pwr.smart.home.data.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;
import pwr.smart.home.common.error.ErrorDTO;
import pwr.smart.home.data.dao.User;
import pwr.smart.home.data.service.MeasurementService;
import pwr.smart.home.data.service.SensorService;
import pwr.smart.home.data.service.UserService;

import java.sql.Timestamp;
import java.util.UUID;

@RestControllerWithBasePath
public class MeasurementsController {

    @Autowired
    private MeasurementService measurementService;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private UserService userService;

    @GetMapping("/measurements")
    public ResponseEntity<?> getMeasurementsBetweenDates(@AuthenticationPrincipal Jwt principal,
                                                         @RequestParam String sensorSerialNumber,
                                                         @RequestParam Timestamp fromDate,
                                                         @RequestParam Timestamp toDate) {
        if (!hasAccess(principal, sensorSerialNumber))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorDTO.builder().message("This is not your sensor!").status(HttpStatus.UNAUTHORIZED).build());
        return ResponseEntity.ok(measurementService.getMeasurementsBetweenDates(sensorSerialNumber, fromDate, toDate));
    }

    private boolean hasAccess(Jwt principal, String sensorSerialNumber) {
        return sensorService.isSensorInHome(sensorSerialNumber,
                userService.findHomeByUserId(UUID.fromString(principal.getSubject())).map(User::getHome).orElse(null));
    }
}

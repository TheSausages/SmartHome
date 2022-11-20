package pwr.smart.home.data.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;
import pwr.smart.home.data.dao.User;
import pwr.smart.home.data.model.AirFilterData;
import pwr.smart.home.data.model.enums.SensorType;
import pwr.smart.home.data.service.AirFilterService;
import pwr.smart.home.data.service.MeasurementService;
import pwr.smart.home.data.service.SensorService;
import pwr.smart.home.data.service.UserService;

@RestControllerWithBasePath
public class AirFilterController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AirFilterController.class);

    @Autowired
    private AirFilterService airFilterService;

    @Autowired
    private MeasurementService measurementService;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private UserService userService;

    @PostMapping("/air-quality")
    public ResponseEntity<?> getFilterMeasurements(@RequestBody AirFilterData filterData) {
        LOGGER.info(filterData.toString());
        airFilterService.addAirFilterMeasurements(filterData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/lastAirFilterMeasurements")
    public ResponseEntity<?> getLastAirFilterMeasurements(@AuthenticationPrincipal Jwt principal,
                                                          @RequestParam String sensorSerialNumber) {
        if (!sensorService.isSensorInHome(sensorSerialNumber,
                userService.findHomeByUserId(principal.getSubject()).map(User::getHome).orElse(null)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This is not your sensor!");
        if (measurementService.isSensorCompatibleType(sensorSerialNumber, SensorType.AIR_POLLUTION)) {
            return ResponseEntity.ok(airFilterService.getLastAirFilterMeasurements(sensorSerialNumber));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incompatible sensor");
        }
    }

    @GetMapping("/allAirFilterMeasurements")
    public ResponseEntity<?> getAllAirFilterMeasurements(@AuthenticationPrincipal Jwt principal,
                                                         @RequestParam String sensorSerialNumber,
                                                         @RequestParam Integer page,
                                                         @RequestParam Integer size) {
        Pageable pageableSetting = Pageable.unpaged();
        if (page != null && size != null) {
            pageableSetting = PageRequest.of(page, size, Sort.by("createdAt"));
        }

        if (!sensorService.isSensorInHome(sensorSerialNumber,
                userService.findHomeByUserId(principal.getSubject()).map(User::getHome).orElse(null)))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("This is not your sensor!");
        if (measurementService.isSensorCompatibleType(sensorSerialNumber, SensorType.AIR_POLLUTION)) {
            return ResponseEntity.ok(measurementService.getAllMeasurements(sensorSerialNumber, pageableSetting));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incompatible sensor");
        }
    }
}

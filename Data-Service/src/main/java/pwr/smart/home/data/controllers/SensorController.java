package pwr.smart.home.data.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;
import pwr.smart.home.common.error.ErrorDTO;
import pwr.smart.home.data.dao.FunctionalDevice;
import pwr.smart.home.data.dao.Sensor;
import pwr.smart.home.data.dao.User;
import pwr.smart.home.data.service.SensorService;
import pwr.smart.home.data.service.UserService;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestControllerWithBasePath
public class SensorController {
    @Autowired
    private UserService userHomeService;
    @Autowired
    private SensorService sensorService;

    @GetMapping("/homeSensors")
    public ResponseEntity<?> getHomeSensors(@AuthenticationPrincipal Jwt principal) {
        Optional<User> user = userHomeService.findHomeByUserId(UUID.fromString(principal.getSubject()));
        if (user.isPresent()) {
            List<Sensor> sensors = sensorService.findAllHomeSensors(user.get().getHome());
            return ResponseEntity.ok(sensors);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorDTO.builder().message("Wrong house").status(HttpStatus.BAD_REQUEST).build());
        }
    }

    @PostMapping("/addSensor")
    public ResponseEntity<?> addNewHomeSensor(@AuthenticationPrincipal Jwt principal, @RequestBody Sensor sensor) {
        Optional<User> user = userHomeService.findHomeByUserId(UUID.fromString(principal.getSubject()));
        if (!checkIfFieldsAreNotEmpty(sensor)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorDTO.builder().message("Fill the form correctly").status(HttpStatus.BAD_REQUEST).build());
        }
        if (user.isPresent()) {
            sensor.setHomeId(user.get().getHome().getId());
            sensor.setCreatedAt(new Date(System.currentTimeMillis()));
            sensorService.addNewSensor(sensor);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorDTO.builder().message("Wrong house").status(HttpStatus.UNAUTHORIZED).build());
        }
    }

    private boolean checkIfFieldsAreNotEmpty(Sensor sensor) {
        return StringUtils.hasText(sensor.getName()) &&
                StringUtils.hasText(sensor.getManufacturer()) &&
                StringUtils.hasText(sensor.getSerialNumber());
    }
}

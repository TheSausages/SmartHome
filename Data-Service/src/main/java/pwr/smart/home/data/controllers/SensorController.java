package pwr.smart.home.data.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;
import pwr.smart.home.common.error.ErrorDTO;
import pwr.smart.home.data.dao.Sensor;
import pwr.smart.home.data.dao.User;
import pwr.smart.home.data.service.SensorService;
import pwr.smart.home.data.service.UserService;

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
}

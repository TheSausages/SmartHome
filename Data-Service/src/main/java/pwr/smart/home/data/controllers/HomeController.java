package pwr.smart.home.data.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;
import pwr.smart.home.common.error.ErrorDTO;
import pwr.smart.home.data.dao.Home;
import pwr.smart.home.data.dao.User;
import pwr.smart.home.data.model.Location;
import pwr.smart.home.data.service.HomeService;
import pwr.smart.home.data.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestControllerWithBasePath
public class HomeController {
    @Autowired
    private UserService userHomeService;
    @Autowired
    private HomeService homeService;

    @GetMapping("/homes")
    public ResponseEntity<?> getHomes() {
        List<Home> homes = homeService.findAllHomesWithActiveFunctionalDevices();

        return ResponseEntity.ok(homes);
    }

    @GetMapping("/latlong/{userId}")
    public ResponseEntity<?> getHouseLocation(@PathVariable UUID userId) {
        Optional<User> user = userHomeService.findHomeByUserId(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(new Location(user.get().getHome().getLatitude(), user.get().getHome().getLongitude()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO.builder().message("Wrong house").status(HttpStatus.BAD_REQUEST).build());
        }
    }

    @GetMapping("/home")
    public ResponseEntity<?> getHome(@AuthenticationPrincipal Jwt principal) {
        Optional<User> user = userHomeService.findHomeByUserId(UUID.fromString(principal.getSubject()));
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get().getHome());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorDTO.builder().message("Wrong house").status(HttpStatus.BAD_REQUEST).build());
        }
    }

    @PostMapping("/setHouseTemperature")
    public ResponseEntity<?> setHouseTemperature(@AuthenticationPrincipal Jwt principal, @RequestParam int houseTemperature) {
        if (houseTemperature > 30 || houseTemperature < 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorDTO.builder().message("Temperature must be between 0 and 30 degrees").status(HttpStatus.BAD_REQUEST).build());
        }
        Optional<User> user = userHomeService.findHomeByUserId(UUID.fromString(principal.getSubject()));
        if (user.isPresent()) {
            Home home = user.get().getHome();
            home.setPreferredTemp(houseTemperature);
            homeService.saveHome(home);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorDTO.builder().message("House not found").status(HttpStatus.BAD_REQUEST).build());
        }
    }

    @PostMapping("/setHouseHumidity")
    public ResponseEntity<?> setHouseHumidity(@AuthenticationPrincipal Jwt principal, @RequestParam int houseHumidity) {
        if (houseHumidity > 70 || houseHumidity < 30) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorDTO.builder().message("Humidity must be between 30 and 70 percent").status(HttpStatus.BAD_REQUEST).build());
        }
        Optional<User> user = userHomeService.findHomeByUserId(UUID.fromString(principal.getSubject()));
        if (user.isPresent()) {
            Home home = user.get().getHome();
            home.setPreferredHum(houseHumidity);
            homeService.saveHome(home);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorDTO.builder().message("House not found").status(HttpStatus.BAD_REQUEST).build());
        }
    }

    @PostMapping("/editAddress")
    public ResponseEntity<?> editAddress(@AuthenticationPrincipal Jwt principal, @RequestBody Home home) {
        Optional<User> user = userHomeService.findHomeByUserId(UUID.fromString(principal.getSubject()));
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorDTO.builder().message("House not found").status(HttpStatus.BAD_REQUEST).build());
        }
        if(homeService.editAddress(user.get().getHome().getId(), home))
            return ResponseEntity.ok().build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorDTO.builder().message("Wrong location").status(HttpStatus.BAD_REQUEST).build());
    }
}

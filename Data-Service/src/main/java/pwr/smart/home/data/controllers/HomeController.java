package pwr.smart.home.data.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;
import pwr.smart.home.data.model.Location;
import pwr.smart.home.data.dao.User;
import pwr.smart.home.data.service.HomeService;
import pwr.smart.home.data.service.UserService;

import java.util.Optional;

@RestControllerWithBasePath
public class HomeController {
    @Autowired
    private HomeService homeService;

    @Autowired
    private UserService userHomeService;

    @GetMapping("/latlong/{userId}")
    public ResponseEntity<?> getHouseLocation(@PathVariable String userId) {
        Optional<User> user = userHomeService.findHomeByUserId(userId);
        if (user.isPresent()) {
            return ResponseEntity.ok(new Location(user.get().getHome().getLatitude(), user.get().getHome().getLongitude()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong house");
        }
    }
}

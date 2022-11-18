package pwr.smart.home.data.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;
import pwr.smart.home.data.dao.Home;
import pwr.smart.home.data.model.Location;
import pwr.smart.home.data.service.HomeService;

import java.util.Optional;

@RestControllerWithBasePath
public class HomeController {
    @Autowired
    private HomeService homeService;

    @GetMapping("/latlong/{house_id}")
    public ResponseEntity<?> getHouseLocation(@PathVariable Long house_id) {
        Optional<Home> home = homeService.getHomeLocation(house_id);
        if (home.isPresent()) {
            return ResponseEntity.ok(new Location(home.get().getLatitude(), home.get().getLongitude()));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong house");
        }
    }
}

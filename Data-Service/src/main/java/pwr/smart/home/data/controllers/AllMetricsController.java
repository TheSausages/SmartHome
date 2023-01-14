package pwr.smart.home.data.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;
import pwr.smart.home.common.error.ErrorDTO;
import pwr.smart.home.data.dao.Home;
import pwr.smart.home.data.dao.Sensor;
import pwr.smart.home.data.dao.User;
import pwr.smart.home.data.model.Location;
import pwr.smart.home.data.service.HomeService;
import pwr.smart.home.data.service.SensorService;
import pwr.smart.home.data.service.UserService;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class AllMetricsController {
    private MetricsEndpoint metricsEndpoint;

    @Autowired(required = false)
    AllMetricsController(MetricsEndpoint metricsEndpoint) {
        this.metricsEndpoint = metricsEndpoint;
    }

    @GetMapping("/actuator/all")
    public ResponseEntity<?> getAllAtOnce() {
         var a = metricsEndpoint.listNames().getNames()
                 .stream().map(metric -> metricsEndpoint.metric(metric, List.of())).collect(Collectors.toList());

         return ResponseEntity.ok(a);
    }
}

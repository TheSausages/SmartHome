package pwr.smart.home.control.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;
import pwr.smart.home.control.model.Endpoint;
import pwr.smart.home.control.service.DataEmitter;

@RestControllerWithBasePath
public class Actions {

    @Autowired
    private DataEmitter dataEmitter;

    @Autowired
    private Endpoint endpoint;

    @PostMapping("/temperature")
    public ResponseEntity<?> setTargetTemperature(@RequestParam String target) {
        return ResponseEntity.ok(dataEmitter.callForAction(target, endpoint.getAirConditionerUrl()));
    }

    @PostMapping("/air-quality")
    public ResponseEntity<?> setTargetAirQuality(@RequestParam String target) {
        return ResponseEntity.ok(dataEmitter.callForAction(target, endpoint.getAirFilterUrl()));
    }

    @PostMapping("/humidity")
    public ResponseEntity<?> setTargetHumidity(@RequestParam String target) {
        return ResponseEntity.ok(dataEmitter.callForAction(target, endpoint.getAirHumidifierUrl()));
    }
}

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
    DataEmitter dataEmitter;

    @Autowired
    Endpoint endpoint;

    @PostMapping("/temperature")
    public ResponseEntity<?> setTargetTemperature(@RequestParam String target) {
        return ResponseEntity.ok(dataEmitter.callForAction(target, endpoint.getAirConditionerUrl("HIBWCDUIYHWASDAE")));
    }

    @PostMapping("/air-quality")
    public ResponseEntity<?> setTargetAirQuality(@RequestParam String target) {
        return ResponseEntity.ok(dataEmitter.callForAction(target, endpoint.getAirFilterUrl("HIBWCDUIYHWASDAD")));
    }

    @PostMapping("/humidity")
    public ResponseEntity<?> setTargetHumidity(@RequestParam String target) {
        return ResponseEntity.ok(dataEmitter.callForAction(target, endpoint.getAirHumidifierUrl("HIBWCDUIYHWASDAF")));
    }
}

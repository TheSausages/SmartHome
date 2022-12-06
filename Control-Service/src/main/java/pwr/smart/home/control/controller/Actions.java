package pwr.smart.home.control.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;
import pwr.smart.home.control.model.Endpoint;
import pwr.smart.home.control.service.ActionsService;
import pwr.smart.home.control.service.DataEmitter;

import java.util.concurrent.ExecutionException;

@RestControllerWithBasePath
public class Actions {

    @Autowired
    private ActionsService actionsService;

    @PostMapping("/temperature")
    public ResponseEntity<?> setTargetTemperature(@RequestParam String target) throws ExecutionException, InterruptedException {
        String serial = "HIBWCDUIYHWASDAE";

        return ResponseEntity.ok(actionsService.doActionsForDeviceWithSerialNumber(target, serial));
    }

    @PostMapping("/air-quality")
    public ResponseEntity<?> setTargetAirQuality(@RequestParam String target) throws ExecutionException, InterruptedException {
        String serial = "HIBWCDUIYHWASDAD";

        return ResponseEntity.ok(actionsService.doActionsForDeviceWithSerialNumber(target, serial));
    }

    @PostMapping("/humidity")
    public ResponseEntity<?> setTargetHumidity(@RequestParam String target) throws ExecutionException, InterruptedException {
        String serial = "HIBWCDUIYHWASDAF";

        return ResponseEntity.ok(actionsService.doActionsForDeviceWithSerialNumber(target, serial));
    }
}

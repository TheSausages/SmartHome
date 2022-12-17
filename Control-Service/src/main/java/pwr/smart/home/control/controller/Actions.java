package pwr.smart.home.control.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;
import pwr.smart.home.common.model.enums.DeviceType;
import pwr.smart.home.control.service.ActionsService;

import java.util.concurrent.ExecutionException;

@RestControllerWithBasePath
public class Actions {

    @Autowired
    private ActionsService actionsService;

    @GetMapping("/temperature")
    public ResponseEntity<?> setTargetTemperature(@AuthenticationPrincipal Jwt principal, @RequestParam String target) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(actionsService.doActionsForDeviceWithSerialNumber(target, DeviceType.AIR_CONDITIONER, principal));
    }

    @GetMapping("/air-quality")
    public ResponseEntity<?> setTargetAirQuality(@AuthenticationPrincipal Jwt principal, @RequestParam String target) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(actionsService.doActionsForDeviceWithSerialNumber(target, DeviceType.AIR_FILTER, principal));
    }

    @GetMapping("/humidity")
    public ResponseEntity<?> setTargetHumidity(@AuthenticationPrincipal Jwt principal, @RequestParam String target) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(actionsService.doActionsForDeviceWithSerialNumber(target, DeviceType.AIR_HUMIDIFIER, principal));
    }


    @GetMapping("/{serial}/activate")
    public ResponseEntity<Boolean> tryToActivate(@PathVariable(name = "serial") String serialNumber) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(actionsService.tryToActivate(serialNumber));
    }

    @GetMapping("/{serial}/deactivate")
    public ResponseEntity<Boolean> tryToDeactivate(@PathVariable(name = "serial") String serialNumber) throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(actionsService.tryToDeactivate(serialNumber));
    }
}

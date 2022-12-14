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
import org.springframework.web.bind.annotation.PathVariable;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;
import pwr.smart.home.common.error.ErrorDTO;
import pwr.smart.home.data.dao.FunctionalDevice;
import pwr.smart.home.data.dao.User;
import pwr.smart.home.data.service.FunctionalDeviceService;
import pwr.smart.home.data.service.UserService;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestControllerWithBasePath
public class FunctionalDeviceController {
    @Autowired
    private UserService userHomeService;
    @Autowired
    private FunctionalDeviceService functionalDeviceService;

    @GetMapping("/homeFunctionalDevices")
    public ResponseEntity<?> getHomeFunctionalDevices(@AuthenticationPrincipal Jwt principal) {
        Optional<User> user = userHomeService.findHomeByUserId(UUID.fromString(principal.getSubject()));
        if (user.isPresent()) {
            List<FunctionalDevice> functionalDevices = functionalDeviceService.findAllHomeFunctionalDevices(user.get().getHome());
            return ResponseEntity.ok(functionalDevices);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorDTO.builder().message("Wrong house").status(HttpStatus.BAD_REQUEST).build());
        }
    }

    @GetMapping("/homeFunctionalDevices/{homeId}")
    public ResponseEntity<?> getHomeFunctionalDevicesWithMeasurements(@PathVariable(name = "homeId") long homeId) {
        return ResponseEntity.ok(functionalDeviceService.getFunctionalDevicesWithMeasurementsForHome(
                homeId
        ));
    }

    @PostMapping("/addFunctionalDevice")
    public ResponseEntity<?> addNewHomeFunctionalDevice(@AuthenticationPrincipal Jwt principal, @RequestBody FunctionalDevice functionalDevice) {
        Optional<User> user = userHomeService.findHomeByUserId(UUID.fromString(principal.getSubject()));
        if (!checkIfFieldsAreNotEmpty(functionalDevice)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorDTO.builder().message("Fill the form correctly").status(HttpStatus.BAD_REQUEST).build());
        }
        if (user.isPresent()) {
            functionalDevice.setHomeId(user.get().getHome().getId());
            functionalDevice.setCreatedAt(new Date(System.currentTimeMillis()));
            functionalDeviceService.saveFunctionalDevice(functionalDevice);
            return ResponseEntity.ok(functionalDevice);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorDTO.builder().message("Wrong house").status(HttpStatus.UNAUTHORIZED).build());
        }
    }

    @PostMapping("/updateFunctionalDevice")
    public ResponseEntity<?> updateHomeFunctionalDevice(@AuthenticationPrincipal Jwt principal, @RequestBody FunctionalDevice functionalDevice) {
        Optional<User> user = userHomeService.findHomeByUserId(UUID.fromString(principal.getSubject()));
        if (!checkIfFieldsAreNotEmpty(functionalDevice)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ErrorDTO.builder().message("Fill the form correctly").status(HttpStatus.BAD_REQUEST).build());
        }
        if (user.isPresent()) {
            functionalDeviceService.saveFunctionalDevice(functionalDevice);

            return ResponseEntity.ok(functionalDevice);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ErrorDTO.builder().message("Wrong house").status(HttpStatus.UNAUTHORIZED).build());
        }
    }

    @GetMapping("/inactive/{serialNumber}")
    public ResponseEntity<?> markFunctionalDeviceAsInactive(@PathVariable(name = "serialNumber") String serialNumber) {
        functionalDeviceService.markDeviceAsInactive(serialNumber);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private boolean checkIfFieldsAreNotEmpty(FunctionalDevice functionalDevice) {
        return StringUtils.hasText(functionalDevice.getName()) &&
                StringUtils.hasText(functionalDevice.getManufacturer()) &&
                StringUtils.hasText(functionalDevice.getSerialNumber());
    }
}
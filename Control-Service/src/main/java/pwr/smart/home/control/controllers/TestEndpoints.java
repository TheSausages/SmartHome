package pwr.smart.home.control.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;

@RestControllerWithBasePath
public class TestEndpoints {
    @GetMapping("/test")
    public String testEndpoint() {
        return "Test Message";
    }
}

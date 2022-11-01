package pwr.smart.home.data.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;
import pwr.smart.home.data.model.FilterData;

@RestControllerWithBasePath
public class TestEndpoints {
    private static final Logger logger = LoggerFactory.getLogger(TestEndpoints.class);

    @PostMapping("/filter")
    public ResponseEntity<?> getFilterInfo(@RequestBody FilterData filterData) {
        logger.info(filterData.toString());
        return ResponseEntity.ok().build();
    }
}

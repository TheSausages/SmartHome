package pwr.smart.home.data.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import pwr.smart.home.common.controllers.RestControllerWithBasePath;
import pwr.smart.home.data.dao.Measurement;
import pwr.smart.home.data.model.AirFilterData;
import pwr.smart.home.data.service.AirFilterDataService;

import java.util.List;

@RestControllerWithBasePath
public class AirFilterController {
    private static final Logger logger = LoggerFactory.getLogger(AirFilterController.class);

    @Autowired
    AirFilterDataService airFilterDataService;

    @PostMapping("/filter")
    public ResponseEntity<?> getFilterMeasurements(@RequestBody AirFilterData filterData) {
        logger.info(filterData.toString());
        airFilterDataService.addAirFilterMeasurements(filterData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/lastAirFilterMeasurements")
    @ResponseBody
    public List<Measurement> getLastAirFilterMeasurements(String sensorSerialNumber) {
        return airFilterDataService.getLastAirFilterMeasurements(sensorSerialNumber);
    }
}

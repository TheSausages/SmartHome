package pwr.smart.home.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.smart.home.data.dao.Measurement;
import pwr.smart.home.data.dao.Sensor;
import pwr.smart.home.data.model.AirFilterData;
import pwr.smart.home.common.model.enums.MeasurementType;
import pwr.smart.home.data.repository.MeasurementRepository;
import pwr.smart.home.data.repository.SensorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AirFilterService {
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private MeasurementService measurementService;
    @Autowired
    private SensorRepository sensorRepository;

    public void addAirFilterMeasurements(AirFilterData airFilterData) {
        Optional<Sensor> sensor = sensorRepository.findBySerialNumber(airFilterData.getSerialNumber());
        if (sensor.isPresent()) {
            measurementService.saveMeasurement(new Measurement(MeasurementType.IAI, airFilterData.getIAI(), sensor.get().getId(), airFilterData.getTimestamp()));
            measurementService.saveMeasurement(new Measurement(MeasurementType.PM25, airFilterData.getPM25(), sensor.get().getId(), airFilterData.getTimestamp()));
            measurementService.saveMeasurement(new Measurement(MeasurementType.GAS, airFilterData.getGas(), sensor.get().getId(), airFilterData.getTimestamp()));
        }
    }

    public List<Measurement> getLastAirFilterMeasurements(String sensorSerialNumber) {
        Optional<Sensor> sensor = sensorRepository.findBySerialNumber(sensorSerialNumber);
        return sensor
                .map(value -> measurementRepository.findTop3BySensorIdOrderByCreatedAtDesc(value.getId()))
                .orElse(new ArrayList<>());
    }
}

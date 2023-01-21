package pwr.smart.home.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.smart.home.data.dao.Measurement;
import pwr.smart.home.data.dao.Sensor;
import pwr.smart.home.data.model.AirHumidifierData;
import pwr.smart.home.common.model.enums.MeasurementType;
import pwr.smart.home.data.model.MeasurementQueue;
import pwr.smart.home.data.repository.MeasurementRepository;
import pwr.smart.home.data.repository.SensorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AirHumidifierService {
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private MeasurementService measurementService;
    @Autowired
    private SensorRepository sensorRepository;

    public void addAirHumidifierMeasurements(AirHumidifierData airHumidifierData) {
        measurementService.saveMeasurement(new MeasurementQueue(airHumidifierData.getSerialNumber(), MeasurementType.HUMIDITY, airHumidifierData.getHumidity(), airHumidifierData.getTimestamp()));
    }

    public Measurement getLastAirHumidifierMeasurement(String sensorSerialNumber) {
        Optional<Sensor> sensor = sensorRepository.findBySerialNumber(sensorSerialNumber);
        return sensor
                .map(value -> measurementRepository.findTopBySensorIdOrderByCreatedAtDesc(value.getId()))
                .orElse(null);
    }
}

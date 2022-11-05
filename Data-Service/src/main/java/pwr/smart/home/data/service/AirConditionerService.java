package pwr.smart.home.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.smart.home.data.dao.Measurement;
import pwr.smart.home.data.dao.Sensor;
import pwr.smart.home.data.model.AirConditionerData;
import pwr.smart.home.data.model.enums.MeasurementType;
import pwr.smart.home.data.repository.MeasurementRepository;
import pwr.smart.home.data.repository.SensorRepository;

import java.util.Optional;

@Service
public class AirConditionerService {
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private SensorRepository sensorRepository;

    public void addAirConditionerMeasurements(AirConditionerData airConditionerData) {
        Optional<Sensor> sensor = sensorRepository.findBySerialNumber(airConditionerData.getSerialNumber());
        sensor.ifPresent(value ->
                measurementRepository.save(
                        new Measurement(
                                MeasurementType.CELSIUS,
                                airConditionerData.getTemperature(),
                                value.getId(),
                                airConditionerData.getTimestamp()
                        )
                )
        );
    }

    public Measurement getLastAirConditionerMeasurement(String sensorSerialNumber) {
        Optional<Sensor> sensor = sensorRepository.findBySerialNumber(sensorSerialNumber);
        return sensor
                .map(value -> measurementRepository.findTopBySensorIdOrderByCreatedAtDesc(value.getId()))
                .orElse(null);
    }
}

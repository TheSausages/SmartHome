package pwr.smart.home.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.smart.home.data.dao.Measurement;
import pwr.smart.home.data.dao.Sensor;
import pwr.smart.home.data.model.AirFilterData;
import pwr.smart.home.common.model.enums.MeasurementType;
import pwr.smart.home.data.model.MeasurementQueue;
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
        measurementService.saveMeasurement(new MeasurementQueue(airFilterData.getSerialNumber(), MeasurementType.IAI, airFilterData.getIAI(), airFilterData.getTimestamp()));
        measurementService.saveMeasurement(new MeasurementQueue(airFilterData.getSerialNumber(), MeasurementType.PM25, airFilterData.getPM25(), airFilterData.getTimestamp()));
        measurementService.saveMeasurement(new MeasurementQueue(airFilterData.getSerialNumber(), MeasurementType.GAS, airFilterData.getGas(), airFilterData.getTimestamp()));
    }

    public List<Measurement> getLastAirFilterMeasurements(String sensorSerialNumber) {
        Optional<Sensor> sensor = sensorRepository.findBySerialNumber(sensorSerialNumber);
        return sensor
                .map(value -> measurementRepository.findTop3BySensorIdOrderByCreatedAtDesc(value.getId()))
                .orElse(new ArrayList<>());
    }
}

package pwr.smart.home.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.smart.home.data.dao.Measurement;
import pwr.smart.home.data.dao.Sensor;
import pwr.smart.home.data.model.AirFilterData;
import pwr.smart.home.data.model.enums.MeasurementType;
import pwr.smart.home.data.repository.MeasurementRepository;
import pwr.smart.home.data.repository.SensorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AirFilterDataService {
    @Autowired
    private MeasurementRepository measurementRepository;
    @Autowired
    private SensorRepository sensorRepository;

    public void addAirFilterMeasurements(AirFilterData airFilterData) {
        Optional<Sensor> sensor = sensorRepository.findBySerialNumber(airFilterData.getSerialNumber());
        if (sensor.isPresent()) {
            List<Measurement> measurementList = new ArrayList<>();
            measurementList.add(new Measurement(MeasurementType.IAI, airFilterData.getIAI(), sensor.get().getId(), airFilterData.getTimestamp()));
            measurementList.add(new Measurement(MeasurementType.PM25, airFilterData.getPM25(), sensor.get().getId(), airFilterData.getTimestamp()));
            measurementList.add(new Measurement(MeasurementType.GAS, airFilterData.getGas(), sensor.get().getId(), airFilterData.getTimestamp()));
            measurementRepository.saveAll(measurementList);
        }
    }
}

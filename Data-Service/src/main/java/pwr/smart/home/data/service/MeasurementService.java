package pwr.smart.home.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pwr.smart.home.data.dao.Measurement;
import pwr.smart.home.data.dao.Sensor;
import pwr.smart.home.data.model.enums.SensorType;
import pwr.smart.home.data.repository.MeasurementRepository;
import pwr.smart.home.data.repository.SensorRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MeasurementService {

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private SensorRepository sensorRepository;

    public boolean isSensorCompatibleType(String serialNumber, SensorType sensorType) {
        Optional<Sensor> sensor = sensorRepository.findBySerialNumber(serialNumber);
        return sensor.filter(value -> value.getType() == sensorType).isPresent();
    }

    public List<Measurement> getAllMeasurements(String sensorSerialNumber, Pageable pageableSetting) {
        Optional<Sensor> sensor = sensorRepository.findBySerialNumber(sensorSerialNumber);

        if (sensor.isPresent()) {
            Long sensorId = sensor.get().getId();
            return measurementRepository.findAllBySensorId(sensorId, pageableSetting);
        } else return null;
    }
}

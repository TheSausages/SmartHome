package pwr.smart.home.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.smart.home.data.dao.Home;
import pwr.smart.home.data.dao.Sensor;
import pwr.smart.home.data.repository.SensorRepository;

import java.util.Optional;

@Service
public class SensorService {
    @Autowired
    private SensorRepository sensorRepository;

    public boolean isSensorInHome(String serialNumber, Home home) {
        Optional<Sensor> sensor = sensorRepository.findBySerialNumber(serialNumber);
        return sensor.map(value -> value.getHome().equals(home)).orElse(false);
    }
}

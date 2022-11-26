package pwr.smart.home.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.smart.home.data.dao.Home;
import pwr.smart.home.data.dao.Sensor;
import pwr.smart.home.data.repository.SensorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class SensorService {
    @Autowired
    private SensorRepository sensorRepository;

    public boolean isSensorInHome(String serialNumber, Home home) {
        Optional<Sensor> sensor = sensorRepository.findBySerialNumber(serialNumber);
        return sensor.map(value -> Objects.equals(value.getHomeId(), home.getId())).orElse(false);
    }

    public List<Sensor> findAllHomeSensors(Home home) {
        Optional<List<Sensor>> sensor = sensorRepository.findSensorsByHomeId(home.getId());
        return sensor.orElse(new ArrayList<>());
    }
}

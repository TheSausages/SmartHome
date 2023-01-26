package pwr.smart.home.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.smart.home.data.dao.Sensor;

import java.util.List;
import java.util.Optional;

public interface SensorRepository extends JpaRepository<Sensor, Long> {
    Optional<Sensor> findBySerialNumber(String serialNumber);
    Optional<List<Sensor>> findSensorsByHomeId(Long homeId);

    List<Sensor> findAllBySerialNumberIn(List<String> serialNumbers);
}

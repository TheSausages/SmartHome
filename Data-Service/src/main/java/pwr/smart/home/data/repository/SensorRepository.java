package pwr.smart.home.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.smart.home.data.dao.SensorDao;

import java.util.Optional;

public interface SensorRepository extends JpaRepository<SensorDao, Long> {
    Optional<SensorDao> findBySerialNumber(String serialNumber);
}

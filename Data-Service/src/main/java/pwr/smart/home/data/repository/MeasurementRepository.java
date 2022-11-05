package pwr.smart.home.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.smart.home.data.dao.Measurement;

import java.util.List;
import java.util.Optional;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findTop3BySensorIdOrderByCreatedAtDesc(Long sensorId);
    Measurement findTopBySensorIdOrderByCreatedAtDesc(Long sensorId);
}

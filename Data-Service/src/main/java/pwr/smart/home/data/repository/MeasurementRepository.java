package pwr.smart.home.data.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import pwr.smart.home.data.dao.Measurement;

import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findTop3BySensorIdOrderByCreatedAtDesc(Long sensorId);
    Measurement findTopBySensorIdOrderByCreatedAtDesc(Long sensorId);

    List<Measurement> findAllBySensorId(Long id, Pageable pageable);
}

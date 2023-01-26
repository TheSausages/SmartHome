package pwr.smart.home.data.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pwr.smart.home.common.model.enums.MeasurementType;
import pwr.smart.home.data.dao.Measurement;
import pwr.smart.home.data.dao.Measurement_Aggregated;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface AggregatedMeasurementRepository extends JpaRepository<Measurement_Aggregated, Long> {
    Optional<Measurement_Aggregated> findTopByOrderByCreatedAtDesc();

    List<Measurement_Aggregated> findAllBySensorIdAndCreatedAtIsBetween(Long id, Timestamp from, Timestamp to);
}

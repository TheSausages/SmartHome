package pwr.smart.home.data.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pwr.smart.home.data.dao.Measurement;
import pwr.smart.home.common.model.enums.MeasurementType;
import pwr.smart.home.data.dao.Measurement_Aggregated;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findTop3BySensorIdOrderByCreatedAtDesc(Long sensorId);
    Measurement findTopBySensorIdOrderByCreatedAtDesc(Long sensorId);

    List<Measurement> findAllBySensorId(Long id, Pageable pageable);
    List<Measurement> findAllBySensorIdAndCreatedAtIsBetween(Long id, Timestamp from, Timestamp to);

    List<Measurement> findTop20ByTypeIsOrderByCreatedAtDesc(MeasurementType type);

    Optional<Measurement> findTopByOrderByCreatedAtAsc();

    @Query("select ms from Measurement ms " +
            "where ms.createdAt > :startTimestamp and ms.createdAt < :endTimestamp order by ms.createdAt")
    List<Measurement> getAllForDateBetweenGroupedBySensor(Timestamp startTimestamp, Timestamp endTimestamp);

    default List<Measurement> getAllForDayGroupedBySensor(Timestamp timestamp) {
        return getAllForDateBetweenGroupedBySensor(
                Timestamp.valueOf(timestamp.toLocalDateTime().toLocalDate().atStartOfDay()),
                Timestamp.valueOf(timestamp.toLocalDateTime().toLocalDate().plus(1, ChronoUnit.DAYS).atStartOfDay())
        );
    }
}

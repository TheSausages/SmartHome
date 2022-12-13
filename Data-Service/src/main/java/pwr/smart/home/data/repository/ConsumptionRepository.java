package pwr.smart.home.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pwr.smart.home.data.dao.Consumption;

import java.sql.Timestamp;
import java.util.List;

public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {

    @Query("select c from Consumption c where created_at >= :yesterdayTimestamp and device_serial_number = :deviceSerialNumber")
    List<Consumption> findFromLast24hBySerialNumber(@Param("yesterdayTimestamp") Timestamp yesterdayTimestamp, @Param("deviceSerialNumber") String deviceSerialNumber);
}

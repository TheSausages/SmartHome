package pwr.smart.home.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.smart.home.data.dao.MeasurementDao;

public interface MeasurementRepository extends JpaRepository<MeasurementDao, Long> {
}

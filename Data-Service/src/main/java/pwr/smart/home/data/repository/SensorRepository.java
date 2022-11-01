package pwr.smart.home.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.smart.home.data.dao.SensorDao;

public interface SensorRepository extends JpaRepository<SensorDao, Long> {
}

package pwr.smart.home.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.smart.home.data.dao.Consumption;

public interface ConsumptionRepository extends JpaRepository<Consumption, Long> {
}

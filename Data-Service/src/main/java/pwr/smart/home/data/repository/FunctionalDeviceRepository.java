package pwr.smart.home.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.smart.home.data.dao.FunctionalDevice;

import java.util.List;
import java.util.Optional;

public interface FunctionalDeviceRepository extends JpaRepository<FunctionalDevice, Long> {
    Optional<FunctionalDevice> findBySerialNumber(String serialNumber);
    Optional<List<FunctionalDevice>> findFunctionalDevicesByHomeId(Long homeId);
}

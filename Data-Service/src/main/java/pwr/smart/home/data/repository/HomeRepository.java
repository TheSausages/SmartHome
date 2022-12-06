package pwr.smart.home.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pwr.smart.home.data.dao.Home;

import java.util.List;
import java.util.Optional;

public interface HomeRepository extends JpaRepository<Home, Long> {
    @Query(value = "select * from home h where h.id in (select distinct fd.home_id from functional_device fd)", nativeQuery = true)
    List<Home> findAllHomesWithFunctionalDevices();

    @Query(value = "select * from home h where h.id = (select fd.home_id from functional_device fd where fd.serial_number = :serialNumber)", nativeQuery = true)
    Optional<Home> findHomeBySerialNumberId(@Param("serialNumber") String serialNumber);
}

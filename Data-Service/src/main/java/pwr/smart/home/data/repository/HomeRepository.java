package pwr.smart.home.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pwr.smart.home.data.dao.Home;

import java.util.List;

public interface HomeRepository extends JpaRepository<Home, Long> {
    @Query(value = "select * from home h where h.id in (select distinct fd.home_id from functional_device fd)", nativeQuery = true)
    List<Home> findAllHomesWithFunctionalDevices();
}

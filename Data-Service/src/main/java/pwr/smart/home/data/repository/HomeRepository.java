package pwr.smart.home.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.smart.home.data.dao.Home;

public interface HomeRepository extends JpaRepository<Home, Long> {

}

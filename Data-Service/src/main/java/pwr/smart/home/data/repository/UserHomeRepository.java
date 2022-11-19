package pwr.smart.home.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.smart.home.data.dao.User;

import java.util.Optional;

public interface UserHomeRepository extends JpaRepository<User, String> {
    Optional<User> findUserByUserId(String s);
}

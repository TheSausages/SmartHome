package pwr.smart.home.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pwr.smart.home.data.dao.User;

import java.util.Optional;
import java.util.UUID;

public interface UserHomeRepository extends JpaRepository<User, String> {
    Optional<User> findUserByUserId(UUID s);
}

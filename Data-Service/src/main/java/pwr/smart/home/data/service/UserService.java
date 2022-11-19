package pwr.smart.home.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.smart.home.data.dao.User;
import pwr.smart.home.data.repository.UserHomeRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserHomeRepository userHomeRepository;

    public Optional<User> findHomeByUserId(UUID userId) {
        return userHomeRepository.findUserByUserId(userId);
    }
}

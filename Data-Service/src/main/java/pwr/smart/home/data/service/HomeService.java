package pwr.smart.home.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.smart.home.data.dao.Home;
import pwr.smart.home.data.repository.HomeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class HomeService {
    @Autowired
    private HomeRepository homeRepository;

    public Optional<Home> getHomeLocation(Long id) {
        return homeRepository.findById(id);
    }

    public Home saveHome(Home home) {
        return homeRepository.save(home);
    }

    public List<Home> findAllHomesWithActiveFunctionalDevices() {
        return homeRepository.findAllHomesWithFunctionalDevices();
    }
}

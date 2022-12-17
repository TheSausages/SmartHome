package pwr.smart.home.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.smart.home.data.dao.Home;
import pwr.smart.home.data.model.Location;
import pwr.smart.home.data.repository.HomeRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public boolean editAddress(Long houseId, Home home) {
        Optional<Home> homeOptEntity = getHomeLocation(houseId);
        if(homeOptEntity.isEmpty()) {
            throw new RuntimeException("Home should be in DB");
        }
        Home homeEntity = homeOptEntity.get();
        homeEntity.setCity(home.getCity());
        homeEntity.setCountry(home.getCountry());
        homeEntity.setPostCode(home.getPostCode());
        homeEntity.setName(home.getName());
        homeEntity.setStreet(home.getStreet());
        Location location = AddressConverter.convertToLatLong(home.getPostCode());
        if(location == null)
            return false;
        homeEntity.setLatitude(location.getLatitude());
        homeEntity.setLongitude(location.getLongitude());
        saveHome(homeEntity);
        return true;
    }

    public Set<Integer> addHour(Long houseId, int hour) {
        Optional<Home> homeOptEntity = getHomeLocation(houseId);
        if(homeOptEntity.isEmpty()) {
            throw new RuntimeException("Home should be in DB");
        }
        Home homeEntity = homeOptEntity.get();
        Set<Integer> hoursParsed = homeEntity.getHours();
        hoursParsed.add(hour);
        persistNewHoursInHome(hoursParsed, homeEntity);
        return hoursParsed;
    }

    public Set<Integer> removeHour(Long houseId, int hour) {
        Optional<Home> homeOptEntity = getHomeLocation(houseId);
        if(homeOptEntity.isEmpty()) {
            throw new RuntimeException("Home should be in DB");
        }
        Home homeEntity = homeOptEntity.get();
        Set<Integer> hoursParsed = homeEntity.getHours();
        hoursParsed.remove(hour);
        persistNewHoursInHome(hoursParsed, homeEntity);
        return hoursParsed;
    }

    private void persistNewHoursInHome(Set<Integer> hoursParsed, Home homeEntity) {
        StringBuilder addToDbStringBuilder = new StringBuilder();
        hoursParsed.forEach(hourInSet -> addToDbStringBuilder.append(hourInSet).append(";"));
        homeEntity.setHours(addToDbStringBuilder.toString());
        saveHome(homeEntity);
    }
}

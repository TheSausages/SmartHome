package pwr.smart.home.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.smart.home.data.dao.Home;
import pwr.smart.home.data.model.Location;
import pwr.smart.home.data.repository.HomeRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    public String addHour(Long houseId, int hour) {
        Optional<Home> homeOptEntity = getHomeLocation(houseId);
        if(homeOptEntity.isEmpty()) {
            throw new RuntimeException("Home should be in DB");
        }
        Home homeEntity = homeOptEntity.get();
        Set<Integer> hoursParsed = extractSetOfHours(homeEntity);
        hoursParsed.add(hour);
        StringBuilder addToDbStringBuilder = persistNewHoursInHome(hoursParsed, homeEntity);
        return addToDbStringBuilder.toString();
    }

    public String removeHour(Long houseId, int hour) {
        Optional<Home> homeOptEntity = getHomeLocation(houseId);
        if(homeOptEntity.isEmpty()) {
            throw new RuntimeException("Home should be in DB");
        }
        Home homeEntity = homeOptEntity.get();
        Set<Integer> hoursParsed = extractSetOfHours(homeEntity);
        hoursParsed.remove(hour);
        StringBuilder addToDbStringBuilder = persistNewHoursInHome(hoursParsed, homeEntity);
        return addToDbStringBuilder.toString();
    }

    private StringBuilder persistNewHoursInHome(Set<Integer> hoursParsed, Home homeEntity) {
        StringBuilder addToDbStringBuilder = new StringBuilder();
        hoursParsed.forEach(hourInSet -> addToDbStringBuilder.append(hourInSet).append(";"));
        homeEntity.setHours(addToDbStringBuilder.toString());
        saveHome(homeEntity);
        return addToDbStringBuilder;
    }

    private Set<Integer> extractSetOfHours(Home homeEntity) {
        String[] hoursString = homeEntity.getHours().split(";");
        if(hoursString.length == 1 && hoursString[0].isEmpty())
            return new HashSet<>();
        return Arrays.stream(hoursString).map(Integer::parseInt).collect(Collectors.toSet());
    }
}

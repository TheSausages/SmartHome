package pwr.smart.home.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.smart.home.data.dao.FunctionalDevice;
import pwr.smart.home.data.dao.Home;
import pwr.smart.home.data.repository.FunctionalDeviceRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FunctionalDeviceService {
    @Autowired
    private FunctionalDeviceRepository functionalDeviceRepository;

    public List<FunctionalDevice> findAllHomeFunctionalDevices(Home home) {
        Optional<List<FunctionalDevice>> functionalDevices = functionalDeviceRepository.findFunctionalDevicesByHomeId(home.getId());
        return functionalDevices.orElse(new ArrayList<>());
    }

    public void addNewFunctionalDevice(FunctionalDevice functionalDevice) {
        functionalDeviceRepository.save(functionalDevice);
    }
}

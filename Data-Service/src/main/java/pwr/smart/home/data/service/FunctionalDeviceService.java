package pwr.smart.home.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.smart.home.data.dao.Measurement;
import pwr.smart.home.data.model.FunctionalDeviceWithMeasurementsDTO;
import pwr.smart.home.data.dao.FunctionalDevice;
import pwr.smart.home.data.dao.Home;
import pwr.smart.home.common.model.enums.MeasurementType;
import pwr.smart.home.data.repository.FunctionalDeviceRepository;
import pwr.smart.home.data.repository.MeasurementRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FunctionalDeviceService {
    @Autowired
    private FunctionalDeviceRepository functionalDeviceRepository;
    @Autowired
    private MeasurementRepository measurementRepository;

    public List<FunctionalDevice> findAllHomeFunctionalDevices(Home home) {
        return findAllHomeFunctionalDevices(home.getId());
    }

    public List<FunctionalDevice> findAllHomeFunctionalDevices(long homeId) {
        Optional<List<FunctionalDevice>> functionalDevices = functionalDeviceRepository.findFunctionalDevicesByHomeId(homeId);
        return functionalDevices.orElse(new ArrayList<>());
    }

    public void addNewFunctionalDevice(FunctionalDevice functionalDevice) {
        functionalDeviceRepository.save(functionalDevice);
    }

    public void markDeviceAsInactive(String serialNumber) {
        Optional<FunctionalDevice> device = functionalDeviceRepository.findBySerialNumber(serialNumber);

        if (device.isPresent()) {
            FunctionalDevice device1 = device.get();
            device1.setConnected(false);

            functionalDeviceRepository.save(device1);
        }
    }

    public List<FunctionalDeviceWithMeasurementsDTO> getFunctionalDevicesWithMeasurementsForHome(long homeId) {
         return findAllHomeFunctionalDevices(homeId)
                .stream()
                .map(functionalDevice -> {
                    Map<MeasurementType, List<Measurement>> measurements = MeasurementType
                            .getMeasurementTypesForDeviceType(functionalDevice.getType())
                            .stream()
                            .collect(Collectors.toMap(
                                    it -> it,
                                    key -> measurementRepository.findTop12ByTypeIsOrderByCreatedAt(key)
                            ));

                    return new FunctionalDeviceWithMeasurementsDTO(
                            functionalDevice,
                            measurements
                    );
                })
                .collect(Collectors.toList());
    }
}

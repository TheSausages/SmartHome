package pwr.smart.home.data.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(FunctionalDeviceService.class);

    @Autowired
    private FunctionalDeviceRepository functionalDeviceRepository;
    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private ConsumptionService consumptionService;

    public List<FunctionalDevice> findAllHomeFunctionalDevices(Home home) {
        return findAllHomeFunctionalDevices(home.getId());
    }

    public List<FunctionalDevice> findAllHomeFunctionalDevices(long homeId) {
        Optional<List<FunctionalDevice>> functionalDevicesOptional = functionalDeviceRepository.findFunctionalDevicesByHomeId(homeId);

        if (functionalDevicesOptional.isPresent()) {
            List<FunctionalDevice> functionalDevices = functionalDevicesOptional.get();

            functionalDevices.forEach(functionalDevice -> {
                double averageConsumptionFromLast24h = consumptionService.findAverageConsumptionFromLast24h(functionalDevice.getSerialNumber());
                functionalDevice.setAverageConsumption(averageConsumptionFromLast24h);
            });
            return functionalDevices;
        } else {
            return new ArrayList<>();
        }

    }

    public void saveFunctionalDevice(FunctionalDevice functionalDevice) {
        functionalDeviceRepository.save(functionalDevice);
    }

    public void markDeviceAsInactive(String serialNumber) {
        Optional<FunctionalDevice> device = functionalDeviceRepository.findBySerialNumber(serialNumber);

        if (device.isPresent()) {
            FunctionalDevice device1 = device.get();
            device1.setActive(false);

            functionalDeviceRepository.save(device1);

            LOGGER.info("Device {} set to inactive", device1.getSerialNumber());
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

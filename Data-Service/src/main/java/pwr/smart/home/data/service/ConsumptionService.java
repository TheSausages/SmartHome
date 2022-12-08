package pwr.smart.home.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.smart.home.data.dao.Consumption;
import pwr.smart.home.data.dao.FunctionalDevice;
import pwr.smart.home.data.model.ConsumptionData;
import pwr.smart.home.data.repository.ConsumptionRepository;
import pwr.smart.home.data.repository.FunctionalDeviceRepository;

import java.util.Optional;

@Service
public class ConsumptionService {

    @Autowired
    FunctionalDeviceRepository functionalDeviceRepository;

    @Autowired
    ConsumptionRepository consumptionRepository;


    public void addConsumption(ConsumptionData consumptionData) {
        Optional<FunctionalDevice> functionalDevice = functionalDeviceRepository.findBySerialNumber(consumptionData.getDeviceSerialNumber());

        if (functionalDevice.isPresent()) {
            Consumption consumption = new Consumption();

            consumption.setCreatedAt(consumptionData.getTimestamp());
            consumption.setDeviceType(functionalDevice.get().getType());
            consumption.setDeviceSerialNumber(consumption.getDeviceSerialNumber());
            consumption.setConsumption(consumption.getConsumption());

            consumptionRepository.save(consumption);
        }


    }
}

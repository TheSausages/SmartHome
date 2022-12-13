package pwr.smart.home.data.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pwr.smart.home.data.dao.Consumption;
import pwr.smart.home.data.dao.FunctionalDevice;
import pwr.smart.home.data.model.ConsumptionData;
import pwr.smart.home.data.repository.ConsumptionRepository;
import pwr.smart.home.data.repository.FunctionalDeviceRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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
            consumption.setDeviceSerialNumber(consumptionData.getDeviceSerialNumber());
            consumption.setConsumption(consumptionData.getConsumption());

            consumptionRepository.save(consumption);
        }
    }

    public double findAverageConsumptionFromLast24h(String serialNumber){
        Instant instant = Instant.now().minus(24, ChronoUnit.HOURS);
        Timestamp timestamp = Timestamp.from(instant);

        List<Consumption> consumptions = consumptionRepository.findFromLast24hBySerialNumber(timestamp, serialNumber);
        AtomicReference<Double> sumOfConsumptions = new AtomicReference<>((double) 0);
        consumptions.forEach(consumption -> sumOfConsumptions.updateAndGet(v -> new Double((double) (v + consumption.getConsumption()))));

        return sumOfConsumptions.get()/24;
    }
}

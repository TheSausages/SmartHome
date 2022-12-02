package pwr.smart.home.control.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pwr.smart.home.control.model.FunctionalDeviceWithMeasurementsDTO;
import pwr.smart.home.control.model.Home;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class ControlJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControlJob.class);

    @Autowired
    DataService dataService;

    @Autowired
    FunDevicesAsyncMethods funDevicesAsyncMethods;

    /**
     * This will be ruin every 5 minutes
     */
    @Scheduled(cron = "* */5 * * * *")
    public void adjustForAllElements() throws ExecutionException, InterruptedException {
        List<Home> homes = dataService.getHomes();

        for (Home home : homes) {
            List<FunctionalDeviceWithMeasurementsDTO> devices = dataService.getFunctionalDevicesWithMeasurementsForHome(home);

            devices.forEach(device -> {
                switch (device.getDevice().getType()) {
                    case AIR_FILTER:
                        funDevicesAsyncMethods.handleFilter(device, home);
                        break;
                    case AIR_HUMIDIFIER:
                        funDevicesAsyncMethods.handleHumidity(device, home);
                        break;
                    case AIR_CONDITIONER:
                        funDevicesAsyncMethods.handleTemperature(device, home);
                        break;
                    default:
                        LOGGER.info("Device of unknown type found: {}", device.getDevice().getType());
                }
            });
        }
    }
}

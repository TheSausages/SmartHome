package pwr.smart.home.control.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pwr.smart.home.control.model.FunctionalDeviceWithMeasurementsDTO;
import pwr.smart.home.control.model.Home;
import pwr.smart.home.control.weather.model.response.AirQualityResponse;
import pwr.smart.home.control.weather.model.response.ForecastWeatherResponse;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class ControlJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControlJob.class);

    @Autowired
    DataService dataService;

    @Autowired
    FunDevicesAsyncMethods funDevicesAsyncMethods;

    @Autowired
    OpenMeteoAsyncMethods openMeteoAsyncMethods;

    /**
     * This will be ruin every 5 minutes
     */
    @Scheduled(cron = "* */5 * * * *")
    public void adjustForAllElements() throws ExecutionException, InterruptedException {
        List<Home> homes = dataService.getHomes();

        for (Home home : homes) {
            Future<List<FunctionalDeviceWithMeasurementsDTO>> devices = funDevicesAsyncMethods.getFunctionalDevicesWithMeasurementsForHome(home);
            Future<ForecastWeatherResponse> weather = openMeteoAsyncMethods.getWeatherForecast(home);
            Future<AirQualityResponse> air = openMeteoAsyncMethods.getAirData(home);

            LOGGER.info("For home {}", home.getId());

            for (FunctionalDeviceWithMeasurementsDTO device : devices.get()) {
                switch (device.getDevice().getType()) {
                    case AIR_FILTER:
                        funDevicesAsyncMethods.handleFilter(device, null, home, air.get());
                        break;
                    case AIR_HUMIDIFIER:
                        funDevicesAsyncMethods.handleHumidity(device, null, home, weather.get());
                        break;
                    case AIR_CONDITIONER:
                        funDevicesAsyncMethods.handleTemperature(device, null, home, weather.get());
                        break;
                    default:
                        LOGGER.info("Device of unknown type found: {}", device.getDevice().getType());
                }
            }
        }
    }
}
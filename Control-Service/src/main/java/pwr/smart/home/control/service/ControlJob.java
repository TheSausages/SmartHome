package pwr.smart.home.control.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pwr.smart.home.control.model.FunctionalDeviceWithMeasurementsDTO;
import pwr.smart.home.control.model.Home;
import pwr.smart.home.control.weather.model.response.ForecastWeatherResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class ControlJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControlJob.class);

    @Autowired
    DataService dataService;

    @Autowired
    AsyncMethods asyncMethods;

    /**
     * This will be ruin every 5 minutes
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void adjustForAllElements() throws ExecutionException, InterruptedException {
        List<Home> homes = dataService.getHomes();

        for (Home home : homes) {
            // Get functional devices info
            Future<List<FunctionalDeviceWithMeasurementsDTO>> info = asyncMethods.getHomeElements(home);

            System.out.println("3");

            // Get weather forecast
            Future<ForecastWeatherResponse> weather = asyncMethods.getWeatherForecast(home);

            System.out.println("5");

            handleResponse(info, weather);
        }
    }

    private void handleResponse(Future<List<FunctionalDeviceWithMeasurementsDTO>> info, Future<ForecastWeatherResponse> weather) throws ExecutionException, InterruptedException {
        List<FunctionalDeviceWithMeasurementsDTO> devices = info.get();
        ForecastWeatherResponse weatherResponse = weather.get();

        System.out.println(devices);
        System.out.println(weatherResponse);
    }

}

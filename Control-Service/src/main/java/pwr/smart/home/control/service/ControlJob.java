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
    @Scheduled(cron = "30 * * * * *")
    public void adjustForAllElements() throws ExecutionException, InterruptedException {
        List<Home> homes = dataService.getHomes();

        for (Home home : homes) {
            // Get functional devices info
            Future<List<FunctionalDeviceWithMeasurementsDTO>> info = asyncMethods.getHomeElements(home);

            asyncMethods.handleResponse(info, home);
        }
    }
}

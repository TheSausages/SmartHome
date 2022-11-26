package pwr.smart.home.control.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pwr.smart.home.control.model.Home;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ControlJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(ControlJob.class);

    @Autowired
    DataService dataService;

    /**
     * This will be ruin every 5 minutes
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void adjustForAllElements() {
        List<Home> homes = dataService.getHomes();

        for (Home home : homes) {

        }
    }

}

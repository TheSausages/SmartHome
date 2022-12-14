package pwr.smart.home.air.humidifier.sensor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pwr.smart.home.air.humidifier.device.model.State;
import pwr.smart.home.common.weather.OpenMeteo;
import pwr.smart.home.common.weather.model.request.ForecastWeatherRequest;
import pwr.smart.home.common.weather.model.response.ForecastWeatherResponse;


import static java.lang.Math.abs;

@Service
public class StatusService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatusService.class);
    private static State state = State.OFF;
    private static double currentHumidity;
    private static double humidityDifference;
    private static int currentPowerLevel = 2;
    private static final double GRADATION_SPEED = 0.1;

    @Autowired
    private DataEmitter dataEmitter;

    @Value("${device.power.1.watts}")
    private int devicePower;

    @Value("${device.power.2.watts}")
    private int devicePower2;

    @Value("${device.power.3.watts}")
    private int devicePower3;

    @Async("asyncExecutor")
    public void setTargetHumidity(int targetHumidity, int powerLevel) throws InterruptedException {
        setHumidityDifference(targetHumidity - currentHumidity);
        setCurrentPowerLevel(powerLevel);
        if (currentHumidity >= targetHumidity) {
            state = State.OFF;
            LOGGER.info("Turning off");
        } else {
            state = State.WORKING;
            LOGGER.info("Turning on");
        }
    }

    private void calculateConsumption() {
        int power;
        switch (currentPowerLevel) {
            case 1:
                power = devicePower;
                break;
            case 2:
                power = devicePower2;
                break;
            default:
                power = devicePower3;
        }
        double consumption = 25000 * power / 3600000d ; //Wh
        dataEmitter.reportConsumption(consumption);
    }

    @Scheduled(fixedDelay = 25000)
    public void simulateWorkingDevice() {
        switch (state) {
            case PERMANENT_OFF:
            case OFF:
                return;
            case WORKING:
        }

        double newValue = currentHumidity + GRADATION_SPEED * currentPowerLevel;
        currentHumidity = Math.round(newValue * 100.0) / 100.0;

        calculateConsumption();

        if (currentHumidity < 0 || currentHumidity > 100) {
            state = State.OFF;
        }
    }

    @Scheduled(fixedDelay = 50000)
    public void simulateNaturalDrying() {
        if (currentHumidity < 25) return;
        double multiplier = 1;
        setCurrentHumidity(currentHumidity - (multiplier * GRADATION_SPEED));
        LOGGER.info("Current humidity: {}", currentHumidity);
    }

    public static double getCurrentHumidity() {
        return currentHumidity;
    }

    public static void setCurrentHumidity(double currentHumidity) {
        StatusService.currentHumidity = currentHumidity;
    }

    public static void setHumidityDifference(double humidityDifference) {
        StatusService.humidityDifference = humidityDifference;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        StatusService.state = state;
    }

    public static int getCurrentPowerLevel() {
        return currentPowerLevel;
    }

    public static void setCurrentPowerLevel(int currentPowerLevel) {
        StatusService.currentPowerLevel = currentPowerLevel;
    }
}

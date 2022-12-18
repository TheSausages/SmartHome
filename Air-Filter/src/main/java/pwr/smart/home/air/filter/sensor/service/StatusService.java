package pwr.smart.home.air.filter.sensor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pwr.smart.home.air.filter.device.model.State;
import pwr.smart.home.air.filter.sensor.model.Sensor;
import pwr.smart.home.common.model.enums.AirConditionerState;
import pwr.smart.home.common.weather.OpenMeteo;
import pwr.smart.home.common.weather.model.request.AirQualityRequest;
import pwr.smart.home.common.weather.model.request.ForecastWeatherRequest;
import pwr.smart.home.common.weather.model.response.AirQualityResponse;
import pwr.smart.home.common.weather.model.response.ForecastWeatherResponse;

import java.util.Calendar;
import java.util.SplittableRandom;

import static java.lang.Math.abs;

@Service
public class StatusService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatusService.class);
    private static State state = State.OFF;
    private static int currentIAI;

    public static int getPowerLevel() {
        return powerLevel;
    }

    public static void setPowerLevel(int powerLevel) {
        StatusService.powerLevel = powerLevel;
    }

    private static int powerLevel;
    private static double currentPM25;
    private static int currentGas;
    private static final double GRADATION_SPEED = 0.1;
    @Autowired
    private Sensor sensor;

    @Autowired
    private DataEmitter dataEmitter;

    @Value("${new.value.propagation.delay}")
    private final int propagationDelay = 2500;

    @Value("${device.power.1.kw}")
    private int devicePower;

    @Value("${device.power.2.kw}")
    private int devicePower2;

    @Value("${device.power.3.kw}")
    private int devicePower3;

    @Async("asyncExecutor")
    public void startDevice(int powerLevel) throws InterruptedException {
        setPowerLevel(powerLevel);
        state = State.WORKING;
    }

    @Scheduled(fixedDelay = propagationDelay)
    public void simulateWorkingDevice() {
        switch (state) {
            case PERMANENT_OFF:
            case OFF:
                return;
            case WORKING:
        }

        setCurrentPM25(getNewDecreasedPM25(powerLevel));
        setCurrentIAI(getNewDecreasedIAI(powerLevel));
        setCurrentGas(getNewDecreasedGas(powerLevel));

        if (currentIAI == 1 && currentGas == 1 && currentPM25 < 4) {
            state = State.OFF;
            return;
        }

        LOGGER.info("After working: Current PM {}", getCurrentPM25());
        LOGGER.info("After working: Current Gas {}", getCurrentGas());
        LOGGER.info("After working: Current IAI {}", getCurrentIAI());

        calculateConsumption();
    }

    @Scheduled(fixedDelay = 50000)
    public void simulateNaturalPollution() {
        AirQualityRequest airQualityRequest = new AirQualityRequest(
                sensor.getLocation().getLatitude(),
                sensor.getLocation().getLongitude()
        );
        AirQualityResponse airQualityResponse = OpenMeteo.getCurrentAirCondition(airQualityRequest);
        if (airQualityResponse == null)
            return;

        float outsidePM = airQualityResponse.getHourly().getPm2_5().get(getCurrentHour());
        double differencePM25 = outsidePM - currentPM25;
        double multiplier = 0.1;
        setCurrentIAI(getNewIncreasedIAI());
        setCurrentGas(getNewIncreasedGas());
        setCurrentPM25(currentPM25 + (differencePM25 * multiplier * GRADATION_SPEED));
        LOGGER.info("Current PM2,5: {}, because there is {} PM 2.5 outside", currentPM25, outsidePM);
        LOGGER.info("Current Gas: {}", currentGas);
        LOGGER.info("Current IAI: {}", currentIAI);
    }

    private int getCurrentHour() {
        Calendar rightNow = Calendar.getInstance();
        return rightNow.get(Calendar.HOUR_OF_DAY);
    }

    private void calculateConsumption() {
        //40 / godzinę
        int power = 30;
        switch (powerLevel) {
            case 1:
                power = devicePower;
                break;
            case 2:
                power = devicePower2;
                break;
            case 3:
                power = devicePower3;
                break;
        }
        double consumption = (3600000f / propagationDelay) * power; //Wh
        dataEmitter.reportConsumption(consumption);
    }

    private static boolean doesItHappened(int chance)  {
        SplittableRandom splittableRandom = new SplittableRandom();
        int random = splittableRandom.nextInt(1, 101);
        return random < chance;
    }

    private static int getNewIncreasedGas() {
        int newGas = getCurrentGas();
        if(doesItHappened(4) && newGas < 4)
            newGas += 1;
        return newGas;
    }

    private int getNewIncreasedIAI() {
        int newIAI = getCurrentGas();
        if(doesItHappened(8) && newIAI < 12)
            newIAI += 1;
        return newIAI;
    }

    private static int getNewDecreasedGas(int powerLevel) {
        int newGas = getCurrentGas();
        if(doesItHappened(15 + 10 * powerLevel) && newGas > 1)
            newGas -= 1;
        return newGas;
    }

    private int getNewDecreasedIAI(int powerLevel) {
        int newIAI = getCurrentGas();
        if(doesItHappened(30 + 20 * powerLevel) && newIAI > 1)
            newIAI -= 1;
        return newIAI;
    }

    private double getNewDecreasedPM25(int powerLevel) {
        double newPM = getCurrentPM25();
        if(doesItHappened(10 + 20 * powerLevel) && newPM > 2) {
            newPM -= 2;
        }
        else if (doesItHappened(30 + 20 * powerLevel) && newPM > 1) {
            newPM -= 1;
        }
        return newPM;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        StatusService.state = state;
    }

    public static int getCurrentIAI() {
        return currentIAI;
    }

    public static void setCurrentIAI(int currentIAI) {
        StatusService.currentIAI = currentIAI;
    }

    public static double getCurrentPM25() {
        return currentPM25;
    }

    public static void setCurrentPM25(double currentPM25) {
        StatusService.currentPM25 = currentPM25;
    }

    public static int getCurrentGas() {
        return currentGas;
    }

    public static void setCurrentGas(int currentGas) {
        StatusService.currentGas = currentGas;
    }
}

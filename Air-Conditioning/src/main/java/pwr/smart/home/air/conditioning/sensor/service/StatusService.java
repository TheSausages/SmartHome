package pwr.smart.home.air.conditioning.sensor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pwr.smart.home.common.model.enums.AirConditionerState;
import pwr.smart.home.air.conditioning.sensor.model.Sensor;
import pwr.smart.home.common.weather.OpenMeteo;
import pwr.smart.home.common.weather.model.request.ForecastWeatherRequest;
import pwr.smart.home.common.weather.model.response.ForecastWeatherResponse;


import static java.lang.Math.abs;

@Service
public class StatusService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatusService.class);
    private static AirConditionerState state = AirConditionerState.OFF;
    private static double currentTemperature;
    private static double targetTemperature;
    private static int powerLevel;
    private static final double GRADATION_SPEED = 0.1;

    @Autowired
    private DataEmitter dataEmitter;

    @Autowired
    private Sensor sensor;

    @Value("${device.power.1.watts}")
    private int devicePower1;

    @Value("${device.power.2.watts}")
    private int devicePower2;

    @Value("${device.power.3.watts}")
    private int devicePower3;

    @Async("asyncExecutor")
    public void setTargetTemperature(double targetTemperature, int powerLevel, AirConditionerState stateFromControl) throws InterruptedException {
        setTargetTemperature(targetTemperature);
        setPowerLevel(powerLevel);
        switch (stateFromControl) {
            case COOLING:
                LOGGER.info("Turning on cooling");
                break;
            case HEATING:
                LOGGER.info("Turning on heating");
                break;
            case OFF:
                LOGGER.info("Turning off");
                break;
        }
        setState(stateFromControl);
    }

    private void calculateConsumption(double temperatureDifference, int powerLevel) {
        int devicePower;
        double divider;

        switch (powerLevel) {
            case 1:
                devicePower = devicePower1;
                divider = 0.5;
                break;
            case 2:
                devicePower = devicePower2;
                divider = 1;
                break;
            default:
                devicePower = devicePower3;
                divider = 2;
        }

        //1:pol stopnia / godzinę
        //2:jeden stopień / godzinę
        //3:dwa stopnie / godzinę

        double consumption = (abs(temperatureDifference)/divider) * devicePower; //Wh
        dataEmitter.reportConsumption(consumption);
    }

    public static double getCurrentTemperature() {
        return currentTemperature;
    }

    @Scheduled(fixedDelay = 25000)
    public void simulateWorkingDevice() {
        double multiplier = 1.0;
        switch (state) {
            case PERMANENT_OFF:
            case OFF:
                return;
            case COOLING:
                multiplier = -1.0;
                break;
            case HEATING:
                break;
        }

        if (currentTemperature >= targetTemperature && state == AirConditionerState.HEATING ||
                currentTemperature < targetTemperature && state == AirConditionerState.COOLING) {
            state = AirConditionerState.OFF;
            return;
        }

        double newValue = currentTemperature + (GRADATION_SPEED * multiplier * powerLevel);
        currentTemperature = Math.round(newValue * 100.0) / 100.0;
        LOGGER.info("Current temperature {}", getCurrentTemperature());

        calculateConsumption(currentTemperature - newValue, powerLevel);
    }

    @Scheduled(fixedDelay = 50000)
    public void simulateNaturalCooling() {
        ForecastWeatherRequest forecastWeatherRequest = new ForecastWeatherRequest(
                sensor.getLocation().getLatitude(),
                sensor.getLocation().getLongitude(),
                true
        );
        ForecastWeatherResponse weather = OpenMeteo.getWeather(forecastWeatherRequest);
        if (weather == null)
            return;
        float outsideTemperature = weather.getCurrent_weather().getTemperature();
        double temperatureDifference = currentTemperature - outsideTemperature;
        double multiplier = 0.01;
        setCurrentTemperature(currentTemperature - (temperatureDifference * multiplier * GRADATION_SPEED));
        LOGGER.info("Current temp: {}, because there is {} C outside", currentTemperature, outsideTemperature);
    }

    public static void setCurrentTemperature(double currentTemperature) {
        StatusService.currentTemperature = currentTemperature;
    }

    public static void setPowerLevel(int powerLevel) {
        StatusService.powerLevel = powerLevel;
    }

    public static void setTargetTemperature(double targetTemperature) {
        StatusService.targetTemperature = targetTemperature;
    }

    public AirConditionerState getState() {
        return state;
    }

    public void setState(AirConditionerState state) {
        StatusService.state = state;
    }
}

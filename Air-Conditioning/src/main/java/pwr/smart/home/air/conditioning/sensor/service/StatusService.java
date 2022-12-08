package pwr.smart.home.air.conditioning.sensor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pwr.smart.home.air.conditioning.device.model.State;


import static java.lang.Math.abs;

@Service
public class StatusService {

    private static State state = State.OFF;
    private static double currentTemperature;
    private static final double GRADATION_SPEED = 0.1;

    @Autowired
    private DataEmitter dataEmitter;

    @Value("${new.value.propagation.delay}")
    private int propagationDelay;

    @Value("${device.power.kw}")
    private int devicePower;

    @Async("asyncExecutor")
    public void setTargetTemperature(double targetTemperature) throws InterruptedException {
        double temperatureDifference = targetTemperature - currentTemperature;

        if (temperatureDifference != 0) {
            turnOnDevice(temperatureDifference);
        } else {
            state = State.OFF;
        }
    }

    private void turnOnDevice(double temperatureDifference) throws InterruptedException {
        double multiplier = 1.0;

        if (temperatureDifference > 0) {
            state = State.HEATING;
        } else if (temperatureDifference < 0) {
            state = State.COOLING;
            multiplier = -1.0;
        }

        double iterations = temperatureDifference / GRADATION_SPEED;
        int iterationsInt = abs((int) iterations);

        for (int i = 0; i < iterationsInt; i++) {
            double newValue = currentTemperature + (GRADATION_SPEED * multiplier);
            currentTemperature = Math.round(newValue * 100.0) / 100.0;
            Thread.sleep(propagationDelay);
        }

        calculateConsumption(temperatureDifference);
    }

    private void calculateConsumption(double temperatureDifference) {
        //jeden stopień / godzinę
        double consumption = abs(temperatureDifference) * devicePower; //Wh
        dataEmitter.reportConsumption(consumption);
    }

    public static double getCurrentTemperature() {
        return currentTemperature;
    }

    public static void setCurrentTemperature(double currentTemperature) {
        StatusService.currentTemperature = currentTemperature;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        StatusService.state = state;
    }
}

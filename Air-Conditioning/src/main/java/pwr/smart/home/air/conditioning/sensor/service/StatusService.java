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

    @Value("${device.power.1.kw}")
    private int devicePower1;

    @Value("${device.power.2.kw}")
    private int devicePower2;

    @Value("${device.power.3.kw}")
    private int devicePower3;

    @Async("asyncExecutor")
    public void setTargetTemperature(double targetTemperature, int powerLevel) throws InterruptedException {
        double temperatureDifference = targetTemperature - currentTemperature;

        if (temperatureDifference != 0) {
            turnOnDevice(temperatureDifference, powerLevel);
        } else {
            state = State.OFF;
        }
    }

    private void turnOnDevice(double temperatureDifference, int powerLevel) throws InterruptedException {
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

            if (currentTemperature > 40 || currentTemperature < 5) {
                state = State.OFF;
                return;
            }

            Thread.sleep(propagationDelay);
        }
        state = State.OFF;
        calculateConsumption(temperatureDifference, powerLevel);
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

        double consumption = (abs(temperatureDifference)/divider)* devicePower; //Wh
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

package pwr.smart.home.air.humidifier.sensor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pwr.smart.home.air.humidifier.device.model.State;


import static java.lang.Math.abs;

@Service
public class StatusService {

    private static State state = State.OFF;
    private static double currentHumidity;
    private static final double GRADATION_SPEED = 0.1;

    @Autowired
    private DataEmitter dataEmitter;

    @Value("${new.value.propagation.delay}")
    private int propagationDelay;

    @Value("${device.power.kw}")
    private int devicePower;

    @Async("asyncExecutor")
    public void setTargetHumidity(int targetHumidity) throws InterruptedException {
        double humidityDifference = targetHumidity - currentHumidity;

        if (humidityDifference > 0) {
            turnOnDevice(humidityDifference);
        } else {
            state = State.OFF;
        }
    }

    private void turnOnDevice(double humidityDifference) throws InterruptedException {
        state = State.WORKING;

        double iterations = humidityDifference / GRADATION_SPEED;
        int iterationsInt = (int) iterations;

        for (int i = 0; i < iterationsInt; i++) {
            double newValue = currentHumidity + GRADATION_SPEED;
            currentHumidity = Math.round(newValue * 100.0) / 100.0;

            if (currentHumidity < 0 || currentHumidity > 100) {
                state = State.OFF;
                return;
            }

            Thread.sleep(propagationDelay);
        }

        state = State.OFF;
        calculateConsumption(humidityDifference);
    }

    private void calculateConsumption(double humidityDifference) {
        //10% / godzinę
        double consumption = (abs(humidityDifference)/10) * devicePower; //Wh
        dataEmitter.reportConsumption(consumption);
    }

    public static double getCurrentHumidity() {
        return currentHumidity;
    }

    public static void setCurrentHumidity(double currentHumidity) {
        StatusService.currentHumidity = currentHumidity;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        StatusService.state = state;
    }
}

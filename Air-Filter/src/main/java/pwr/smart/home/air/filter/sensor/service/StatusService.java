package pwr.smart.home.air.filter.sensor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pwr.smart.home.air.filter.model.State;

import static java.lang.Math.abs;

@Service
public class StatusService {

    private static State state = State.OFF;
    private static double currentAirQuality;
    private static final double GRADATION_SPEED = 0.1;

    @Value("${new.value.propagation.delay}")
    private int propagationDelay;

    @Async("asyncExecutor")
    public void setAirQualityValue(int targetAirQuality) throws InterruptedException {
        double airQualityDifference = targetAirQuality - currentAirQuality;

        if (airQualityDifference < 0) {
            turnOnDevice(airQualityDifference);
        } else {
            state = State.OFF;
        }
    }

    private void turnOnDevice(double airQualityDifference) throws InterruptedException {
        state = State.WORKING;

        double iterations = airQualityDifference / GRADATION_SPEED;
        int iterationsInt = abs((int) iterations);

        for (int i = 0; i < iterationsInt; i++) {
            double newValue = currentAirQuality - GRADATION_SPEED;
            currentAirQuality = Math.round(newValue * 100.0) / 100.0;
            Thread.sleep(propagationDelay);
        }
    }

    public static double getCurrentAirQuality() {
        return currentAirQuality;
    }

    public static void setCurrentAirQuality(double currentAirQuality) {
        StatusService.currentAirQuality = currentAirQuality;
    }

    public static int getIAI(){
        return UnitConversionService.resolveIAI((int) Math.round(StatusService.getCurrentAirQuality()));
    }

    public static int getGas(){
        return UnitConversionService.resolveGas((int) Math.round(StatusService.getCurrentAirQuality()));
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        StatusService.state = state;
    }
}

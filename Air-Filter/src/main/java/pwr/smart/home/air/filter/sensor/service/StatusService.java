package pwr.smart.home.air.filter.sensor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pwr.smart.home.air.filter.device.model.State;

import static java.lang.Math.abs;

@Service
public class StatusService {

    private static State state = State.OFF;
    private static double currentAirQuality;
    private static final double GRADATION_SPEED = 0.1;

    @Autowired
    private DataEmitter dataEmitter;

    @Value("${new.value.propagation.delay}")
    private int propagationDelay;

    @Value("${device.power.kw}")
    private int devicePower;

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

            if (currentAirQuality < 0) {
                state = State.OFF;
                return;
            }

            Thread.sleep(propagationDelay);
        }
        state = State.OFF;
        calculateConsumption(airQualityDifference);
    }

    private void calculateConsumption(double airQualityDifference) {
        //40 / godzinę
        double consumption = (abs(airQualityDifference)/40) * devicePower; //Wh
        dataEmitter.reportConsumption(consumption);
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

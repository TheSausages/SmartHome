package pwr.smart.home.control.service;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pwr.smart.home.common.model.enums.AirConditionerState;
import pwr.smart.home.common.model.enums.MeasurementType;
import pwr.smart.home.control.model.Endpoint;
import pwr.smart.home.control.model.FunctionalDeviceWithMeasurementsDTO;
import pwr.smart.home.control.model.Home;
import pwr.smart.home.control.model.Measurement;
import pwr.smart.home.common.weather.model.response.AirQualityResponse;
import pwr.smart.home.common.weather.model.response.ForecastWeatherResponse;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FunctionalDevicesAsyncMethods {
    private static final Logger LOGGER = LoggerFactory.getLogger(FunctionalDevicesAsyncMethods.class);
    private static final String NO_HOURS_CHOSEN_SO_ACTION_IS_NOT_REQUIRED = "No hours chosen so action is not required {}";
    private static final String NO_ACTION_REQUIRED = "No Action required {}";

    @Autowired
    private OpenMeteoAsyncMethods openMeteoAsyncMethods;

    @Autowired
    private DataEmitter dataEmitter;

    @Autowired
    private DataService dataService;

    @Autowired
    private Endpoint endpoint;

    @Async(value = "threadPoolTaskExecutor")
    public CompletableFuture<List<FunctionalDeviceWithMeasurementsDTO>> getFunctionalDevicesWithMeasurementsForHome(Home home) {
        List<FunctionalDeviceWithMeasurementsDTO> devices = dataService.getFunctionalDevicesWithMeasurementsForHome(home);

        return CompletableFuture.completedFuture(devices);
    }

    @Async(value = "TemperatureThreadPoolTaskExecutor")
    public Future<String> handleTemperature(FunctionalDeviceWithMeasurementsDTO data, String target, Home home, ForecastWeatherResponse weather) {
        LOGGER.info("Handle Temperature for {}", data.getDevice().getSerialNumber());
        if (target != null)
            home.setPreferredTemp(Integer.parseInt(target));
        if (!data.getMeasurements().containsKey(MeasurementType.CELSIUS)) {
            throw new RuntimeException("No Celsius measurement for temperature");
        }

        List<Measurement> temperatureMeasurement = data.getMeasurements().get(MeasurementType.CELSIUS);

        SimpleRegression regression = new SimpleRegression();

        temperatureMeasurement.forEach( mes -> regression.addData(mes.getCreatedAt().getTime(), mes.getValue()));

        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        int nextHour = findNextUsageHour(currentHour, home.getHours());

        int lastMeasurement = temperatureMeasurement.stream().max(Comparator.comparing(Measurement::getCreatedAt)).get().getValue();

        double prediction = regression.predict(Timestamp.from(Instant.now().plus(timeDifference(nextHour, currentHour), ChronoUnit.HOURS)).getTime());
        if (checkIfActionsNeedToBeTaken(home, currentHour, lastMeasurement, weather, nextHour)) {
            LOGGER.info("Temperature for {} is ok for now", data.getDevice().getSerialNumber());
            return CompletableFuture.completedFuture(dataEmitter.callForAction("", endpoint.getAirConditionerUrl(data.getDevice().getSerialNumber()) + "/turnOff", 0, data.getDevice().getSerialNumber(), AirConditionerState.OFF));
        }

        int settingTemp = home.getPreferredTemp();
        if (prediction > home.getPreferredTemp()) {
            settingTemp -= 1;
            LOGGER.info("Set Temperature device to {}", settingTemp);
            return CompletableFuture.completedFuture(dataEmitter.callForAction(Integer.toString(settingTemp), endpoint.getAirConditionerUrl(data.getDevice().getSerialNumber()) + "/setTarget", data.getDevice().getPowerLevel(), data.getDevice().getSerialNumber(), AirConditionerState.COOLING));
        }
        else if (prediction < home.getPreferredTemp()) {
            settingTemp += 1;
            LOGGER.info("Set Temperature device to {}", settingTemp);
            return CompletableFuture.completedFuture(dataEmitter.callForAction(Integer.toString(settingTemp), endpoint.getAirConditionerUrl(data.getDevice().getSerialNumber()) + "/setTarget", data.getDevice().getPowerLevel(), data.getDevice().getSerialNumber(), AirConditionerState.HEATING));
        }
        LOGGER.info(NO_ACTION_REQUIRED, data.getDevice().getSerialNumber());
        return CompletableFuture.completedFuture(dataEmitter.callForAction("", endpoint.getAirConditionerUrl(data.getDevice().getSerialNumber()) + "/turnOff", 0, data.getDevice().getSerialNumber(), AirConditionerState.OFF));
    }

     private boolean checkIfActionsNeedToBeTaken(Home home, int currentHour, int lastMeasurement, ForecastWeatherResponse weather, int nextHour) {
         boolean isWarmOutside = !(weather.getCurrent_weather().getTemperature() < 20f);
         return nextHour == -1 ||
                 (home.getHours().contains(currentHour) && home.getPreferredTemp() <= lastMeasurement && !isWarmOutside) ||
                 (home.getHours().contains(currentHour) && home.getPreferredTemp() > lastMeasurement && isWarmOutside) ||
                 (timeDifference(nextHour, currentHour) > 2 && !home.getHours().contains(currentHour));
     }

    private int timeDifference(int nextHour, int currentHour) {
        return (nextHour - currentHour) % 24;
    }

    private int findNextUsageHour(int currentHour, Set<Integer> hours) {
        return hours.stream().filter(h -> h > currentHour).min(Integer::compareTo).orElse(hours.stream().min(Integer::compareTo).orElse(-1));
    }

    @Async(value = "HumidityThreadPoolTaskExecutor")
    public Future<String> handleHumidity(FunctionalDeviceWithMeasurementsDTO data, String target, Home home, ForecastWeatherResponse weather) {
        LOGGER.info("Handle Humidity for {}", data.getDevice().getSerialNumber());

        if (!data.getMeasurements().containsKey(MeasurementType.HUMIDITY)) {
            throw new RuntimeException("No measurement for humidity");
        }

        List<Measurement> humidityMeasurement = data.getMeasurements().get(MeasurementType.HUMIDITY);

        SimpleRegression regression = new SimpleRegression();

        humidityMeasurement.forEach( mes -> regression.addData(mes.getCreatedAt().getTime(), mes.getValue()));

        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        int nextHour = findNextUsageHour(currentHour, home.getHours());

        int lastMeasurement = humidityMeasurement.stream().max(Comparator.comparing(Measurement::getCreatedAt)).get().getValue();
        if (nextHour == -1 || (home.getHours().contains(currentHour) && home.getPreferredHum() <= lastMeasurement) ||
                (timeDifference(nextHour, currentHour) > 2 && !home.getHours().contains(currentHour))) {
            LOGGER.info(NO_HOURS_CHOSEN_SO_ACTION_IS_NOT_REQUIRED, data.getDevice().getSerialNumber());
            return CompletableFuture.completedFuture(dataEmitter.callForAction(Integer.toString(0), endpoint.getAirHumidifierUrl(data.getDevice().getSerialNumber()) + "/turnOff", 0, data.getDevice().getSerialNumber(), null));
        }

        double prediction = regression.predict(Timestamp.from(Instant.now().plus(timeDifference(nextHour, currentHour), ChronoUnit.HOURS)).getTime());

        if (lastMeasurement <= home.getPreferredHum() || prediction < home.getPreferredHum()) {
            int settingHum = home.getPreferredHum() + 2;
            LOGGER.info("Set Humidity device to {}", settingHum);
            return CompletableFuture.completedFuture(dataEmitter.callForAction(Integer.toString(settingHum), endpoint.getAirHumidifierUrl(data.getDevice().getSerialNumber()) + "/setTarget", data.getDevice().getPowerLevel(), data.getDevice().getSerialNumber(), null));
        }

        LOGGER.info(NO_ACTION_REQUIRED, data.getDevice().getSerialNumber());
        return CompletableFuture.completedFuture(dataEmitter.callForAction(Integer.toString(0), endpoint.getAirHumidifierUrl(data.getDevice().getSerialNumber()) + "/turnOff", 0, data.getDevice().getSerialNumber(), null));
    }

    @Async(value = "FilterThreadPoolTaskExecutor")
    public Future<String> handleFilter(FunctionalDeviceWithMeasurementsDTO data, String target, Home home, AirQualityResponse air) {
        LOGGER.info("Handle Filter for {}", data.getDevice());

        SimpleRegression gasRegression = new SimpleRegression();
        SimpleRegression iaiRegression = new SimpleRegression();
        SimpleRegression pm25Regression = new SimpleRegression();

        if (data.getMeasurements().containsKey(MeasurementType.GAS)) {
            List<Measurement> gasMeasurement = data.getMeasurements().get(MeasurementType.GAS);

            gasMeasurement.forEach( mes -> gasRegression.addData(mes.getCreatedAt().getTime(), mes.getValue()));
        }

        if (data.getMeasurements().containsKey(MeasurementType.IAI)) {
            List<Measurement> iaiMeasurement = data.getMeasurements().get(MeasurementType.IAI);

            iaiMeasurement.forEach( mes -> iaiRegression.addData(mes.getCreatedAt().getTime(), mes.getValue()));
        }

        if (data.getMeasurements().containsKey(MeasurementType.PM25)) {
            List<Measurement> pmMeasurement = data.getMeasurements().get(MeasurementType.PM25);

            pmMeasurement.forEach( mes -> pm25Regression.addData(mes.getCreatedAt().getTime(), mes.getValue()));
        }

        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        int nextHour = findNextUsageHour(currentHour, home.getHours());

        if (nextHour == -1 || (timeDifference(nextHour, currentHour) > 2 && !home.getHours().contains(currentHour))) {
            LOGGER.info(NO_HOURS_CHOSEN_SO_ACTION_IS_NOT_REQUIRED, data.getDevice().getSerialNumber());
            return CompletableFuture.completedFuture(dataEmitter.callForAction(Integer.toString(0), endpoint.getAirFilterUrl(data.getDevice().getSerialNumber()) + "/turnOff", 0, data.getDevice().getSerialNumber(), null));
        }

        double pm25Prediction = pm25Regression.predict(Timestamp.from(Instant.now().plus(timeDifference(nextHour, currentHour), ChronoUnit.HOURS)).getTime());
        double gasPrediction = gasRegression.predict(Timestamp.from(Instant.now().plus(timeDifference(nextHour, currentHour), ChronoUnit.HOURS)).getTime());
        double iaiPrediction = iaiRegression.predict(Timestamp.from(Instant.now().plus(timeDifference(nextHour, currentHour), ChronoUnit.HOURS)).getTime());

        AirCondition predictedPmCondition = AirCondition.getAirConditionForPm(pm25Prediction);
        AirCondition predictedGasCondition = AirCondition.getAirConditionForGas(gasPrediction);
        AirCondition predictedIaiCondition = AirCondition.getAirConditionForIai(iaiPrediction);

        // If all predict good results, turn off the device
        if (predictedGasCondition == AirCondition.GOOD && predictedIaiCondition == AirCondition.GOOD && predictedPmCondition == AirCondition.GOOD) {
            LOGGER.info(NO_ACTION_REQUIRED, data.getDevice().getSerialNumber());

            return CompletableFuture.completedFuture(dataEmitter.callForAction(Integer.toString(0), endpoint.getAirFilterUrl(data.getDevice().getSerialNumber()) + "/turnOff", 0, data.getDevice().getSerialNumber(), null));
        }

        Map<AirCondition, Long> nrOfPredictions = Stream.of(predictedPmCondition, predictedGasCondition, predictedIaiCondition)
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        // If there are 2 good, and 1 acceptable also do nothing
        if (nrOfPredictions.get(AirCondition.GOOD) == 2 && nrOfPredictions.get(AirCondition.ACCEPTABLE) == 1) {
            LOGGER.info(NO_ACTION_REQUIRED, data.getDevice().getSerialNumber());

            return CompletableFuture.completedFuture(dataEmitter.callForAction(Integer.toString(0), endpoint.getAirFilterUrl(data.getDevice().getSerialNumber()) + "/turnOff", 0, data.getDevice().getSerialNumber(), null));
        }

        // Add something

        LOGGER.info("Set Filter device to {}", 5);

        return CompletableFuture.completedFuture(dataEmitter.callForAction(Integer.toString(5), endpoint.getAirFilterUrl(data.getDevice().getSerialNumber()) + "/setTarget", data.getDevice().getPowerLevel(), data.getDevice().getSerialNumber(), null));
    }

    private enum AirCondition {
        GOOD(20, 1, 3),
        ACCEPTABLE(35, 2, 6),
        BAD(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

        private final int pmThreshold;
        private final int gasThreshold;
        private final int iaiThreshold;

        AirCondition(int pmThreshold, int gasThreshold, int iaiThreshold) {
            this.pmThreshold = pmThreshold;
            this.gasThreshold = gasThreshold;
            this.iaiThreshold = iaiThreshold;
        }

        public static AirCondition getAirConditionForIai(double iaiValue) {
            if (iaiValue <= GOOD.iaiThreshold) {
                return AirCondition.GOOD;
            }

            if (iaiValue <= ACCEPTABLE.iaiThreshold) {
                return AirCondition.ACCEPTABLE;
            }

            return AirCondition.BAD;
        }

        public static AirCondition getAirConditionForGas(double gasValue) {
            if (gasValue <= GOOD.gasThreshold) {
                return AirCondition.GOOD;
            }

            if (gasValue <= ACCEPTABLE.gasThreshold) {
                return AirCondition.ACCEPTABLE;
            }

            return AirCondition.BAD;
        }

        public static AirCondition getAirConditionForPm(double pmValue) {
            if (pmValue <= GOOD.pmThreshold) {
                return AirCondition.GOOD;
            }

            if (pmValue <= ACCEPTABLE.pmThreshold) {
                return AirCondition.ACCEPTABLE;
            }

            return AirCondition.BAD;
        }
    }
}

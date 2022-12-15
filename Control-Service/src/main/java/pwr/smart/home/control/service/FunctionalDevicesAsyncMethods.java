package pwr.smart.home.control.service;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pwr.smart.home.common.model.enums.MeasurementType;
import pwr.smart.home.control.model.Endpoint;
import pwr.smart.home.control.model.FunctionalDeviceWithMeasurementsDTO;
import pwr.smart.home.control.model.Home;
import pwr.smart.home.control.model.Measurement;
import pwr.smart.home.control.weather.model.response.AirQualityResponse;
import pwr.smart.home.control.weather.model.response.ForecastWeatherResponse;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class FunctionalDevicesAsyncMethods {
    private static final Logger LOGGER = LoggerFactory.getLogger(FunctionalDevicesAsyncMethods.class);

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
        if (!data.getMeasurements().containsKey(MeasurementType.CELSIUS)) {
            throw new RuntimeException("No Celsius measurement for temperature");
        }

        List<Measurement> temperatureMeasurement = data.getMeasurements().get(MeasurementType.CELSIUS);

        // For now We use regression to get the prediction for the next
        SimpleRegression regression = new SimpleRegression();

        temperatureMeasurement.forEach( mes -> regression.addData(mes.getCreatedAt().getTime(), mes.getValue()));

        double prediction = regression.predict(Timestamp.from(Instant.now().plus(12, ChronoUnit.HOURS)).getTime());

        if (prediction <= home.getPreferredTemp()) {
            // For testing purposes add something with weather
            int settingTemp = home.getPreferredTemp() + 1;

            LOGGER.info("Set Temperature device to {}", settingTemp);

            return CompletableFuture.completedFuture(dataEmitter.callForAction(Integer.toString(settingTemp), endpoint.getAirConditionerUrl(data.getDevice().getSerialNumber()) + "/setTarget", data.getDevice().getPowerLevel(), data.getDevice().getSerialNumber()));
        } else {
            // For testing purposes add something with weather
            int settingTemp = home.getPreferredTemp() - 1;

            LOGGER.info("Set Temperature device to {}", settingTemp);

            return CompletableFuture.completedFuture(dataEmitter.callForAction(Integer.toString(settingTemp), endpoint.getAirConditionerUrl(data.getDevice().getSerialNumber()) + "/setTarget", data.getDevice().getPowerLevel(), data.getDevice().getSerialNumber()));
        }
    }

    @Async(value = "HumidityThreadPoolTaskExecutor")
    public Future<String> handleHumidity(FunctionalDeviceWithMeasurementsDTO data, String target, Home home, ForecastWeatherResponse weather) {
        if (!data.getMeasurements().containsKey(MeasurementType.HUMIDITY)) {
            throw new RuntimeException("No Celsius measurement for temperature");
        }

        List<Measurement> temperatureMeasurement = data.getMeasurements().get(MeasurementType.HUMIDITY);

        // For now We use regression to get the prediction for the next
        SimpleRegression regression = new SimpleRegression();

        temperatureMeasurement.forEach( mes -> regression.addData(mes.getCreatedAt().getTime(), mes.getValue()));

        double prediction = regression.predict(Timestamp.from(Instant.now().plus(12, ChronoUnit.HOURS)).getTime());

        if (prediction <= home.getPreferredTemp()) {
            // For testing purposes add something with weather
            int settingTemp = home.getPreferredTemp() + 1;

            LOGGER.info("Set Humidity device to {}", settingTemp);

            return CompletableFuture.completedFuture(dataEmitter.callForAction(Integer.toString(settingTemp), endpoint.getAirHumidifierUrl(data.getDevice().getSerialNumber()) + "/setTarget", data.getDevice().getPowerLevel(), data.getDevice().getSerialNumber()));
        } else {
            // For testing purposes add something with weather
            int settingTemp = home.getPreferredTemp() - 1;

            LOGGER.info("Set Humidity device to {}", settingTemp);

            return CompletableFuture.completedFuture(dataEmitter.callForAction(Integer.toString(settingTemp), endpoint.getAirHumidifierUrl(data.getDevice().getSerialNumber()) + "/setTarget", data.getDevice().getPowerLevel(), data.getDevice().getSerialNumber()));
        }
    }

    @Async(value = "FilterThreadPoolTaskExecutor")
    public Future<String> handleFilter(FunctionalDeviceWithMeasurementsDTO data, String target, Home home, AirQualityResponse air) {
        if (data.getMeasurements().containsKey(MeasurementType.GAS)) {
            // Some operation with Gas
            List<Measurement> temperatureMeasurement = data.getMeasurements().get(MeasurementType.GAS);
        }

        if (data.getMeasurements().containsKey(MeasurementType.IAI)) {
            // Some operations with IAI
            List<Measurement> temperatureMeasurement = data.getMeasurements().get(MeasurementType.IAI);
        }

        if (data.getMeasurements().containsKey(MeasurementType.PM25)) {
            // Some operations with PM25
            List<Measurement> temperatureMeasurement = data.getMeasurements().get(MeasurementType.PM25);
        }

        LOGGER.info("Set Filter device to {}", 5);

        return CompletableFuture.completedFuture(dataEmitter.callForAction(Integer.toString(5), endpoint.getAirFilterUrl(data.getDevice().getSerialNumber()) + "/setTarget", data.getDevice().getPowerLevel(), data.getDevice().getSerialNumber()));
    }
}

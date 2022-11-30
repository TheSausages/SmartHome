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
import pwr.smart.home.control.weather.OpenMeteo;
import pwr.smart.home.control.weather.model.request.AirQualityRequest;
import pwr.smart.home.control.weather.model.request.ForecastWeatherRequest;
import pwr.smart.home.control.weather.model.response.AirQualityResponse;
import pwr.smart.home.control.weather.model.response.ForecastWeatherResponse;
import pwr.smart.home.control.weather.model.response.WeatherApiResponse;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class AsyncMethods {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncMethods.class);

    @Autowired
    AsyncMethods2 asyncMethods2;

    @Autowired
    DataService dataService;

    @Autowired
    DataEmitter dataEmitter;

    @Autowired
    Endpoint endpoint;

    @Async
    public Future<List<FunctionalDeviceWithMeasurementsDTO>> getHomeElements(Home home) {
        List<FunctionalDeviceWithMeasurementsDTO> list = dataService.getFunctionalDevicesWithMeasurementsForHome(home);

        return CompletableFuture.completedFuture(list);
    }

    @Async
    public void handleResponse(Future<List<FunctionalDeviceWithMeasurementsDTO>> info, Home home) throws ExecutionException, InterruptedException {
        List<FunctionalDeviceWithMeasurementsDTO> devices = info.get();

        devices.forEach(device -> {
            switch (device.getDevice().getType()) {
                case AIR_FILTER:
                    handleFilter(device, home);
                    break;
                case AIR_HUMIDIFIER:
                    handleHumidity(device, home);
                    break;
                case AIR_CONDITIONER:
                    handleTemperature(device, home);
                    break;
                default:
                    LOGGER.info("Device of unknown type found: {}", device.getDevice().getType());
            }
        });
    }

    private void handleTemperature(FunctionalDeviceWithMeasurementsDTO data, Home home) {
        // weather can be used in the future for a better model
        Future<ForecastWeatherResponse> weather = asyncMethods2.getWeatherForecast(home);

        if (!data.getMeasurements().containsKey(MeasurementType.CELSIUS)) {
            throw new RuntimeException("No Celsius measurement for temperature");
        }

        List<Measurement> temperatureMeasurement = data.getMeasurements().get(MeasurementType.CELSIUS);

        // For now We use regression to get the prediction for the next
        SimpleRegression regression = new SimpleRegression();

        temperatureMeasurement.forEach( mes -> regression.addData(mes.getCreatedAt().getTime(), mes.getValue()));

        double prediction = regression.predict(Timestamp.from(Instant.now().plus(12, ChronoUnit.HOURS)).getTime());

        if (prediction <= home.getPreferredTemp()) {
            // For testing purposes add somethign with weather
            double settingTemp = home.getPreferredTemp() + 1;

            dataEmitter.callForAction(Double.toString(settingTemp), endpoint.getAirConditionerUrl());
        } else {
            // For testing purposes add somethign with weather
            double settingTemp = home.getPreferredTemp() - 1;

            dataEmitter.callForAction(Double.toString(settingTemp), endpoint.getAirConditionerUrl());
        }
    }

    private void handleHumidity(FunctionalDeviceWithMeasurementsDTO data, Home home) {
        Future<ForecastWeatherResponse> weather = asyncMethods2.getWeatherForecast(home);
    }

    private void handleFilter(FunctionalDeviceWithMeasurementsDTO data, Home home) {
        Future<AirQualityResponse> weather = asyncMethods2.getAirData(home);
    }
}

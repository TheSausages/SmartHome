package pwr.smart.home.control.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pwr.smart.home.control.model.Home;
import pwr.smart.home.control.weather.OpenMeteo;
import pwr.smart.home.control.weather.model.request.AirQualityRequest;
import pwr.smart.home.control.weather.model.request.ForecastWeatherRequest;
import pwr.smart.home.control.weather.model.response.AirQualityResponse;
import pwr.smart.home.control.weather.model.response.ForecastWeatherResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class OpenMeteoAsyncMethods {
    @Autowired
    private OpenMeteo openMeteo;

    @Async
    public Future<ForecastWeatherResponse> getWeatherForecast(Home home) {
        ForecastWeatherResponse forecastWeatherResponse = openMeteo.getTodayAndTomorrowWeather(new ForecastWeatherRequest(
                home.getLatitude(),
                home.getLongitude(),
                ForecastWeatherRequest.dailyParameters
        ));

        return CompletableFuture.completedFuture(forecastWeatherResponse);
    }

    @Async
    public Future<AirQualityResponse> getAirData(Home home) {
        AirQualityResponse forecastWeatherResponse = openMeteo.getCurrentAirCondition(new AirQualityRequest(
                home.getLatitude(),
                home.getLongitude()
        ));

        return CompletableFuture.completedFuture(forecastWeatherResponse);
    }
}

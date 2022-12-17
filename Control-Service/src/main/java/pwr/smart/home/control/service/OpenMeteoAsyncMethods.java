package pwr.smart.home.control.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pwr.smart.home.control.model.Home;
import pwr.smart.home.common.weather.OpenMeteo;
import pwr.smart.home.common.weather.model.request.AirQualityRequest;
import pwr.smart.home.common.weather.model.request.ForecastWeatherRequest;
import pwr.smart.home.common.weather.model.response.AirQualityResponse;
import pwr.smart.home.common.weather.model.response.ForecastWeatherResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class OpenMeteoAsyncMethods {

    @Async(value = "threadPoolTaskExecutor")
    public Future<ForecastWeatherResponse> getWeatherForecast(Home home) {
        ForecastWeatherResponse forecastWeatherResponse = OpenMeteo.getWeather(new ForecastWeatherRequest(
                home.getLatitude(),
                home.getLongitude(),
                ForecastWeatherRequest.dailyParameters
        ));

        return CompletableFuture.completedFuture(forecastWeatherResponse);
    }

    @Async(value = "threadPoolTaskExecutor")
    public Future<AirQualityResponse> getAirData(Home home) {
        AirQualityResponse forecastWeatherResponse = OpenMeteo.getCurrentAirCondition(new AirQualityRequest(
                home.getLatitude(),
                home.getLongitude()
        ));

        return CompletableFuture.completedFuture(forecastWeatherResponse);
    }
}

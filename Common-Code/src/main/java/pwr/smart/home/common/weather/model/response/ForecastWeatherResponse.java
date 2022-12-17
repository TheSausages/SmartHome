package pwr.smart.home.common.weather.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pwr.smart.home.common.weather.model.CurrentWeather;
import pwr.smart.home.common.weather.model.Daily;
import pwr.smart.home.common.weather.model.DailyUnits;

@Setter
@Getter
@NoArgsConstructor
public class ForecastWeatherResponse extends WeatherApiResponse {
    private Daily daily;
    private DailyUnits daily_units;
    private CurrentWeather current_weather;
}

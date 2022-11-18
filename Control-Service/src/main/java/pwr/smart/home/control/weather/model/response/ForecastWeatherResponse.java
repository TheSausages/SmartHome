package pwr.smart.home.control.weather.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pwr.smart.home.control.weather.model.Daily;
import pwr.smart.home.control.weather.model.DailyUnits;

@Setter
@Getter
@NoArgsConstructor
public class ForecastWeatherResponse extends WeatherApiResponse {
    private Daily daily;
    private DailyUnits dailyUnits;
}

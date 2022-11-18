package pwr.smart.home.control.weather.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pwr.smart.home.control.weather.model.Hourly;
import pwr.smart.home.control.weather.model.HourlyUnits;

@Setter
@Getter
@NoArgsConstructor
public class AirQualityResponse extends WeatherApiResponse {
    private Hourly hourly;
    private HourlyUnits hourly_units;
}

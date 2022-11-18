package pwr.smart.home.control.weather.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public abstract class WeatherApiResponse {
    private float latitude;
    private float longitude;
    private float generationtime_ms;
    private int utc_offset_seconds;
    private String timezone;
    private String timezone_abbreviation;
}

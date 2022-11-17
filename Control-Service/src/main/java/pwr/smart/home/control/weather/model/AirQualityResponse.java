package pwr.smart.home.control.weather.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class AirQualityResponse {
    private float latitude;
    private float longitude;
    private float generationtime_ms;
    private int utc_offset_seconds;
    private String timezone;
    private String timezone_abbreviation;
    private Hourly hourly;
    private HourlyUnits hourly_units;

    private String getCurrentValues() {
        return "";
    }
}

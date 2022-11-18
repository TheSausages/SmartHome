package pwr.smart.home.control.weather.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class DailyUnits {
    private String temperature_2m_max;
    private String temperature_2m_min;
    private String apparent_temperature_max;
    private String apparent_temperature_min;
    private String precipitation_hours;
    private String weathercode;
}

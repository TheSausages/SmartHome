package pwr.smart.home.common.weather.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CurrentWeather {
    private String time;
    private float temperature;
    private int weathercode;
    private float windspeed;
    private int winddirection;
}

package pwr.smart.home.control.weather.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class HourlyUnits {
    private String time;
    private String pm10;
    private String pm2_5;
    private String carbon_monoxide;
    private String nitrogen_dioxide;
    private String sulphur_dioxide;
}

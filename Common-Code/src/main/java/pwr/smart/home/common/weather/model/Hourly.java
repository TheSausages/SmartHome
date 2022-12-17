package pwr.smart.home.common.weather.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Hourly {
    private List<String> time;
    private List<Float> pm10;
    private List<Float> pm2_5;
    private List<Float> carbon_monoxide;
    private List<Float> nitrogen_dioxide;
    private List<Float> sulphur_dioxide;
}

package pwr.smart.home.common.weather.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class Daily {
    private List<String> time;
    private List<Float> temperature_2m_max;
    private List<Float> temperature_2m_min;
    private List<Float> apparent_temperature_max;
    private List<Float> apparent_temperature_min;
    private List<Float> precipitation_hours;
    private List<Integer> weathercode;
}

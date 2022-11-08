package pwr.smart.home.air.humidifier.sensor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Sensor {
    private String serialNumber;
    private String brand;
    private SensorType type;
}

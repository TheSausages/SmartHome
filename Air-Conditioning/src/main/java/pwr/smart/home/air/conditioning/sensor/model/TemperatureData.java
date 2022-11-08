package pwr.smart.home.air.conditioning.sensor.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class TemperatureData {
    private String serialNumber;
    private Timestamp timestamp;
    private SensorType type;
    private double temperature;

    @Override
    public String toString() {
        return "Data{" +
                "serialNumber='" + serialNumber + '\'' +
                ", timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", temperature=" + temperature +
                '}';
    }
}

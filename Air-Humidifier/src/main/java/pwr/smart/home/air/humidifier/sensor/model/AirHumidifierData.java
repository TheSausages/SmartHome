package pwr.smart.home.air.humidifier.sensor.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class AirHumidifierData {
    private String serialNumber;
    private Timestamp timestamp;
    private SensorType type;
    private int humidity;

    @Override
    public String toString() {
        return "Data{" +
                "serialNumber='" + serialNumber + '\'' +
                ", timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", humidity=" + humidity +
                '}';
    }
}

package pwr.smart.home.data.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AirHumidifierData extends SensorDataAbstract{
    private int humidity;

    @Override
    public String toString() {
        return "AirHumidifierData{" +
                "serialNumber='" + getSerialNumber() + '\'' +
                ", timestamp=" + getTimestamp() +
                ", type='" + getType() + '\'' +
                "humidity=" + humidity +
                '}';
    }
}

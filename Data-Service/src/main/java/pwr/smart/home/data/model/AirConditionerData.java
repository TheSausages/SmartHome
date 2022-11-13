package pwr.smart.home.data.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AirConditionerData extends SensorDataAbstract {
    private int temperature;

    @Override
    public String toString() {
        return "AirConditionerData{" +
                "serialNumber='" + getSerialNumber() + '\'' +
                ", timestamp=" + getTimestamp() +
                ", type='" + getType() + '\'' +
                ", temperature=" + temperature +
                '}';
    }
}

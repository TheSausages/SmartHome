package pwr.smart.home.data.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AirFilterData extends SensorDataAbstract {
    private int PM25;
    private int IAI;
    private int gas;

    @Override
    public String toString() {
        return "AirFilterData{" +
                "serialNumber='" + getSerialNumber() + '\'' +
                ", timestamp=" + getTimestamp() +
                ", type='" + getType() + '\'' +
                ", PM25=" + PM25 +
                ", IAI=" + IAI +
                ", gas=" + gas +
                '}';
    }
}

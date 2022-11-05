package pwr.smart.home.data.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pwr.smart.home.common.enums.SensorType;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class AirFilterData {
    private String serialNumber;
    private Timestamp timestamp;
    private SensorType type;
    private int PM25;
    private int IAI;
    private int gas;

    @Override
    public String toString() {
        return "Data{" +
                "serialNumber='" + serialNumber + '\'' +
                ", timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", PM25=" + PM25 +
                ", IAI=" + IAI +
                ", gas=" + gas +
                '}';
    }
}

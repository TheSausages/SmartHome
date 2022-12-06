package pwr.smart.home.air.conditioning.device.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;


@Getter
@Setter
@AllArgsConstructor
public class ConsumptionData {
    private String deviceSerialNumber;
    private double consumption;
    private Timestamp timestamp;

    @Override
    public String toString() {
        return "ConsumptionData{" +
                "deviceSerialNumber='" + deviceSerialNumber + '\'' +
                ", consumption=" + consumption +
                ", timestamp=" + timestamp +
                '}';
    }
}

package pwr.smart.home.data.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class ConsumptionData {
    private String deviceSerialNumber;
    private double consumption;
    private Timestamp timestamp;

    @Override
    public String toString() {
        return "ConsumptionData{"+
                "deviceSerialNumber='" + deviceSerialNumber + '\'' +
                ", consumption=" + consumption +
                '}';
    }
}

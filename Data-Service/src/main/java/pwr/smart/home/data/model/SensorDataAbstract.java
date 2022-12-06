package pwr.smart.home.data.model;

import lombok.Getter;
import lombok.Setter;
import pwr.smart.home.common.model.enums.SensorType;

import java.sql.Timestamp;

@Getter
@Setter
public abstract class SensorDataAbstract {
    private String serialNumber;
    private Timestamp timestamp;
    private SensorType type;
}

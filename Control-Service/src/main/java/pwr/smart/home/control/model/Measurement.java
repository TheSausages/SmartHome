package pwr.smart.home.control.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import pwr.smart.home.common.model.enums.MeasurementType;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Measurement {
    private Long id;
    private MeasurementType type;
    private int value;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp createdAt;
    private Long sensorId;
}

package pwr.smart.home.data.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pwr.smart.home.common.model.enums.MeasurementType;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private MeasurementType type;
    private float value;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp createdAt;
    private Long sensorId;

    public Measurement(MeasurementType type, float value, Long sensorId, Timestamp createdAt) {
        this.type = type;
        this.value = value;
        this.createdAt = createdAt;
        this.sensorId = sensorId;
    }
}

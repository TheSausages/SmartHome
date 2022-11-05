package pwr.smart.home.data.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pwr.smart.home.data.model.enums.MeasurementType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private MeasurementType type;
    private int value;
    private Timestamp createdAt;
    private Long sensorId;

    public Measurement(MeasurementType type, int value, Long sensorId, Timestamp createdAt) {
        this.type = type;
        this.value = value;
        this.createdAt = createdAt;
        this.sensorId = sensorId;
    }
}
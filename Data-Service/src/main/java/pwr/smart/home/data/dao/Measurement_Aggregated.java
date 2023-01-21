package pwr.smart.home.data.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.Hibernate;
import pwr.smart.home.common.model.enums.MeasurementType;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity(name = "MEASUREMENT_AGGREGATED")
public class Measurement_Aggregated {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private MeasurementType type;
    private float value;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp createdAt;
    private Long sensorId;

    public Measurement_Aggregated(MeasurementType type, float value, Long sensorId, Timestamp createdAt) {
        this.type = type;
        this.value = value;
        this.createdAt = createdAt;
        this.sensorId = sensorId;
    }

    public Measurement_Aggregated(MeasurementType type, Double value, Long sensorId, Timestamp createdAt) {
        this.type = type;
        this.value = value.floatValue();
        this.createdAt = createdAt;
        this.sensorId = sensorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Measurement_Aggregated that = (Measurement_Aggregated) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

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
@AllArgsConstructor
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

    public Measurement(MeasurementType type, float value, Timestamp createdAt) {
        this.type = type;
        this.value = value;
        this.createdAt = createdAt;
    }

    public static Measurement fromAggregated(Measurement_Aggregated aggregated) {
        return new Measurement(
                aggregated.getId(),
                aggregated.getType(),
                aggregated.getValue(),
                aggregated.getCreatedAt(),
                aggregated.getSensorId()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Measurement that = (Measurement) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

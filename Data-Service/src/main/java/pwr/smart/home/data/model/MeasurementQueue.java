package pwr.smart.home.data.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.Hibernate;
import pwr.smart.home.common.model.enums.MeasurementType;
import pwr.smart.home.data.dao.Measurement_Aggregated;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class MeasurementQueue {
    private String serialNumber;
    private MeasurementType type;
    private float value;
    private Timestamp createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasurementQueue that = (MeasurementQueue) o;
        return Float.compare(that.value, value) == 0 && Objects.equals(serialNumber, that.serialNumber) && type == that.type && Objects.equals(createdAt, that.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serialNumber, type, value, createdAt);
    }
}

package pwr.smart.home.data.dao;

import pwr.smart.home.data.model.MeasurementsType;

import javax.persistence.*;
import java.sql.Date;

@Entity(name = "measurements")
public class MeasurementDao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private MeasurementsType type;
    private int value;
    private Date created_at;
    private Long sensor_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MeasurementsType getType() {
        return type;
    }

    public void setType(MeasurementsType type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Long getSensor_id() {
        return sensor_id;
    }

    public void setSensor_id(Long sensor_id) {
        this.sensor_id = sensor_id;
    }
}

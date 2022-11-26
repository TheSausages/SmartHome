package pwr.smart.home.data.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pwr.smart.home.data.model.enums.SensorType;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Sensor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private SensorType type;
    private String name;
    private String manufacturer;
    private String serialNumber;
    private Date createdAt;
    private boolean isConnected;
    @Column(name = "home_id")
    private Long homeId;
}

package pwr.smart.home.data.dao;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pwr.smart.home.data.model.enums.DeviceType;
import pwr.smart.home.data.model.enums.SensorType;

import javax.persistence.*;
import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "functional_device")
public class FunctionalDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private DeviceType type;
    private String name;
    private String manufacturer;
    private String serialNumber;
    @Column(name = "consumed_electricity")
    private int consumedElectricity;
    private Date createdAt;
    private boolean isConnected;
    @Column(name = "home_id")
    private Long homeId;
}


package pwr.smart.home.data.dao;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pwr.smart.home.common.model.enums.DeviceType;

import javax.persistence.*;
import java.sql.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "functional_device")
public class FunctionalDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private DeviceType type;
    private String name;
    private String manufacturer;
    private String serialNumber;
    @Transient
    private double averageConsumption;
    private Date createdAt;
    private boolean isConnected;
    @Column(name = "home_id")
    private Long homeId;
    @Column(name = "power_level")
    private int powerLevel;
}


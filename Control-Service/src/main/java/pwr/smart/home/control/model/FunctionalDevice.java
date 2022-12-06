package pwr.smart.home.control.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pwr.smart.home.common.model.enums.DeviceType;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
public class FunctionalDevice {
    private Long id;
    private DeviceType type;
    private String name;
    private String manufacturer;
    private String serialNumber;
    private int consumedElectricity;
    private Date createdAt;
    private boolean isConnected;
    private Long homeId;
}


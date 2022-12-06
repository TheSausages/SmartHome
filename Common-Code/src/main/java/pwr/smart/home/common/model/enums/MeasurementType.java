package pwr.smart.home.common.model.enums;

import java.util.List;

public enum MeasurementType {
    CELSIUS, HUMIDITY, IAI, PM25, GAS;

    public static List<MeasurementType> getMeasurementTypesForDeviceType(DeviceType deviceType) {
        switch (deviceType) {
            case AIR_FILTER: return List.of(IAI, PM25, GAS);
            case AIR_CONDITIONER: return List.of(CELSIUS);
            case AIR_HUMIDIFIER: return List.of(HUMIDITY);
            default: return List.of();
        }
    }
}

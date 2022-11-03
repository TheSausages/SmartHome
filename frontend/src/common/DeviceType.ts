export enum DeviceType {
    AirConditioner,
    Heater,
    AirFilter
};

export enum SensorType {
    AirConditionSensor,
    Thermometer
};

export enum DeviceDestiny {
    Sensor,
    FunctionalDevice
};

export const sensorTypeMapper = (enumValue: SensorType) => {
    if(enumValue === SensorType.AirConditionSensor)
    {
        return "Czujnik jakości powietrza";
    } else {
        return "Termometr";
    }
}

export const deviceNameMapper = (enumValue: DeviceType) => {
    if(enumValue === DeviceType.AirConditioner) {
        return "Klimatyzator"
    } else if (enumValue === DeviceType.Heater) {
        return "Piec grzewczy"
    } else {
        return "Filtr powietrza"
    }
};
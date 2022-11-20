export enum DeviceType {
    AirConditioner,
    Humidifier,
    AirFilter
}

export enum SensorType {
    AirConditionSensor,
    Thermometer
}

export enum DeviceDestiny {
    Sensor,
    FunctionalDevice
}

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
    } else if (enumValue === DeviceType.Humidifier) {
        return "Nawilżacz powietrza"
    } else {
        return "Filtr powietrza"
    }
};
export enum DeviceType {
    AirConditioner = "AIR_CONDITIONER",
    Humidifier = "AIR_HUMIDIFIER",
    AirFilter = "AIR_FILTER"
}

export enum SensorType {
    AirConditionSensor = "AIR_POLLUTION",
    Temperature = "TEMPERATURE",
    AirHumidity = "AIR_HUMIDITY"
}

export enum DeviceDestiny {
    Sensor,
    FunctionalDevice
}

export const sensorTypeMapper = (enumValue: SensorType) => {
    if(enumValue === SensorType.AirConditionSensor)
    {
        return "Czujnik jakości powietrza";
    } else if (enumValue === SensorType.Temperature) {
        return "Czujnik temperatury";
    }
    return "Czujnik nawilżenia powietrza"
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
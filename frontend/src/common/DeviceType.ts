export enum DeviceType {
    AirConditioner,
    Heater,
    AirFilter
};

export const deviceNameMapper = (enumValue: DeviceType) => {
    if(enumValue == DeviceType.AirConditioner) {
        return "Klimatyzator"
    } else if (enumValue == DeviceType.Heater) {
        return "Piec grzewczy"
    } else {
        return "Filtr powietrza"
    }
};
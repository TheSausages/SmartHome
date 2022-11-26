import { SensorType } from "../common/DeviceType";

export enum MeasurementType {
    CELSIUS = "CELSIUS",
    HUMIDITY = "HUMIDITY",
    IAI = "IAI",
    PM25 = "PM25",
    GAS = "GAS"
}

export interface SensorQueryParameters
{
    sensorSerialNumber: string,
    startDate: string,
    endDate: string
}

export interface Measurement {
    id: number;
    type: MeasurementType;
    value: number;
    createdAt: Date;
    sensorId: number;
}

export interface SensorInfo
{
    serialNumber: string,
    type: SensorType,
    name: string,
    connected: boolean
}

export interface SensorAdder
{
    serialNumber: string,
    type: SensorType,
    name: string,
    manufacturer: string
}
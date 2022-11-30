import { DeviceType } from "../common/DeviceType";

export interface FunctionalDeviceInfo
{
    serialNumber: string,
    type: DeviceType,
    name: string,
    connected: boolean
}

export interface FunctionalDeviceAdder
{
    serialNumber: string,
    type: DeviceType,
    name: string,
    manufacturer: string,
    averageConsumptionPerHour: number
}
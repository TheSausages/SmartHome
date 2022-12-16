import { DeviceType } from "../common/DeviceType";

export interface FunctionalDeviceInfo
{
    serialNumber: string,
    type: DeviceType,
    name: string,
    connected: boolean,
    powerLevel: number,
    averageConsumption: number,
    manufacturer: string,
    id: number
}

export interface FunctionalDeviceAdder
{
    serialNumber: string,
    type: DeviceType,
    name: string,
    manufacturer: string,
    powerLevel: number;
}

export interface FunctionalDeviceEditor
{
    serialNumber: string,
    type: DeviceType,
    name: string,
    powerLevel: number,
    manufacturer: string,
    id: number
}
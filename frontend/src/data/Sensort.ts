export enum MeasurementType {
    CELSIUS,
    HUMIDITY,
    IAI,
    PM25,
    GAS
}

export interface Measurement {
    id: number;
    type: MeasurementType;
    value: number;
    createdAt: Date;
    sensorId: number;
}
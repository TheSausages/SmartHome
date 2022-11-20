import {Button, Input, InputLabel, MenuItem, Select} from '@mui/material';
import {Box, Stack} from '@mui/system';
import React, {useState} from 'react';
import {DeviceDestiny, deviceNameMapper, DeviceType, SensorType, sensorTypeMapper} from '../../../common/DeviceType';

export interface DeviceProps
{
    deviceDestiny?: DeviceDestiny
}

export default function DeviceAdder(props: DeviceProps) {
    const [ deviceDestiny, setDeviceDestiny ] = useState<DeviceDestiny>(props.deviceDestiny != null ? props.deviceDestiny : DeviceDestiny.Sensor);
    const [ sensorType, setSensorType ] = useState<SensorType>(SensorType.AirConditionSensor);
    const [ deviceType, setDeviceType ] = useState<DeviceType>(DeviceType.AirConditioner);
    const [ name, setName ] = useState<string>('');
    const [ manufacturer, setManufacturer ] = useState<string>('');
    const [ serialNumber, setSerialNumber ] = useState<string>('');
    const [ averageConsumptionPerHour, setAverageConsumptionPerHour ] = useState<number>(0);

    const handleOnDeviceDestinyChange = (e: any) => setDeviceDestiny(e.target.value);
    const handleOnDeviceTypeChange = (e: any) => setDeviceType(e.target.value);
    const handleOnSensorTypeChange = (e: any) => setSensorType(e.target.value);
    const handleOnNameChange = (e: any) => setName(e.target.value);
    const handleOnManufacturerChange = (e: any) => setManufacturer(e.target.value);
    const handleOnSerialNumberChange = (e: any) => setSerialNumber(e.target.value);
    const handleOnAverageConsumptionPerHourChange = (e: any) => setAverageConsumptionPerHour(parseInt(e.target.value));

    const handleResetButton = () => {
        setDeviceDestiny(DeviceDestiny.Sensor);
        setSensorType(SensorType.AirConditionSensor);
        setDeviceType(DeviceType.AirConditioner);
        setName('');
        setManufacturer('');
        setSerialNumber('');
        setAverageConsumptionPerHour(0);
    };

    const handleOnSubmit = (e: any) => {
        e.preventDefault();
    }

    const deviceTypeSelect = deviceDestiny === DeviceDestiny.Sensor ?
    (
    <>
        <InputLabel sx={{display:'inline-block', width:"10%"}}>Typ czujnika:</InputLabel>
        <Select sx={{width: '250px'}}value={sensorType} onChange={handleOnSensorTypeChange}>
            <MenuItem value={SensorType.AirConditionSensor}>{sensorTypeMapper(SensorType.AirConditionSensor)}</MenuItem>
            <MenuItem value={SensorType.Thermometer}>{sensorTypeMapper(SensorType.Thermometer)}</MenuItem>
        </Select>
    </>
    )
    :
    (
    <>
        <InputLabel sx={{display:'inline-block', width:"10%"}}>Typ urządzenia:</InputLabel>
        <Select sx={{width: '250px'}} value={deviceType} onChange={handleOnDeviceTypeChange}>
            <MenuItem value={DeviceType.AirConditioner}>{deviceNameMapper(DeviceType.AirConditioner)}</MenuItem>
            <MenuItem value={DeviceType.AirFilter}>{deviceNameMapper(DeviceType.AirFilter)}</MenuItem>
            <MenuItem value={DeviceType.Humidifier}>{deviceNameMapper(DeviceType.Humidifier)}</MenuItem>
        </Select>
    </>
    );

    const averageConsumption = (deviceDestiny === DeviceDestiny.FunctionalDevice &&
        <Box sx={{marginTop: '20px'}}>
            <InputLabel sx={{display:'inline-block', width:"10%"}}>Średnie zużycie:</InputLabel>
            <Input value={averageConsumptionPerHour} onChange={handleOnAverageConsumptionPerHourChange} type="number"/>
        </Box>);

    return (
        <Stack direction="column" sx={{marginTop: '50px'}}>
            <form method="post" onSubmit={handleOnSubmit}>
                <Box>
                    <InputLabel sx={{display:'inline-block', width:"10%"}}>Typ urządzenia:</InputLabel>
                    <Select sx={{width:'250px'}}value={deviceDestiny} onChange={handleOnDeviceDestinyChange}>
                        <MenuItem value={DeviceDestiny.Sensor}>Sensor</MenuItem>
                        <MenuItem value={DeviceDestiny.FunctionalDevice}>Urządzenie funkcjonalne</MenuItem>
                    </Select>
                </Box>
                <Box>
                    {deviceTypeSelect}
                </Box>
                <Box sx={{marginTop: '20px'}}>
                    <InputLabel sx={{display:'inline-block', width:"10%"}}>Nazwa:</InputLabel>
                    <Input value={name} onChange={handleOnNameChange} type="text"/>
                </Box>
                <Box sx={{marginTop: '20px'}}>
                    <InputLabel sx={{display:'inline-block', width:"10%"}}>Producent:</InputLabel>
                    <Input value={manufacturer} onChange={handleOnManufacturerChange} type="text"/>
                </Box>
                <Box sx={{marginTop: '20px'}}>
                    <InputLabel sx={{display:'inline-block', width:"10%"}}>Numer seryjny:</InputLabel>
                    <Input value={serialNumber} onChange={handleOnSerialNumberChange} type="text"/>
                </Box>
                {averageConsumption}
                <Box sx={{marginTop: '30px'}}>
                    <Button color="warning" variant="contained" sx={{marginRight: '10px'}} onClick={handleResetButton}>Anuluj</Button>
                    <Button color="success" variant="contained" type="submit">Dodaj</Button>
                </Box>
            </form>
        </Stack>
    )
}
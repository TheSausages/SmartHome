import {Button, Input, InputLabel, MenuItem, Select} from '@mui/material';
import {Box, Stack} from '@mui/system';
import React, {useState} from 'react';
import { useMutation } from 'react-query';
import {DeviceDestiny, deviceNameMapper, DeviceType, SensorType, sensorTypeMapper} from '../../../common/DeviceType';
import { addNewFunctionalDevice, addNewSensor } from '../../../common/RequestHelper/RequestHelper';
import { useNavigate } from 'react-router-dom';
import { settings_path } from '../../../common/Paths';

export interface DeviceProps
{
    deviceDestiny?: DeviceDestiny
}

export default function DeviceAdder(props: DeviceProps) {
    const [ deviceDestiny, setDeviceDestiny ] = useState<DeviceDestiny>(props.deviceDestiny != null ? props.deviceDestiny : DeviceDestiny.Sensor);
    const [ sensorType, setSensorType ] = useState<SensorType>(SensorType.AirConditionSensor);
    const [ deviceType, setDeviceType ] = useState<DeviceType>(DeviceType.AirConditioner);
    const [ powerLevel, setPowerLevel ] = useState<number>(2);
    const [ name, setName ] = useState<string>('');
    const [ manufacturer, setManufacturer ] = useState<string>('');
    const [ serialNumber, setSerialNumber ] = useState<string>('');
    const addFunctionalDevice = useMutation(addNewFunctionalDevice);
    const addSensor = useMutation(addNewSensor);
    const navigator = useNavigate();

    const handleOnDeviceDestinyChange = (e: any) => setDeviceDestiny(e.target.value);
    const handleOnDeviceTypeChange = (e: any) => setDeviceType(e.target.value);
    const handleOnSensorTypeChange = (e: any) => setSensorType(e.target.value);
    const handleOnNameChange = (e: any) => setName(e.target.value);
    const handleOnManufacturerChange = (e: any) => setManufacturer(e.target.value);
    const handleOnSerialNumberChange = (e: any) => setSerialNumber(e.target.value);
    const handleOnPowerLevelChange = (e: any) => setPowerLevel(parseInt(e.target.value));

    const handleResetButton = () => {
        setDeviceDestiny(DeviceDestiny.Sensor);
        setSensorType(SensorType.AirConditionSensor);
        setDeviceType(DeviceType.AirConditioner);
        setName('');
        setManufacturer('');
        setSerialNumber('');
        setPowerLevel(2)
        navigator(settings_path);
    };

    const handleOnSubmit = (e: any) => {
        e.preventDefault();
        if(deviceDestiny == DeviceDestiny.Sensor)
        {
            addSensor.mutate({serialNumber: serialNumber, type: sensorType, name: name, manufacturer: manufacturer}, {onSuccess: (response: any) => {
                console.log(response);
                navigator(settings_path);
            }});
        } else {
            addFunctionalDevice.mutate({serialNumber: serialNumber, type: deviceType, name: name, manufacturer: manufacturer, powerLevel: powerLevel}, {onSuccess: (response: any) => {
                console.log(response);
                navigator(settings_path);
            }});
        }

    }

    const deviceTypeSelect = deviceDestiny === DeviceDestiny.Sensor ?
    (
    <>
        <InputLabel sx={{display:'inline-block', width:"10%"}}>Typ czujnika:</InputLabel>
        <Select sx={{width: '250px'}} value={sensorType} onChange={handleOnSensorTypeChange}>
            <MenuItem value={SensorType.AirConditionSensor}>{sensorTypeMapper(SensorType.AirConditionSensor)}</MenuItem>
            <MenuItem value={SensorType.Temperature}>{sensorTypeMapper(SensorType.Temperature)}</MenuItem>
            <MenuItem value={SensorType.AirHumidity}>{sensorTypeMapper(SensorType.AirHumidity)}</MenuItem>
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

    const powerLevelBlock = (deviceDestiny === DeviceDestiny.FunctionalDevice &&
        <Box sx={{marginTop: '20px'}}>
            <InputLabel sx={{display:'inline-block', width:"10%"}}>Poziom mocy</InputLabel>
            <Select sx={{width: '250px'}} value={powerLevel} onChange={handleOnPowerLevelChange}>
                <MenuItem value={1}>1</MenuItem>
                <MenuItem value={2}>2</MenuItem>
                <MenuItem value={3}>3</MenuItem>
            </Select>
        </Box>);

    return (
        <Stack direction="column" sx={{marginTop: '50px'}}>
            <form method="post" onSubmit={handleOnSubmit}>
                <Box>
                    <InputLabel sx={{display:'inline-block', width:"10%"}}>Typ urządzenia:</InputLabel>
                    <Select sx={{width:'250px'}} value={deviceDestiny} onChange={handleOnDeviceDestinyChange}>
                        <MenuItem value={DeviceDestiny.Sensor}>Sensor</MenuItem>
                        <MenuItem value={DeviceDestiny.FunctionalDevice}>Urządzenie funkcjonalne</MenuItem>
                    </Select>
                </Box>
                <Box sx={{marginTop: '20px'}}>
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
                {powerLevelBlock}
                <Box sx={{marginTop: '30px'}}>
                    <Button color="warning" variant="contained" sx={{marginRight: '10px'}} onClick={handleResetButton}>Anuluj</Button>
                    <Button color="success" variant="contained" type="submit">Dodaj</Button>
                </Box>
            </form>
        </Stack>
    )
}
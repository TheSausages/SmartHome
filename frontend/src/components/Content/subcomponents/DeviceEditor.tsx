import {Button, Dialog, DialogTitle, Input, InputLabel, MenuItem, Select} from '@mui/material';
import {Box, Stack} from '@mui/system';
import React, {Dispatch, SetStateAction, useState} from 'react';
import { useMutation } from 'react-query';
import {DeviceDestiny, deviceNameMapper, DeviceType, SensorType, sensorTypeMapper} from '../../../common/DeviceType';
import {
    addNewFunctionalDevice,
    addNewSensor,
    updateFunctionalDevice
} from '../../../common/RequestHelper/RequestHelper';
import { useNavigate } from 'react-router-dom';
import { settings_path } from '../../../common/Paths';
import {FunctionalDeviceEditor} from "../../../data/FunctionalDevices";

export interface DeviceProps
{
    device: FunctionalDeviceEditor,
    setOpen: Dispatch<SetStateAction<boolean>>,
    open: boolean,
    refreshItems: () => void
}

export default function DeviceEditor(props: DeviceProps) {
    const [ deviceType, setDeviceType ] = useState<DeviceType>(props.device.type);
    const [ powerLevel, setPowerLevel ] = useState<number>(props.device.powerLevel);
    const [ name, setName ] = useState<string>(props.device.name);
    const [ manufacturer, setManufacturer ] = useState<string>(props.device.manufacturer);
    const [ serialNumber, setSerialNumber ] = useState<string>(props.device.serialNumber);
    const addFunctionalDevice = useMutation(updateFunctionalDevice);
    const navigator = useNavigate();

    const handleOnDeviceTypeChange = (e: any) => {
        e.preventDefault()

        setDeviceType(e.target.value)
    };
    const handleOnNameChange = (e: any) => {
        e.preventDefault()

        setName(e.target.value)
    };
    const handleOnManufacturerChange = (e: any) => {
        e.preventDefault()

        setManufacturer(e.target.value)
    };
    const handleOnSerialNumberChange = (e: any) => {
        e.preventDefault()

        setSerialNumber(e.target.value)
    };
    const handleOnPowerLevelChange = (e: any) => {
        e.preventDefault()

        setPowerLevel(parseInt(e.target.value))
    };

    const handleOnSubmit = (e: any) => {
        e.preventDefault();

        console.log('submit')

        addFunctionalDevice.mutate({serialNumber: serialNumber, type: deviceType, name: name, manufacturer: manufacturer, powerLevel: powerLevel, id: props.device.id}, {onSuccess: (response: any) => {
                console.log(response);
                navigator(settings_path);
            }});

        props.setOpen(false)

        props.refreshItems()
    }

    return (
        <Dialog open={props.open} fullWidth={true} PaperProps={{sx: {alignItems: "center"}}}>
            <DialogTitle>Edycja Urządzenia</DialogTitle>
            <form method="post" onSubmit={handleOnSubmit} style={{width: "80%"}}>
                <Box>
                    <InputLabel sx={{display:'inline-block', width:"33%"}}>Typ urządzenia:</InputLabel>
                    <Select sx={{width: '250px'}} value={deviceType} onChange={handleOnDeviceTypeChange}>
                        <MenuItem value={DeviceType.AirConditioner}>{deviceNameMapper(DeviceType.AirConditioner)}</MenuItem>
                        <MenuItem value={DeviceType.AirFilter}>{deviceNameMapper(DeviceType.AirFilter)}</MenuItem>
                        <MenuItem value={DeviceType.Humidifier}>{deviceNameMapper(DeviceType.Humidifier)}</MenuItem>
                    </Select>
                </Box>
                <Box sx={{marginTop: '20px'}}>
                    <InputLabel sx={{display:'inline-block', width:"33%"}}>Nazwa:</InputLabel>
                    <Input value={name} onChange={handleOnNameChange} type="text"/>
                </Box>
                <Box sx={{marginTop: '20px'}}>
                    <InputLabel sx={{display:'inline-block', width:"33%"}}>Producent:</InputLabel>
                    <Input value={manufacturer} onChange={handleOnManufacturerChange} type="text"/>
                </Box>
                <Box sx={{marginTop: '20px'}}>
                    <InputLabel sx={{display:'inline-block', width:"33%"}}>Numer seryjny:</InputLabel>
                    <Input value={serialNumber} onChange={handleOnSerialNumberChange} type="text"/>
                </Box>
                <Box sx={{marginTop: '20px'}}>
                    <InputLabel sx={{display:'inline-block', width:"33%"}}>Poziom mocy</InputLabel>
                    <Select id={"Select-Power-Level"} sx={{width: '250px'}} value={powerLevel} onChange={handleOnPowerLevelChange}>
                        <MenuItem id={"Select-Power-Level-1"} value={1}>1</MenuItem>
                        <MenuItem id={"Select-Power-Level-2"} value={2}>2</MenuItem>
                        <MenuItem id={"Select-Power-Level-3"} value={3}>3</MenuItem>
                    </Select>
                </Box>
                <Box  sx={{marginBlock: '30px', justifyContent: "center", display: "flex"}}>
                    <Button color="warning" variant="contained" sx={{marginRight: '10px'}} onClick={() => props.setOpen(false)}>Anuluj</Button>
                    <Button id={"Device-Edit-Submit"} color="success" variant="contained" type="submit">Aktualizuj</Button>
                </Box>
            </form>
        </Dialog>
    )
}
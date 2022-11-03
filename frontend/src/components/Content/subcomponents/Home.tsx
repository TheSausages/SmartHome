import React, {useState} from 'react';

import { Box, Button, Grid, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Select, MenuItem, SelectChangeEvent } from "@mui/material";
import WbSunnyIcon from '@mui/icons-material/WbSunny'
import NightlightIcon from '@mui/icons-material/Nightlight';
import FiberManualRecordOutlinedIcon from '@mui/icons-material/FiberManualRecordOutlined';
import FiberManualRecordIcon from '@mui/icons-material/FiberManualRecord';
import ThermostatIcon from '@mui/icons-material/Thermostat';
import BloodtypeIcon from '@mui/icons-material/Bloodtype';
import { DeviceType, deviceNameMapper } from '../../../common/DeviceType';

export interface HomeProps
{
}

type DeviceParameter = {
    name: string,
    value: number
};

export default function Home(props: HomeProps) {
    const [ activityHour, setActivityHour ] = useState<number>(-1);
    const [ requestedTemperature, setRequestedTemperature ] = useState<number>(17);
    const [ requestedAirHumadity, setRequestedAirHumadity ] = useState<number>(40);
    const [ activeDevice, setActiveDevice ] = useState<DeviceType>(DeviceType.AirConditioner);
    const [ deviceParameters, setDeviceParameters ] = useState<DeviceParameter[]>([{name: 'PM 2.5', value: 5}, {name: 'PM 10', value: 23}]);

    var hoursArray:number[] = [];
    var availableTemperatures:number[] = [];
    var availableAirHumadityLevels:number[] = [];
    for(let i = 0; i < 24; i++) {
        hoursArray.push(i);
    }
    for(let i = 0; i <= 30; i++) {
        availableTemperatures.push(i);
    }
    for(let i = 30; i <= 70; i++ ) {
        availableAirHumadityLevels.push(i);
    }

    const handleActivityHourChange = (value: number) => {
        if(activityHour === value) {
            setActivityHour(-1);
        } else {
            setActivityHour(value);
        }
    };

    const handleOnTemperatureChange = (e: SelectChangeEvent) => {
        setRequestedTemperature(parseInt(e.target.value));
    };

    const handleOnAirHumadityChange = (e: SelectChangeEvent) => {
        setRequestedAirHumadity(parseInt(e.target.value));
    };

    const changeActiveDevice = (device: DeviceType) => {
        setActiveDevice(device);
    }

    const deviceParameterRows = deviceParameters.map((item, index) => (
        <TableRow sx={{backgroundColor: 'green'}} key={index}>
            <TableCell>{item.name}</TableCell>
            <TableCell>{item.value}</TableCell>
        </TableRow>
    ));

    

    const hoursList = hoursArray.map((item, index) => (
        <Grid item xs={1} key={index}>
            <Button sx={{border: 1, borderRadius: '5px', borderColor: '#bfbdbd', color:'black'}} 
                onClick={() => handleActivityHourChange(item)} variant={activityHour === item ? "contained" : "outlined"}
                color={activityHour === item ? "primary" : "secondary"}>
                    {item < 10 ? '0'+ item : item}:00
            </Button>
        </Grid>
    ));

    const temperatureOptions = availableTemperatures.map((item, index) => (
        <MenuItem value={item} key={index}>{item}°C</MenuItem>
    ));

    const airHumadityOptions = availableAirHumadityLevels.map((item, index) => (
        <MenuItem value={item} key={index}>{item}%</MenuItem>
    ))


    return (
        <Grid container spacing={2} sx={{marginTop: '20px'}}>
            <Grid item xs={1}>
                <Box sx={{marginTop: '50px', marginLeft: '10px'}}>
                    <h3>Pogoda</h3>
                    <Box sx={{border: 1, borderRadius: '10px', margin: '20px'}}>
                        <span>Dziś</span>
                        <br/>
                        <br/>
                        <WbSunnyIcon/>
                        <p>17°C</p>
                        <NightlightIcon/>
                        <p>9°C</p>

                    </Box>
                    <Box sx={{border: 1, borderRadius: '10px', margin: '20px'}}>
                        <span>Jutro</span>
                        <br/>
                        <br/>
                        <WbSunnyIcon/>
                        <p>17°C</p>
                        <NightlightIcon/>
                        <p>9°C</p>

                    </Box>
                </Box>
            </Grid>
            <Grid item xs={6} sx={{marginLeft: '30px'}}>
                <Box sx={{border: 1, minHeight: '200px', marginLeft: '10px', marginRight: '10px'}}>
                    <p>Coś tam</p>
                </Box>
                <TableContainer component={Paper} sx={{marginTop: '30px'}}>
                <Table sx={{minWidth: 250}} aria-label='simple table'>
                    <TableHead>
                        <TableRow>
                            <TableCell>Czujnik</TableCell>
                            <TableCell>Odczyt</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        <TableRow>
                            <TableCell>Temperatura</TableCell>
                            <TableCell>17°C</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell>Wilgotność</TableCell>
                            <TableCell>45%</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell>Jakość powietrza</TableCell>
                            <TableCell>Dobra</TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
                </TableContainer>

            </Grid>
            <Grid item xs={4} sx={{marginLeft: '30px'}}>
                <Box sx={{minHeight: '200px', marginLeft: '10px', marginRight: '10px'}}>
                    <p><b>Urządzenia</b></p>
                    <TableContainer component={Paper} sx={{marginTop: '20px'}}>
                        <Table sx={{minWidth: 100}} aria-label='simple table'>
                            <TableHead>
                                <TableRow>
                                    <TableCell><b>{deviceNameMapper(activeDevice)}</b></TableCell>
                                    <TableCell></TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {deviceParameterRows}
                            </TableBody>
                        </Table>
                    </TableContainer>
                    <div>
                        {activeDevice === DeviceType.AirConditioner ? <FiberManualRecordIcon/> : <FiberManualRecordOutlinedIcon onClick={() => changeActiveDevice(DeviceType.AirConditioner)}/>}
                        {activeDevice === DeviceType.Heater ? <FiberManualRecordIcon/> : <FiberManualRecordOutlinedIcon onClick={() => changeActiveDevice(DeviceType.Heater)}/>}
                        {activeDevice === DeviceType.AirFilter ? <FiberManualRecordIcon/> : <FiberManualRecordOutlinedIcon onClick={() => changeActiveDevice(DeviceType.AirFilter)}/>}
                    </div>
                </Box>
                <Box sx={{marginLeft: '10px', marginRight: '10px'}}>
                    <p><b>Preferencje domowników</b></p>
                    <Grid container spacing={2}>
                        <Grid item xs={6}>
                            <p>Temperatura</p>
                            <ThermostatIcon fontSize="large"/>
                            <Box>
                                <Select value={`${requestedTemperature}`} onChange={handleOnTemperatureChange}>
                                    {temperatureOptions}
                                </Select>
                            </Box>
                        </Grid>
                        <Grid item xs={6}>
                            <p>Wilgotność powietrza</p>
                            <BloodtypeIcon fontSize="large"/>
                            <Box>
                                <Select value={`${requestedAirHumadity}`} onChange={handleOnAirHumadityChange}>
                                    {airHumadityOptions}
                                </Select>
                            </Box>
                        </Grid>
                        <Grid item xs={12} sx={{minHeight: '300px'}}>
                            <p>Godziny aktywności</p>
                            <Grid container columnSpacing={1} rowSpacing={1} columns={6}>
                                {hoursList}
                            </Grid>
                        </Grid>
                    </Grid>
                </Box>
            </Grid>
        </Grid>
    )
}
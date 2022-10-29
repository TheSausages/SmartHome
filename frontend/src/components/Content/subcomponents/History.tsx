import React from 'react';

import { Grid, Paper, Box, Button, TextField, styled } from "@mui/material";
import { Stack } from "@mui/system";
import FiberManualRecordOutlinedIcon from '@mui/icons-material/FiberManualRecordOutlined';
import FiberManualRecordIcon from '@mui/icons-material/FiberManualRecord';
import { useState } from "react";
import { Input } from '@mui/material';
import { DeviceType, deviceNameMapper } from '../../../common/DeviceType';

export interface HistoryProps
{
}



export default function History(props: HistoryProps) {
    const [ startDate, setStartDate] = useState<string>('');
    const [ endDate, setEndDate ] = useState<string>('');
    const [ activeDevice, setActiveDevice ] = useState<DeviceType>(DeviceType.AirConditioner);

    const handleOnStartDateChange = (e: any) => {
        setStartDate(e.target.value);
    };

    const handleOnEndDateChange = (e: any) => {
        setEndDate(e.target.value);
    }

    const changeActiveDevice = (device: DeviceType) => {
        setActiveDevice(device);
    }

    return (
        <Box sx={{marginTop: '20px'}}>
        <Grid container spacing={2}>
            <Grid item xs={2} />
            <Grid item xs={8}>
                <h3>{deviceNameMapper(activeDevice)}</h3>
                <Stack direction="row">
                        <p style={{marginLeft: '20px', marginRight: '20px'}}>Od:</p>
                        <Input type="date" value={startDate} onChange={handleOnStartDateChange} />
                        <p style={{marginLeft: '20px', marginRight: '20px'}}>Do:</p>
                        <Input type="date" value={endDate} onChange={handleOnEndDateChange} />
                </Stack>
                <br/>
                <br/>
                <Box sx={{border: 1, minHeight: '200px', marginLeft: '10px', marginRight: '10px'}}>
                    <p>Coś tam</p>
                </Box>
                <Box sx={{border: 1, minHeight: '200px', marginLeft: '10px', marginRight: '10px', marginTop:'10px'}}>
                    <p>Coś tam</p>
                </Box>
                {activeDevice === DeviceType.AirConditioner ? <FiberManualRecordIcon/> : <FiberManualRecordOutlinedIcon onClick={() => changeActiveDevice(DeviceType.AirConditioner)}/>}
                {activeDevice === DeviceType.Heater ? <FiberManualRecordIcon/> : <FiberManualRecordOutlinedIcon onClick={() => changeActiveDevice(DeviceType.Heater)}/>}
                {activeDevice === DeviceType.AirFilter ? <FiberManualRecordIcon/> : <FiberManualRecordOutlinedIcon onClick={() => changeActiveDevice(DeviceType.AirFilter)}/>}
            </Grid>
        </Grid>
        </Box>
    )
}
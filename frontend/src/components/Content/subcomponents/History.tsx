import React, {useState} from 'react';

import {Box, Grid, Input, Button} from "@mui/material";
import {Stack} from "@mui/system";
import FiberManualRecordOutlinedIcon from '@mui/icons-material/FiberManualRecordOutlined';
import FiberManualRecordIcon from '@mui/icons-material/FiberManualRecord';
import {deviceNameMapper, DeviceType} from '../../../common/DeviceType';
import { useMutation } from 'react-query';
import { getSensorMeasurementFromDateToDate } from '../../../common/RequestHelper/RequestHelper';
import { Measurement, MeasurementType } from '../../../data/Sensort';
import { ApiError } from '../../../data/ApiError';
import { LineChart, XAxis, YAxis, Line, Legend, Tooltip } from 'recharts';
import moment from 'moment';

export interface HistoryProps
{
}

const supportedMeasurement: MeasurementType[] = [ MeasurementType.GAS, MeasurementType.IAI, MeasurementType.PM25]


export default function History(props: HistoryProps) {
    const [ startDate, setStartDate] = useState<string>('');
    const [ endDate, setEndDate ] = useState<string>('');
    const [ activeDevice, setActiveDevice ] = useState<DeviceType>(DeviceType.AirConditioner);
    const [ activeSensorId, setActiveSensorId ] = useState<string>("HIBWCDUIYHWASDAE");
    const [ receivedData, setReceivedData ] = useState<Measurement[]>([]);
    const parameterQuery = useMutation(getSensorMeasurementFromDateToDate);

    const handleOnStartDateChange = (e: any) => {
        setStartDate(e.target.value);
    };

    const handleOnEndDateChange = (e: any) => {
        setEndDate(e.target.value);
    }

    const handleOnSensorIdChange = (device: DeviceType) => {
        if (device === DeviceType.AirConditioner)
        {
            setActiveSensorId("HIBWCDUIYHWASDAE");
            setReceivedData([]);
            return;
        } else if (device === DeviceType.AirFilter)
        {
            setActiveSensorId("HIBWCDUIYHWASDAD");
            setReceivedData([]);
            return;
        }
        setActiveSensorId("HIBWCDUIYHWASDAF");
        setReceivedData([]);
    }

    const changeActiveDevice = (device: DeviceType) => {
        setActiveDevice(device);
        handleOnSensorIdChange(device);
    }

    const handleOnSubmit = (e: any) => 
    {
        e.preventDefault();
        if(startDate === '' || endDate === '')
        {
            alert("Pola z datą nie mogą być puste");
            return;
        }
        parameterQuery.mutate({sensorSerialNumber: activeSensorId, startDate: startDate, endDate: endDate}, {onSuccess: (response: Measurement[]) => {
            console.log(response.sort((first: Measurement, second:Measurement) => (first.createdAt > second.createdAt ? 1 : -1)));
            setReceivedData(response.sort((first: Measurement, second:Measurement) => (first.createdAt > second.createdAt ? 1 : -1)));
        }})
    }

    const linearChart = activeDevice === DeviceType.AirFilter ?
    (
    <>
        {supportedMeasurement.map((item: MeasurementType) => (
    <LineChart width={700} height={300} key={item}>
        <Tooltip />
        <XAxis dataKey="createdAt" tickFormatter={(value: Date) => (value ? moment(value).format('MM-DD-YYYY hh:mm') : value)} minTickGap={15}/>
        <YAxis dataKey="value"/>
        <Legend/>
        <Line name={item} type="monotone" dataKey="value" stroke="#121212" data={receivedData.filter((value: Measurement) => (value.type === item))} />
    </LineChart>
        ))}
    </>
    ) :
    (
    <LineChart width={700} height={300} data={receivedData}>
        <XAxis dataKey="createdAt" tickFormatter={(value: Date) => (value ? moment(value).format('MM-DD-YYYY hh:mm') : value)} minTickGap={15}/>
        <YAxis dataKey="value"/>
        <Line type="monotone" dataKey="value" stroke="#8884d8"/>
    </LineChart>);

    return (
        <Box sx={{marginTop: '20px'}}>
        <Grid container spacing={2}>
            <Grid item xs={2} />
            <Grid item xs={8}>
                <h3>{deviceNameMapper(activeDevice)}</h3>
                    <form method="get" onSubmit={handleOnSubmit}>
                <Stack direction="row">
                        <p style={{marginLeft: '20px', marginRight: '20px'}}>Od:</p>
                        <Input type="date" value={startDate} onChange={handleOnStartDateChange} />
                        <p style={{marginLeft: '20px', marginRight: '20px'}}>Do:</p>
                        <Input type="date" value={endDate} onChange={handleOnEndDateChange} />
                        <Button sx={{marginLeft: '20px', height:'20px', marginTop:'20px', backgroundColor: 'rgba(76, 175, 80, 0.7)'}} variant="contained" type="submit">Wyświetl</Button>
                </Stack>
                    </form>
                <br/>
                <br/>
                <>
                {receivedData.length > 0 && linearChart}
                </>
                {activeDevice === DeviceType.AirConditioner ? <FiberManualRecordIcon/> : <FiberManualRecordOutlinedIcon onClick={() => changeActiveDevice(DeviceType.AirConditioner)}/>}
                {activeDevice === DeviceType.Humidifier ? <FiberManualRecordIcon/> : <FiberManualRecordOutlinedIcon onClick={() => changeActiveDevice(DeviceType.Humidifier)}/>}
                {activeDevice === DeviceType.AirFilter ? <FiberManualRecordIcon/> : <FiberManualRecordOutlinedIcon onClick={() => changeActiveDevice(DeviceType.AirFilter)}/>}
            </Grid>
        </Grid>
        </Box>
    )
}
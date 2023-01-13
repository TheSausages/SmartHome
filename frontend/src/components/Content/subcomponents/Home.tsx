import React, {useEffect, useState} from 'react';

import {
    Box,
    Button,
    Grid,
    MenuItem,
    Paper,
    Select,
    SelectChangeEvent,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow
} from "@mui/material";
import WbSunnyIcon from '@mui/icons-material/WbSunny'
import NightlightIcon from '@mui/icons-material/Nightlight';
import FiberManualRecordOutlinedIcon from '@mui/icons-material/FiberManualRecordOutlined';
import FiberManualRecordIcon from '@mui/icons-material/FiberManualRecord';
import ThermostatIcon from '@mui/icons-material/Thermostat';
import BloodtypeIcon from '@mui/icons-material/Bloodtype';
import {deviceNameMapper, DeviceType} from '../../../common/DeviceType';
import {
    getLastHumidifierFilterMeasurement,
    getLastAirFilterMeasurement,
    getLastAirConditionerMeasurement,
    setHomeTemperature,
    setHomeHumidity,
    addHomeActivityHour,
    deleteHomeActivityHour,
    getHomeActivityHours,
    getWeatherInfo,
    runAgainWithTemperature, runAgainWithHumidity, getHomeInfo
} from "../../../common/RequestHelper/RequestHelper";
import {useQueries, useMutation} from "react-query";
import { Measurement, MeasurementType } from '../../../data/Sensort';
import { ForecastWeather } from '../../../data/Weather';
import {HomeInfo} from "../../../data/HomeInfo";

export interface HomeProps
{
}

enum AirCondition
{
    Bad,
    Acceptable,
    Good
}

export default function Home(props: HomeProps) {
    const [ activityHours, setActivityHours ] = useState<number[]>([]);
    const [ requestedTemperature, setRequestedTemperature ] = useState<number>(17);
    const [ requestedAirHumadity, setRequestedAirHumadity ] = useState<number>(40);
    const [ activeDevice, setActiveDevice ] = useState<DeviceType>(DeviceType.AirConditioner);
    const [ airConditionParameters, setAirConditionParameters ] = useState<Measurement[]>([]);
    const [ todayMaxTemperature, setTodayMaxTemperature ] = useState<number>(0);
    const [ todayMinTemperature, setTodayMinTemperature ] = useState<number>(0);
    const [ tomorrowMaxTemperature, setTomorrowMaxTemperature ] = useState<number>(0);
    const [ tomorrowMinTemperature, setTomorrowMinTemperature ] = useState<number>(0);
    const [ temperature, setTemperature ] = useState<number>(0);
    const [ humidity, setHumidity ] = useState<number>(0);

    const setHomeTemperatureRequest = useMutation(setHomeTemperature);
    const setHomeHumidityRequest = useMutation(setHomeHumidity);
    const runAgainWithTemperatureRequest = useMutation(runAgainWithTemperature);
    const runAgainWithHumidityRequest = useMutation(runAgainWithHumidity);
    const addHomeActivityHourRequest = useMutation(addHomeActivityHour);
    const deleteHomeActivityHourRequest = useMutation(deleteHomeActivityHour);

    const [forecastWeatherQuery, airConditionQuery, temperatureQuery, humidityQuery, getActivityHours, getHomeInformation] = useQueries([
            {
                queryKey: ['GetWeatherInfo'],
                queryFn: (() => getWeatherInfo()),
                onSuccess: (data: ForecastWeather) => {
                    setTodayMaxTemperature(data.daily.temperature_2m_max[0])
                    setTomorrowMaxTemperature(data.daily.temperature_2m_max[1])
                    setTodayMinTemperature(data.daily.temperature_2m_min[0])
                    setTomorrowMinTemperature(data.daily.temperature_2m_min[1])
                }
            },
            {
                queryKey: ['AirFilterQuery'],
                queryFn: (() => {
                    return getLastAirFilterMeasurement("HIBWCDUIYHWASDAD")
                }),
                onSuccess: (data: Measurement) => {
                    console.log(data)
                    setAirConditionParameters(Array.isArray(data) ? data : [data])
                }
            },
            {
                queryKey: ['TemperatureQuery'],
                queryFn: (() => getLastAirConditionerMeasurement("HIBWCDUIYHWASDAE")),
                onSuccess: (data: Measurement) => {
                    setTemperature(data.value);
                }
            },
            {
                queryKey: ['AirHumidityQuery'],
                queryFn: (() => getLastHumidifierFilterMeasurement("HIBWCDUIYHWASDAF")),
                onSuccess: (data: Measurement) => {
                    setHumidity(data.value);
                }
            },
            {
                queryKey: ['GetHourActivityQuery'],
                queryFn: (getHomeActivityHours),
                onSuccess: (activityHours: number[]) => {
                    setActivityHours(activityHours);
                }
            },
            {
                queryKey: ['GetHomeInfoQuery'],
                queryFn: (getHomeInfo),
                onSuccess: (data: HomeInfo) => {
                    setRequestedTemperature(prevState => (data.preferredTemp !== undefined && data.preferredTemp !== null) ? data.preferredTemp : prevState)
                    setRequestedAirHumadity(prevState => (data.preferredHum !== undefined && data.preferredHum !== null) ? data.preferredHum : prevState)
                }
            }
    ])

    useEffect(() => {
        getActivityHours.refetch();
        forecastWeatherQuery.refetch();
        getHomeInformation.refetch()
    }, [getActivityHours.refetch, forecastWeatherQuery.refetch, getHomeInformation.refetch])

    useEffect(() => {
        airConditionQuery.refetch();
        temperatureQuery.refetch();
        humidityQuery.refetch();
    }, [activeDevice])

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

    const addActivityHour = (clickedHour: number) =>
    {
        setActivityHours((prev) => {
            if(prev.find(element => element === clickedHour) !== undefined)
            {
                sendDeleteHomeActivityHourRequest(clickedHour);
                return prev.filter(element => element !== clickedHour);
            }
            sendAddHomeActivityHourRequest(clickedHour);
            return [...prev, clickedHour];
        });
    }

    const sendAddHomeActivityHourRequest = async (clickedHour: number) => {
        addHomeActivityHourRequest.mutate(clickedHour, {onSuccess: (response: any) => {
            console.log(response);
        }});
    }

    const sendDeleteHomeActivityHourRequest = async (clickedHour: number) => {
        deleteHomeActivityHourRequest.mutate(clickedHour, {onSuccess: (response: any) => {
            console.log(response);
        }});
    }

    const handleOnTemperatureChange = (e: SelectChangeEvent) => {
        setRequestedTemperature(parseInt(e.target.value));
        setHomeTemperatureRequest.mutate(parseInt(e.target.value), {onSuccess: (response: any) =>
        {
            console.log(response);
        }})
        runAgainWithTemperatureRequest.mutate(parseInt(e.target.value), {onSuccess: (response: any) =>
        {
            console.log(response);
        }})
    };

    const handleOnAirHumadityChange = (e: SelectChangeEvent) => {
        setRequestedAirHumadity(parseInt(e.target.value));
        setHomeHumidityRequest.mutate(parseInt(e.target.value), {onSuccess: (response: any) => {
            console.log(response);
        }})
        runAgainWithHumidityRequest.mutate(parseInt(e.target.value), {onSuccess: (response: any) =>
        {
            console.log(response);
        }})
    };

    const changeActiveDevice = (device: DeviceType) => {
        setActiveDevice(device);
    }

    const checkIAIMeasurement = (value: number) => 
    {
        if( value >= 1 && value <= 3 )
        {
            return AirCondition.Good;
        } else if ( value > 3 && value <= 6 )
        {
            return AirCondition.Acceptable;
        }
        else
        {
            return AirCondition.Bad;
        }
    }

    const checkPM25Measurement = (value: number) =>
    {
        if( value >= 1 && value <= 20 )
        {
            return AirCondition.Good;
        } else if ( value > 20 && value <= 35)
        {
            return AirCondition.Acceptable;
        }
        else
        {
            return AirCondition.Bad;
        }
    }

    const checkGASMeasurement = (value: number) => 
    {
        if( value === 1 )
        {
            return AirCondition.Good;
        } else if (value === 3 || value === 2)
        {
            return AirCondition.Acceptable;
        }
        else
        {
            return AirCondition.Bad;
        }
    }

    const checkMeasurements = (type: MeasurementType, value:number) =>
    {
        if (type === MeasurementType.IAI)
        {
            return checkIAIMeasurement(value);
        } else if (type === MeasurementType.GAS)
        {
            return checkGASMeasurement(value);
        }
        return checkPM25Measurement(value);
    }

    const chooseColor = (airCondition : AirCondition) =>
    {
        if (airCondition === AirCondition.Good)
        {
            return 'rgba(76, 175, 80, 0.7)' // green
        } else if (airCondition === AirCondition.Acceptable)
        {
            return 'rgba(255, 224, 102, 0.7)' // orange
        }
        return 'rgba(255, 153, 153, 0.7)' // red
    }

    const decideQualityOfAir = () =>
    {
        var worstQuality: AirCondition = AirCondition.Good
        airConditionParameters.forEach(item => {
            var quality: AirCondition = checkMeasurements(item.type, item.value);
            if (quality < worstQuality)
            {
                worstQuality = quality;
            }
        })
        if (worstQuality === AirCondition.Good)
        {
            return "Dobra";
        } else if (worstQuality === AirCondition.Acceptable)
        {
            return "Średnia";
        }
        return "Zła";
    }

    const AirConditionRows = airConditionParameters.map((item, index) => (
        <TableRow sx={{backgroundColor: chooseColor(checkMeasurements(item.type, item.value))}} key={index}>
            <TableCell>{item.type}</TableCell>
            <TableCell>{item.value}</TableCell>
        </TableRow>
    ));

    const TemperatureRow = (<TableRow sx={{backgroundColor: 'rgba(76, 175, 80, 0.7)'}}>
    <TableCell>Temperatura</TableCell>
    <TableCell>{temperature}°C</TableCell>
    </TableRow>);

    const AirHumidityRow = (<TableRow sx={{backgroundColor: 'rgba(76, 175, 80, 0.7)'}}>
    <TableCell>Wilgotność</TableCell>
    <TableCell>{humidity}%</TableCell>
    </TableRow>);

    

    const hoursList = hoursArray.map((item, index) => (
        <Grid item xs={1} key={index}>
            <Button sx={{border: 1, borderRadius: '10px', borderColor: '#bfbdbd', color:'black'}} 
                onClick={() => addActivityHour(item)} variant={activityHours.find(value => value === item) !== undefined ? "contained" : "outlined"}
                color="primary">
                    {item < 10 ? '0'+ item : item}:00
            </Button>
        </Grid>
    ));

    const temperatureOptions = availableTemperatures.map((item, index) => (
        <MenuItem id={`Temperature-Options-${item}`} value={item} key={index}>{item}°C</MenuItem>
    ));

    const airHumadityOptions = availableAirHumadityLevels.map((item, index) => (
        <MenuItem value={item} key={index}>{item}%</MenuItem>
    ))
    return (
        <Grid container spacing={2} sx={{marginTop: '20px'}}>
            <Grid item xs={1}>
                <Box sx={{marginTop: '50px', marginLeft: '10px', minWidth:'100px'}}>
                    <h3>Pogoda</h3>
                    <Box sx={{border: 1, borderColor: 'grey', borderRadius: '10px', margin: '20px',  minWidth:'50px'}}>
                        <span>Dziś</span>
                        <br/>
                        <br/>
                        <WbSunnyIcon/>
                        <p>{todayMaxTemperature}°C</p>
                        <NightlightIcon/>
                        <p>{todayMinTemperature}°C</p>

                    </Box>
                    <Box sx={{border: 1, borderRadius: '10px', margin: '20px', minWidth:'50px'}}>
                        <span>Jutro</span>
                        <br/>
                        <br/>
                        <WbSunnyIcon/>
                        <p>{tomorrowMaxTemperature}°C</p>
                        <NightlightIcon/>
                        <p>{tomorrowMinTemperature}°C</p>

                    </Box>
                </Box>
            </Grid>
            <Grid item xs={6} sx={{marginLeft: '30px', minWidth: '500px'}}>
                <Box sx={{ marginLeft: '10px', marginRight: '10px'}}>
                    <img src="home.jpg" style={{height: '200px', width: '100%', borderRadius:'10px'}}/>
                </Box>
                <TableContainer component={Paper} sx={{marginTop: '30px', borderRadius:'10px'}}>
                <Table sx={{minWidth: 250}} aria-label='simple table'>
                    <TableHead>
                        <TableRow>
                            <TableCell><b>Czujnik</b></TableCell>
                            <TableCell><b>Odczyt</b></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        <TableRow>
                            <TableCell>Temperatura</TableCell>
                            <TableCell>{temperature}°C</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell>Wilgotność</TableCell>
                            <TableCell>{humidity}%</TableCell>
                        </TableRow>
                        <TableRow>
                            <TableCell>Jakość powietrza</TableCell>
                            <TableCell>{decideQualityOfAir()}</TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
                </TableContainer>

            </Grid>
            <Grid item xs={4} sx={{marginLeft: '30px', minWidth: '500px'}}>
                <Box sx={{minHeight: '200px', marginLeft: '10px', marginRight: '10px'}}>
                    <p><b>Urządzenia</b></p>
                    <TableContainer component={Paper} sx={{marginTop: '20px', borderRadius: '10px'}}>
                        <Table sx={{minWidth: 100}} aria-label='simple table'>
                            <TableHead>
                                <TableRow>
                                    <TableCell><b>{deviceNameMapper(activeDevice)}</b></TableCell>
                                    <TableCell></TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {activeDevice === DeviceType.AirConditioner && TemperatureRow}
                                {activeDevice === DeviceType.AirFilter && AirConditionRows}
                                {activeDevice === DeviceType.Humidifier && AirHumidityRow}
                            </TableBody>
                        </Table>
                    </TableContainer>
                    <div>
                        {activeDevice === DeviceType.AirConditioner ? <FiberManualRecordIcon/> : <FiberManualRecordOutlinedIcon onClick={() => changeActiveDevice(DeviceType.AirConditioner)}/>}
                        {activeDevice === DeviceType.Humidifier ? <FiberManualRecordIcon/> : <FiberManualRecordOutlinedIcon onClick={() => changeActiveDevice(DeviceType.Humidifier)}/>}
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
                                <Select id={"Select-Temperature"} value={`${requestedTemperature}`} onChange={handleOnTemperatureChange}>
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
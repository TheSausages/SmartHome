import React, {useState, useEffect} from 'react';
import {
    Box,
    Button,
    Grid,
    Input,
    InputLabel,
    Paper,
    Stack,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow
} from "@mui/material";
import {add_device_path, add_sensor_path} from '../../../common/Paths';
import {NavLink} from 'react-router-dom';
import {useQueries, useMutation} from 'react-query';
import {
    activateFunctionalDevice, deactivateFunctionalDevice,
    getAllHomeFunctionalDevices,
    getAllHomeSensors,
    getHomeInfo,
    updateHomeInfo
} from '../../../common/RequestHelper/RequestHelper';
import { HomeInfo } from '../../../data/HomeInfo';
import { FunctionalDeviceInfo } from '../../../data/FunctionalDevices';
import { SensorInfo } from '../../../data/Sensort';

export interface SettingsProps
{};

type DeviceParameters = {
    serialNumber: string,
    name: string,
    connected: boolean,
    powerLevel: number,
    averageConsumption: number
}

type SensorParameters = {
    serialNumber: string,
    name: string,
    connected: boolean
}

export default function Settings(props: SettingsProps) {
    const [ name, setName ] = useState<string>('');
    const [ street, setStreet ] = useState<string>('');
    const [ city, setCity ] = useState<string>('');
    const [ zipCode, setZipCode ] = useState<string>('');
    const [ country, setCountry ] = useState<string>('');
    const [ devices, setDevices] = useState<DeviceParameters[]>([]);
    const [ sensores, setSensores] = useState<SensorParameters[]>([]);

    const updateHomeInfoMutation = useMutation(updateHomeInfo);

    const [ sensorsQuery, devicesQuery, homeInfoQuery ] = useQueries([
        {
            queryKey: ['GetAllSensorsForHome'],
            queryFn: () => getAllHomeSensors(),
            onSuccess: (data: SensorInfo[]) => {
                setSensores(data.map((item: SensorInfo) => ({serialNumber: item.serialNumber, name: item.name, connected: item.connected})))
                console.log(data);
            }
        },
        {
            queryKey: ['GetAllFunctionalDevices'],
            queryFn: () => getAllHomeFunctionalDevices(),
            onSuccess: (data: FunctionalDeviceInfo[]) => {
                setDevices(data.map((item: FunctionalDeviceInfo) => ({serialNumber: item.serialNumber, name: item.name, connected: item.connected, powerLevel: item.powerLevel, averageConsumption: item.averageConsumption})))
                console.log(data);
            }
        },
        {
            queryKey: ['GetHomeInfo'],
            queryFn: () => getHomeInfo(),
            onSuccess: (data: HomeInfo) => {
                setName(data.name);
                setStreet(data.street);
                setCity(data.city);
                setCountry(data.country);
                setZipCode(data.postCode);
            }
        }
    ]);

    useEffect(() => {
        sensorsQuery.refetch();
        devicesQuery.refetch();
        homeInfoQuery.refetch();
    }, [sensorsQuery.refetch, devicesQuery.refetch, homeInfoQuery.refetch]);


    const handleOnSubmit = (e: any) => {
        e.preventDefault();
        updateHomeInfoMutation.mutate({name: name, country: country, city: city, postCode: zipCode, street: street}, {onSuccess: (response: any) => {
            console.log(response);
        }});
    } 
    const handleOnNameChange = (e: any) => setName(e.target.value);
    const handleOnStreetChange = (e: any) => setStreet(e.target.value);
    const handleOnCityChange = (e: any) => setCity(e.target.value);
    const handleOnZipCodeChange = (e: any) => setZipCode(e.target.value);
    const handleOnCountryChange = (e: any) => setCountry(e.target.value);

    const resetInputs = () => {
        setName('');
        setStreet('');
        setCity('');
        setZipCode('');
        setCountry('');
    }

    const changeDeviceState = (index: number) => {
        const nextDevs = devices.map((item, arrayNumber) => {
            if (arrayNumber === index) {
                const newItem = {...item};

                if (item.connected) {
                    deactivateFunctionalDevice(item.serialNumber)
                        .then(resp => {
                            if (resp as boolean) {
                                newItem.connected = !item.connected;
                            } else {
                                console.log(`Did not change state of ${item.serialNumber} correctly`)
                            }
                        })
                } else {
                    activateFunctionalDevice(item.serialNumber)
                        .then(resp => {
                            if (resp as boolean) {
                                newItem.connected = !item.connected;
                            } else {
                                console.log(`Did not change state of ${item.serialNumber} correctly`)
                            }
                        })
                }


                return newItem
            }
            return item;
        })

        setDevices(nextDevs)

        setTimeout(() => devicesQuery.refetch(), 500)
    };

    const changeSensorState = (index: number) => {
        setSensores(prevSensores => {
            return prevSensores.map((item, arrayNumber) => {
                if(arrayNumber === index) {
                    const newItem = item;
                    newItem.connected = !newItem.connected;
                    return newItem;
                }
                return item;
            });
        });
    };

    const devicesMap = devices
        .sort((a,b) => a.serialNumber.localeCompare(b.serialNumber))
        .map((item, index) => (
            <TableRow key={index}>
                <TableCell>{item.serialNumber}</TableCell>
                <TableCell>{item.name}</TableCell>
                <TableCell>{item.powerLevel}</TableCell>
                <TableCell>{item.averageConsumption}</TableCell>
                <TableCell>
                    <Button sx={{width: '100%'}} variant="contained" onClick={() => changeDeviceState(index)}>{item.connected ? 'Rozłącz': 'Połącz'}</Button>
                </TableCell>
            </TableRow>
        ));

    const sensorMap = sensores
        .sort((a,b) => a.serialNumber.localeCompare(b.serialNumber))
        .map((item, index) => (
            <TableRow key={index}>
                <TableCell>{item.serialNumber}</TableCell>
                <TableCell>{item.name}</TableCell>
                <TableCell>{item.connected ? "Połączony" : "Rozłączony"}</TableCell>
                <TableCell>
                    <Button sx={{width: '100%'}} variant="contained" onClick={() => changeSensorState(index)}>{item.connected ? 'Rozłącz': 'Połącz'}</Button>
                </TableCell>
            </TableRow>
        ));

    return (
        <Grid container spacing={2} sx={{marginTop: '30px'}}>
            <Grid item xs={1} />
            <Grid item xs={5}>
                <h3>Adres</h3>
                <Stack direction="column">
                    <form method='post' onSubmit={handleOnSubmit}>
                    <Box sx={{width: 'auto', marginTop: '10px'}}>
                        <InputLabel sx={{display: 'inline-block', width: '25%'}}>Nazwa:</InputLabel>
                        <Input value={name} onChange={handleOnNameChange} type="text"/>
                    </Box>
                    <Box sx={{width: 'auto', marginTop: '10px'}}>
                        <InputLabel sx={{display: 'inline-block', width: '25%'}}>Ulica:</InputLabel>
                        <Input value={street} onChange={handleOnStreetChange} type="text"/>
                    </Box>
                    <Box sx={{width: 'auto', marginTop: '10px'}}>
                        <InputLabel sx={{display: 'inline-block', width: '25%'}}>Miasto:</InputLabel>
                        <Input value={city} onChange={handleOnCityChange} type="text"/>
                    </Box>
                    <Box sx={{width: 'auto', marginTop: '10px'}}>
                        <InputLabel sx={{display: 'inline-block', width: '25%'}}>Kod pocztowy:</InputLabel>
                        <Input value={zipCode} onChange={handleOnZipCodeChange} type="text"/>
                    </Box>
                    <Box sx={{width: 'auto', marginTop: '10px'}}>
                        <InputLabel sx={{display: 'inline-block', width: '25%'}}>Kraj:</InputLabel>
                        <Input value={country} onChange={handleOnCountryChange} type="text"/>
                    </Box>
                    <Box sx={{marginTop: '30px'}}>
                        <Button variant="contained" sx={{marginRight: '10px', backgroundColor: 'rgba(255, 224, 102, 0.7)'}} onClick={resetInputs}>Anuluj</Button>
                        <Button sx={{backgroundColor: 'rgba(76, 175, 80, 0.7)'}} variant="contained" type="submit">Zapisz</Button>
                    </Box>
                    </form>
                </Stack>
            </Grid>
            <Grid item xs={6} sx={{paddingRight: '20px'}}>
                <h3>Urządzenia</h3>
                <TableContainer component={Paper} sx={{marginTop: '10px'}}>
                <Table aria-label='simple table' sx={{borderRadius: '10px'}}>
                    <TableHead>
                        <TableRow>
                            <TableCell>Nr seryjny</TableCell>
                            <TableCell>Nazwa</TableCell>
                            <TableCell>Poziom mocy</TableCell>
                            <TableCell>Śr. zużycie / 24h</TableCell>
                            <TableCell><Button component={NavLink} to={add_device_path}>+</Button></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {devicesMap}
                    </TableBody>
                </Table>
                </TableContainer>

                <h3>Czujniki</h3>
                <TableContainer component={Paper} sx={{marginTop: '10px', marginBottom: '30px'}}>
                <Table aria-label='simple table' sx={{borderRadius: '10px'}}>
                    <TableHead>
                        <TableRow>
                            <TableCell>Nr seryjny</TableCell>
                            <TableCell>Nazwa</TableCell>
                            <TableCell>Status</TableCell>
                            <TableCell><Button component={NavLink} to={add_sensor_path}>+</Button></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {sensorMap}
                    </TableBody>
                </Table>
                </TableContainer>
            </Grid>
        </Grid>
    )
}
import React, {useState} from 'react';
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

export interface SettingsProps
{};

type DeviceParameters = {
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
    const [ devices, setDevices] = useState<DeviceParameters[]>([{serialNumber: 'dwdawda', name:'Filtr Xiaomi', connected: false}, {serialNumber:'dawdadaw', name:'Klimatyzator', connected:true}]);
    const [ sensores, setSensores] = useState<DeviceParameters[]>([{serialNumber: 'dwdawda', name:'Jakość powietrza', connected: false}, {serialNumber:'dawdadaw', name:'Termometr', connected:true}]);


    const handleOnSubmit = (e: any) => {
        e.preventDefault();
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
        setDevices(prevDevices => {
            return prevDevices.map((item, arrayNumber) => {
                if(arrayNumber === index) {
                    const newItem = item;
                    newItem.connected = !newItem.connected;
                    return newItem;
                }
                return item;
            });
        });
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

    const devicesMap = devices.map((item, index) => (
        <TableRow key={index}>
            <TableCell>{item.serialNumber}</TableCell>
            <TableCell>{item.name}</TableCell>
            <TableCell>{item.connected ? "Połączony" : "Rozłączony"}</TableCell>
            <TableCell>
                {item.connected ? <Button sx={{width: '100%'}} variant="contained" onClick={() => changeDeviceState(index)}>Rozłącz</Button> :
                <Button sx={{width: '100%'}} variant="contained" onClick={() => changeDeviceState(index)}>Połącz</Button>}
            </TableCell>
        </TableRow>
    ));

    const sensorMap = sensores.map((item, index) => (
        <TableRow key={index}>
            <TableCell>{item.serialNumber}</TableCell>
            <TableCell>{item.name}</TableCell>
            <TableCell>{item.connected ? "Połączony" : "Rozłączony"}</TableCell>
            <TableCell>
                {item.connected ? <Button sx={{width: '100%'}} variant="contained" onClick={() => changeSensorState(index)}>Rozłącz</Button> :
                <Button sx={{width: '100%'}} variant="contained" onClick={() => changeSensorState(index)}>Połącz</Button>}
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
                        <Button color="warning" variant="contained" sx={{marginRight: '10px'}} onClick={resetInputs}>Anuluj</Button>
                        <Button color="success" variant="contained" type="submit">Zapisz</Button>
                    </Box>
                    </form>
                </Stack>
            </Grid>
            <Grid item xs={6} sx={{paddingRight: '20px'}}>
                <h3>Urządzenia</h3>
                <TableContainer component={Paper} sx={{marginTop: '10px'}}>
                <Table aria-label='simple table'>
                    <TableHead>
                        <TableRow>
                            <TableCell>Nr seryjny</TableCell>
                            <TableCell>Nazwa</TableCell>
                            <TableCell>Status</TableCell>
                            <TableCell><Button component={NavLink} to={add_device_path}>+</Button></TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {devicesMap}
                    </TableBody>
                </Table>
                </TableContainer>

                <h3>Czujniki</h3>
                <TableContainer component={Paper} sx={{marginTop: '10px'}}>
                <Table aria-label='simple table'>
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
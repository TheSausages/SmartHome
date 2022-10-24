import { Box, Grid, Icon, Paper, Table, TableBody, TableCell, TableContainer, TableHead, TableRow } from "@mui/material";
import WbSunnyIcon from '@mui/icons-material/WbSunny'
import NightlightIcon from '@mui/icons-material/Nightlight';
import FiberManualRecordOutlinedIcon from '@mui/icons-material/FiberManualRecordOutlined';
import FiberManualRecordIcon from '@mui/icons-material/FiberManualRecord';
import ThermostatIcon from '@mui/icons-material/Thermostat';
import BloodtypeIcon from '@mui/icons-material/Bloodtype';

export interface HomeProps
{
}

// const WeatherDiv = styled('div')({
//     border: 1px
// });

export default function Home(props: HomeProps) {
    return (
        <Grid container spacing={2} sx={{marginTop: '30px'}}>
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
                    <TableContainer component={Paper} sx={{marginTop: '30px'}}>
                        <Table sx={{minWidth: 100}} aria-label='simple table'>
                            <TableHead>
                                <TableRow>
                                    <TableCell><b>Filtr powietrza</b></TableCell>
                                    <TableCell></TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                <TableRow>
                                    <TableCell>PM 2</TableCell>
                                    <TableCell>5</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>PM 10</TableCell>
                                    <TableCell>24</TableCell>
                                </TableRow>
                                <TableRow>
                                    <TableCell>IAI</TableCell>
                                    <TableCell>03</TableCell>
                                </TableRow>
                            </TableBody>
                        </Table>
                    </TableContainer>
                    <div>
                        <FiberManualRecordIcon/>
                        <FiberManualRecordOutlinedIcon/>
                        <FiberManualRecordOutlinedIcon/>
                    </div>
                </Box>
                <Box sx={{minHeight: '200px', marginLeft: '10px', marginRight: '10px'}}>
                    <p><b>Preferencje domowników</b></p>
                    <Grid container spacing={2}>
                        <Grid item xs={6}>
                            <p>Temperatura</p>
                            <ThermostatIcon fontSize="large"/>
                            <p>17°C</p>
                        </Grid>
                        <Grid item xs={6}>
                            <p>Wilgotność powietrza</p>
                            <BloodtypeIcon fontSize="large"/>
                            <p>45%</p>
                        </Grid>
                    </Grid>
                </Box>
            </Grid>
        </Grid>
    )
}
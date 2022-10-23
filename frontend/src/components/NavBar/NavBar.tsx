import { Button, Container,IconButton} from '@mui/material';
import AppBar from '@mui/material/AppBar';
import { NavLink } from 'react-router-dom';
import { help_path, history_path, login_path } from '../../common/Paths';

export interface NavbarProps
{
}

export default function NavBar(props: NavbarProps) {
    return (
        <AppBar position='static' sx={{backgroundColor: 'black'}}>
            <Container maxWidth="xl">
                    <IconButton component={NavLink} to="/">
                        <img src="logo.svg" alt="logo"/>
                    </IconButton>
                    <Button color="inherit" component={NavLink} to="/">Dom</Button>
                    <Button color="inherit" component={NavLink} to={history_path}>Historia odczytów</Button>
                    <Button color="inherit" component={NavLink} to={help_path}>Pomoc</Button>
                    <Button color="inherit" component={NavLink} to={login_path}>Zaloguj</Button>
                    <Button color="inherit">Wyloguj</Button>
            </Container>
        </AppBar>
    )
}
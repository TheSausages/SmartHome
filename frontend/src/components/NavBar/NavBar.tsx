import React from 'react';
import {Button, IconButton} from '@mui/material';
import AppBar from '@mui/material/AppBar';
import {NavLink} from 'react-router-dom';
import {help_path, history_path, settings_path} from '../../common/Paths';
import {styled} from '@mui/system';
import UserService from "../../service/UserService";

export interface NavbarProps {
}

const StyledDiv = styled('div')({
    display: "flex",
    flexDirection: "row",
    flexWrap: "wrap",
    justifyContent: "space-between",
    alignItems: "stretch",
    alignContent: "flex-start"
});

export default function NavBar(props: NavbarProps) {
    return (
        <AppBar position='static' sx={{backgroundColor: 'rgba(76, 175, 80, 0.7)', borderRadius: '10px', marginTop:'5px', width:'90%', marginLeft:'5%'}}>
            <StyledDiv>
                <div>
                    <IconButton component={NavLink} to="/">
                    <img src="css_sprites.webp" style={{height: '48px', width: '48px', objectFit: "none", objectPosition: "-10px -230px"}}/>
                    </IconButton>
                    <Button color="inherit" component={NavLink} id="home" to="/">Dom</Button>
                    <Button color="inherit" component={NavLink} id="history" to={history_path}>Historia odczytów</Button>
                    <Button color="inherit" component={NavLink} id="help" to={help_path}>Pomoc</Button>
                    <Button color="inherit" component={NavLink} id="settings" to={settings_path}>Ustawienia</Button>
                </div>
                <div style={{ marginBlock: 'auto' }}>
                    {!UserService.isLoggedIn() && <Button color="inherit" onClick={() => UserService.doLogin()}>Zaloguj</Button>}
                    {UserService.isLoggedIn() && <Button color="inherit" onClick={() => UserService.doLogout()}>Wyloguj</Button>}
                </div>
            </StyledDiv>
        </AppBar>
    )
}
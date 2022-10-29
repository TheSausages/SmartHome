import React from 'react';
import { Button,IconButton } from '@mui/material';
import AppBar from '@mui/material/AppBar';
import { NavLink } from 'react-router-dom';
import { help_path, history_path, login_path } from '../../common/Paths';
import { styled } from '@mui/system';

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
        <AppBar position='static' sx={{backgroundColor: 'black'}}>
            <StyledDiv>
                <div>
                    <IconButton component={NavLink} to="/">
                        <img src="logo.svg" alt="logo"/>
                    </IconButton>
                    <Button color="inherit" component={NavLink} to="/">Dom</Button>
                    <Button color="inherit" component={NavLink} to={history_path}>Historia odczytów</Button>
                    <Button color="inherit" component={NavLink} to={help_path}>Pomoc</Button>
                </div>
                <div style={{ marginBlock: 'auto' }}>
                    <Button color="inherit" component={NavLink} to={login_path}>Zaloguj</Button>
                    <Button color="inherit">Wyloguj</Button>
                </div>
            </StyledDiv>
        </AppBar>
    )
}
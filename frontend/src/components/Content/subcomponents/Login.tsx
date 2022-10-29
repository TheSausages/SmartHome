import { Button, Input, InputLabel } from '@mui/material';
import { Box, Stack } from '@mui/system';
import React, { useState } from 'react';

export interface LoginProps
{
}

export default function Login(props: LoginProps) {
    const [ loginInput, setLoginInput ] = useState<string>('');
    const [ passwordInput, setPasswordInput ] = useState<string>('');

    const handleOnLoginChange = (e: any) => {
        setLoginInput(e.target.value);
    };

    const handleOnPasswordChange = (e: any) => {
        setPasswordInput(e.target.value);
    }

    const handleResetButton = () => {
        setLoginInput('');
        setPasswordInput('');
    };

    const handleOnSubmit = (e: any) => {
        e.preventDefault();
    }

    return (
        <Stack direction="column" sx={{marginTop: '50px'}}>
            <form method="post" onSubmit={handleOnSubmit}>
                <Box>
                    <InputLabel>Login:</InputLabel>
                    <Input value={loginInput} onChange={handleOnLoginChange} type="text"/>
                </Box>
                <Box sx={{marginTop: '20px'}}>
                    <InputLabel>Hasło:</InputLabel>
                    <Input value={passwordInput} onChange={handleOnPasswordChange} type="password"/>
                </Box>
                <Box sx={{marginTop: '30px'}}>
                    <Button color="warning" variant="contained" sx={{marginRight: '10px'}} onClick={handleResetButton}>Anuluj</Button>
                    <Button color="success" variant="contained" type="submit">Zaloguj</Button>
                </Box>
            </form>
        </Stack>
    )
}
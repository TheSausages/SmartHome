import React from 'react';

import { Paper, Typography } from "@mui/material";

export interface FooterProps
{
}

export default function Footer(props: FooterProps) {
    return (
        <Paper sx={{width: '100%', position: 'fixed', bottom: 0}} component="footer" square variant="outlined">
            <Typography variant="caption" color = 'initial'>
                Copyright ©2022
            </Typography>
        </Paper>

    )
}

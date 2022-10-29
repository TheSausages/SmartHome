import { Grid } from '@mui/material';
import { Box } from '@mui/system';
import React from 'react';

export interface HelpProps
{
}

export default function Help(props: HelpProps) {
    return (
        <Grid container spacing={2} sx={{marginTop: '30px'}}>
            <Grid item xs={2}/>
            <Grid item xs={8}>
                <Box>
                    <h1>O nas</h1>
                    <h3>Rozwiązanie o nazwie SMART HOME zostało stworzone w 2022 roku</h3>
                    <p>Jesteśmy firmą zajmującą się dostarczaniem oprogramowania do sterowania inteligentnymi domami. Naszym głównym celem jest
                        zwiększenie komfortu mieszkańców wraz z jednoczesnym zmniejszeniem zużycia energii elektrycznej oraz zmniejszeniem ilości
                        zarazków unoszących się w powietrzu. Powodem powstania projektu był zaobserwowany wzrost cen energii elektrycznej
                        oraz pandemia Sars-Cov-2. Proponowane przez nas rozwiązanie spowoduje, że już od dziś na zawsze będziesz mógł zapomnieć
                        o ręcznym strojeniu urządzeń w swoim domu   
                    </p>
                </Box>
                <Box sx={{marginTop: '20px'}}>
                    <h1>Kontakt</h1>
                    <h4>SMART HOME</h4>
                    <p><b>Adres:</b> wyb. Stanisława Wyspiańskiego X, 50-370 Wrocław</p>
                    <p><b>Tel:</b> +48 888 888 888</p>
                    <p><b>Email:</b> dummyEmail@student.pwr.edu.pl</p>
                    <p><b>Fax:</b> 888 999 991</p>
                </Box>
            </Grid>
        </Grid>
    )
}
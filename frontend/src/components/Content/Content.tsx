import React from 'react';

import {Routes, Route} from 'react-router-dom';
import Home from './subcomponents/Home';
import History from './subcomponents/History';
import Help from './subcomponents/Help';
import Login from './subcomponents/Login';
import { help_path, history_path, login_path } from '../../common/Paths';

export interface ContentProps
{
}

export default function Content(props: ContentProps) {
    return (
        <main>
            <Routes>
                <Route path={history_path} element={<History/>} />
                <Route path={help_path} element={<Help/>} />
                <Route path={login_path} element={<Login/>} />
                <Route path="/" element={<Home/>} />
            </Routes>
        </main>
    )
}
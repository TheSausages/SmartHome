import React from 'react';

import {Route, Routes} from 'react-router-dom';
import Home from './subcomponents/Home';
import History from './subcomponents/History';
import Help from './subcomponents/Help';
import {add_device_path, add_sensor_path, help_path, history_path, settings_path} from '../../common/Paths';
import {DeviceDestiny} from '../../common/DeviceType';
import Settings from './subcomponents/Settings';
import DeviceAdder from './subcomponents/DeviceAdder';

export interface ContentProps
{
}

export default function Content(props: ContentProps) {
    return (
        <main>
            <Routes>
                <Route path={history_path} element={<History/>} />
                <Route path={help_path} element={<Help/>} />
                <Route path={settings_path} element={<Settings/>} />
                <Route path={add_device_path} element={<DeviceAdder deviceDestiny={DeviceDestiny.FunctionalDevice}/>} />
                <Route path={add_sensor_path} element={<DeviceAdder deviceDestiny={DeviceDestiny.Sensor}/>} />
                <Route path="/" element={<Home/>} />
            </Routes>
        </main>
    )
}
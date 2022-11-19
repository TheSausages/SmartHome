import React from 'react';

import {Route, Routes} from 'react-router-dom';
import Home from './subcomponents/Home';
import History from './subcomponents/History';
import Help from './subcomponents/Help';
import {add_device_path, add_sensor_path, help_path, history_path, settings_path} from '../../common/Paths';
import {DeviceDestiny} from '../../common/DeviceType';
import Settings from './subcomponents/Settings';
import DeviceAdder from './subcomponents/DeviceAdder';
import PrivateRoute from "../PrivateRoute/PrivateRoute";

export interface ContentProps
{
}

export default function Content(props: ContentProps) {
    return (
        <main>
            <Routes>
                {/*Public Routes*/}
                <Route path={help_path} element={<Help/>} />

                {/*Private Routes*/}
                <Route path={history_path} element={<PrivateRoute><History/></PrivateRoute>} />
                <Route path={settings_path} element={<PrivateRoute><Settings/></PrivateRoute>} />
                <Route path={add_device_path} element={<PrivateRoute><DeviceAdder deviceDestiny={DeviceDestiny.FunctionalDevice}/></PrivateRoute>} />
                <Route path={add_sensor_path} element={<PrivateRoute><DeviceAdder deviceDestiny={DeviceDestiny.Sensor}/></PrivateRoute>} />
                <Route path="/" element={<PrivateRoute><Home/></PrivateRoute>} />
            </Routes>
        </main>
    )
}
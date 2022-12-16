import axios from 'axios';
import UserService from "../../service/UserService";
import {ApiError} from "../../data/ApiError";
import { SensorAdder, SensorQueryParameters } from '../../data/Sensort';
import { HomeInfo } from '../../data/HomeInfo';
import { FunctionalDeviceAdder } from '../../data/FunctionalDevices';


const request = axios.create({
    timeout: 1000,
    headers: {
        'Content-Type': 'application/json',
    }
});

request.interceptors.response.use((response) => response, (error: ApiError) => {
    console.log(`Error with status ${error.status}: ${error.message}`)
});

const addToken = () => request.defaults.headers.common['Authorization'] = `Bearer ${UserService.getToken()}`;

const control_request = (path: string) => `${window.__RUNTIME_CONFIG__.CONTROL_API_PATH}${path}`
const data_request = (path: string) => `${window.__RUNTIME_CONFIG__.DATA_API_PATH}${path}`


const house_location_api_path = (userId: string) => `/latlong/${userId}`
const air_conditioner_last_api_path = (sensorSerialNumber: string) => `/lastAirConditionerMeasurement?sensorSerialNumber=${sensorSerialNumber}`
const air_conditioner_all_api_path = (sensorSerialNumber: string, page: number, size: number) => `/allAirConditionerMeasurements?sensorSerialNumber=${sensorSerialNumber}&page=${page}&size=${size}`
const air_filter_last_api_path = (sensorSerialNumber: string) => `/lastAirFilterMeasurements?sensorSerialNumber=${sensorSerialNumber}`
const air_filter_all_api_path = (sensorSerialNumber: string, page: number, size: number) => `/allAirFilterMeasurements?sensorSerialNumber=${sensorSerialNumber}&page=${page}&size=${size}`
const air_humidifier_last_api_path = (sensorSerialNumber: string) => `/lastAirHumidifierMeasurement?sensorSerialNumber=${sensorSerialNumber}`
const air_humidifier_all_api_path = (sensorSerialNumber: string, page: number, size: number) => `/allAirHumidifierMeasurements?sensorSerialNumber=${sensorSerialNumber}&page=${page}&size=${size}`
const sensor_measurements_from_date_to_date_api_path = (sensorSerialNumber: string, startDate: string, endDate: string) => `/measurements?sensorSerialNumber=${sensorSerialNumber}&fromDate=${startDate} 00:00:00.000&toDate=${endDate} 23:59:59.999`;
const home_sensors_api_path = () => '/homeSensors';
const home_functional_device_api_path = () => '/homeFunctionalDevices';
const home_info_api_path = () => '/home';
const update_home_info_api_path = () => '/editAddress';
const add_functional_device_api_path = () => '/addFunctionalDevice';
const add_sensor_api_path = () => '/addSensor';
const update_functional_device_path = () => '/updateFunctionalDevice'

const weather_info_api_path = () => '/weather'
const air_info_api_path = () => '/air'
const set_temperature_api_path = (target: number) => `/temperature?target=${target}`
const set_home_temperature_api_path = (temperature: number) => `/setHouseTemperature?houseTemperature=${temperature}`;
const set_home_humidity_api_path = (humidity: number) => `/setHouseHumidity?houseHumidity=${humidity}`;
const add_home_activity_hour_path = (activityHour: number) => `/addHour?hour=${activityHour}`;
const delete_home_activity_hour_path = (activityHour: number) => `/removeHour?hour=${activityHour}`;
const get_home_activity_hours_path = () => '/getHours';
const set_air_quality_api_path = (target: number) => `/air-quality?target=${target}`
const set_air_humidity_api_path = (target: number) => `/humidity?target=${target}`
const activate_functional_device_api_path = (serial: string) => `/${serial}/activate`;
const deactivate_functional_device_api_path = (serial: string) => `/${serial}/deactivate`;


export const getHouseLocation = async (userId: string) => {
    addToken();
    const response = await request.get<Location>(data_request(house_location_api_path(userId)));
    return response.data;
}

export const getHomeInfo = async() => {
    addToken();
    const response = await request.get(data_request(home_info_api_path()));
    return response.data
}

export const getLastAirConditionerMeasurement = async (sensorSerialNumber: string) => {
    addToken();

    const response = await request.get(data_request(air_conditioner_last_api_path(sensorSerialNumber)));
    return response.data;
}

export const getAllAirConditionerMeasurement = async (sensorSerialNumber: string, page: number, size: number) => {
    addToken();

    const response = await request.get(data_request(air_conditioner_all_api_path(sensorSerialNumber, page, size)));
    return response.data;
}

export const getLastAirFilterMeasurement = async (sensorSerialNumber: string) => {
    addToken();

    const response = await request.get(data_request(air_filter_last_api_path(sensorSerialNumber)));
    return response.data;
}

export const getAllAirFilterMeasurement = async (sensorSerialNumber: string, page: number, size: number) => {
    addToken();

    const response = await request.get(data_request(air_filter_all_api_path(sensorSerialNumber, page, size)));
    return response.data;
}

export const getLastHumidifierFilterMeasurement = async (sensorSerialNumber: string) => {
    addToken();

    const response = await request.get(data_request(air_humidifier_last_api_path(sensorSerialNumber)));
    return response.data;
}

export const getAllHumidifierFilterMeasurement = async (sensorSerialNumber: string, page: number, size: number) => {
    addToken();

    const response = await request.get(data_request(air_humidifier_all_api_path(sensorSerialNumber, page, size)));
    return response.data;
}

export const getSensorMeasurementFromDateToDate = async (parameterObject: SensorQueryParameters) => {
    addToken();

    const response = await request.get(data_request(sensor_measurements_from_date_to_date_api_path(parameterObject.sensorSerialNumber, parameterObject.startDate, parameterObject.endDate)))
    return response.data
}  

export const getWeatherInfo = async () => {
    addToken();

    const response = await request.get(control_request(weather_info_api_path()));
    return response.data;
}

export const getAirInfo = async () => {
    addToken();

    const response = await request.get(control_request(air_info_api_path()));
    return response.data;
}

export const setHomeTemperature = async (temperature: number) => {
    addToken();

    const response =  await request.post(data_request(set_home_temperature_api_path(temperature)));
    return response.data;
}

export const setHomeHumidity = async (humidity: number) => {
    addToken();

    const response = await request.post(data_request(set_home_humidity_api_path(humidity)));
    return response.data;
}

export const addHomeActivityHour = async (activeHour: number) => {
    addToken();

    const response = await request.post(data_request(add_home_activity_hour_path(activeHour)));
    return response.data;
}

export const deleteHomeActivityHour = async (activeHour: number) => {
    addToken();

    const response = await request.post(data_request(delete_home_activity_hour_path(activeHour)));
    return response.data
}

export const getHomeActivityHours = async () => {
    addToken();

    const response = await request.get(data_request(get_home_activity_hours_path()));
    return response.data;
}

export const setAirQuality = async (target: number) => {
    addToken();

    const response = await request.post(control_request(set_air_quality_api_path(target)));
    return response.data;
}

export const setAirHumidity = async (target: number) => {
    addToken();

    const response =  await request.post(control_request(set_air_humidity_api_path(target)));
    return response.data;
}

export const getAllHomeSensors = async () => {
    addToken();

    const response = await request.get(data_request(home_sensors_api_path()));
    return response.data;
}

export const getAllHomeFunctionalDevices = async () => {
    addToken();

    const response = await request.get(data_request(home_functional_device_api_path()));
    return response.data;
};

export const updateHomeInfo = async (parameters: HomeInfo) => {
    addToken();

    const response = await request.post(data_request(update_home_info_api_path()), {name : parameters.name, street: parameters.street,
        city: parameters.city, postCode: parameters.postCode, country: parameters.country});
    return response.data;
};

export const addNewFunctionalDevice = async (parameters: FunctionalDeviceAdder) => {
    addToken();

    const response = await request.post(data_request(add_functional_device_api_path()), {type: parameters.type, name: parameters.name, manufacturer: parameters.manufacturer, serialNumber: parameters.serialNumber, powerLevel: parameters.powerLevel});
    return response.data;
}

// export const updateFunctionalDevice = async (parameters: FunctionalDeviceAdder) => {
//     addToken();
//
//     const response = await request.put(data_request(update_functional_device_path()), {type: parameters.type, name: parameters.name, manufacturer: parameters.manufacturer, serialNumber: parameters.serialNumber, consumedElectricity: parameters.averageConsumptionPerHour, powerLevel: parameters.powerLevel});
//     return response.data;
// }

export const addNewSensor = async (parameters: SensorAdder) => {
    addToken();

    const response = await request.post(data_request(add_sensor_api_path()), {type: parameters.type, name: parameters.name, manufacturer: parameters.manufacturer, serialNumber: parameters.serialNumber})
    return response.data;
}

export const activateFunctionalDevice = async (serial: string) => {
    addToken();

    const response = await request.get(control_request(activate_functional_device_api_path(serial)))
    return response.data;
}

export const deactivateFunctionalDevice = async (serial: string) => {
    addToken();

    const response = await request.get(control_request(deactivate_functional_device_api_path(serial)))
    return response.data;
}

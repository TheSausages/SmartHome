import axios from 'axios';
import UserService from "../../service/UserService";
import {ApiError} from "../../data/ApiError";


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

const weather_info_api_path = () => '/weather'
const air_info_api_path = () => '/air'
const set_temperature_api_path = (target: number) => `/temperature?target=${target}`
const set_air_quality_api_path = (target: number) => `/air-quality?target=${target}`
const set_air_humidity_api_path = (target: number) => `/humidity?target=${target}`


export const getHouseLocation = async (userId: string) => {
    addToken();

    const response = await request.get<Location>(data_request(house_location_api_path(userId)));
    return response.data;
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

export const setTemperature = async (target: number) => {
    addToken();

    const response =  await request.post(control_request(set_temperature_api_path(target)));
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

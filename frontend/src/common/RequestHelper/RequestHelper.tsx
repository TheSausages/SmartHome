import axios from 'axios';

const request = axios.create({
    baseURL: 'https://localhost:44323',
    headers: {
        'Access-Control-Allow-Origin':'*',
        'Access-Control-Allow-Headers': 'Origin, X-Requested-With, Content-Type, Accept',
        'Content-Type': 'application/json'
    }
});

export const getDeviceInfo = async () => {
    return null;
}
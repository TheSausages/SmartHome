interface WeatherApiResponse {
    latitude: number;
    longtitude: number;
    generationtime_ms: number;
    utc_offset_seconds: number;
    timezone: string;
    timezone_abbreviation: string;
}

interface Daily {
    time: string[];
    temperature_2m_max: number[];
    temperature_2m_min: number[];
    apparent_temperature_max: number[];
    apparent_temperature_min: number[];
    precipitation_hours: number[];
    weathercode: number[];
}

interface DailyUnits {
    temperature_2m_max: string;
    temperature_2m_min: string;
    apparent_temperature_max: string;
    apparent_temperature_min: string;
    precipitation_hours: string;
    weathercode: string;
}

export interface ForecastWeather extends WeatherApiResponse{
    daily: Daily;
    dailyUnits: DailyUnits;
}

interface Hourly {
    time: string[];
    pm10: number[];
    pm2_5: number[];
    carbon_monoxide: number[];
    nitrogen_dioxide: number[];
    sulphur_dioxide: number[];
}

interface HourlyUnits {
    time: string;
    pm10: string;
    pm2_5: string;
    carbon_monoxide: string;
    nitrogen_dioxide: string;
    sulphur_dioxide: string;
}

export interface AirQuality extends WeatherApiResponse {
    hourly: Hourly;
    HourlyUnits: HourlyUnits;
}
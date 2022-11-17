package pwr.smart.home.control.weather.model;


public class ForecastWeatherRequest extends WeatherRequest {
    public final static String[] hourlyParameters = {
            "temperature_2m", "relativehumidity_2m", "weathercode",
            "cloudcover", "windspeed_10m"
    };
    public final static String[] dailyParameters = {
            "temperature_2m_max", "temperature_2m_min", "apparent_temperature_max",
            "apparent_temperature_min", "precipitation_hours", "weathercode"
    };

    private String[] daily;
    private boolean current_weather;

    public ForecastWeatherRequest(Float latitude, Float longitude, String startDate, String endDate, String[] hourly) {
        super(latitude, longitude, startDate, endDate, hourly);
    }

    public ForecastWeatherRequest(Float latitude, Float longitude, String startDate, String endDate, String[] hourly, boolean current_weather) {
        super(latitude, longitude, startDate, endDate, hourly);
        this.current_weather = current_weather;
    }

    public ForecastWeatherRequest(Float latitude, Float longitude, String startDate, String endDate, String[] hourly, boolean current_weather, String[] daily) {
        super(latitude, longitude, startDate, endDate, hourly);
        this.current_weather = current_weather;
        this.daily = daily;
    }

    public ForecastWeatherRequest(Float latitude, Float longitude, String[] daily, boolean current_weather) {
        super(latitude, longitude);
        this.daily = daily;
        this.current_weather = current_weather;
    }

    @Override
    public String toString() {
        return super.toString() +
                "&daily=" + String.join(",", daily) +
                "&current_weather=" + current_weather;
    }
}

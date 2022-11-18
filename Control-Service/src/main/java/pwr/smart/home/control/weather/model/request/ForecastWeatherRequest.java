package pwr.smart.home.control.weather.model.request;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ForecastWeatherRequest extends WeatherRequest {
    public final static String[] hourlyParameters = {
            "temperature_2m", "relativehumidity_2m", "weathercode",
            "cloudcover", "windspeed_10m"
    };

    public final static String[] dailyParameters = {
            "temperature_2m_max", "temperature_2m_min", "apparent_temperature_max",
            "apparent_temperature_min", "precipitation_hours", "weathercode"
    };

    private final String[] daily;
    private final boolean current_weather;

    public ForecastWeatherRequest(Float latitude, Float longitude, String[] daily) {
        super(latitude, longitude);
        this.daily = daily;
        this.current_weather = false;
        setTodayAndTomorrowsDate();
    }

    private void setTodayAndTomorrowsDate() {
        String today = new SimpleDateFormat(pattern).format(new Date());
        this.setStartDate(today);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        String formattedTomorrow = new SimpleDateFormat(pattern).format(tomorrow);
        this.setEndDate(formattedTomorrow);
    }

    @Override
    public String toString() {
        return super.toString() +
                "&daily=" + String.join(",", daily) +
                "&current_weather=" + current_weather;
    }
}

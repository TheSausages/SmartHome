package pwr.smart.home.common.weather.model.request;


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

    private String[] daily;
    private final boolean current_weather;

    public ForecastWeatherRequest(Float latitude, Float longitude, String[] daily) {
        super(latitude, longitude);
        this.daily = daily;
        this.current_weather = true;
        setTodayAndTomorrowsDate();
    }

    public ForecastWeatherRequest(Float latitude, Float longitude, boolean current_weather) {
        super(latitude, longitude);
        this.current_weather = current_weather;
        setTodayDate();
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

    private void setTodayDate() {
        String today = new SimpleDateFormat(pattern).format(new Date());
        this.setStartDate(today);
        this.setEndDate(today);
    }

    @Override
    public String toString() {
        String str = super.toString() +
                "&current_weather=" + current_weather;
        if (daily != null)
            str += "&daily=" + String.join(",", daily);
        return str;
    }
}

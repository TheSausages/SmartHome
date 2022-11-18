package pwr.smart.home.control.weather.model.request;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AirQualityRequest extends WeatherRequest {
    private final static String[] hourlyParameters = {
            "pm10", "pm2_5", "carbon_monoxide",
            "nitrogen_dioxide", "sulphur_dioxide"
    };

    public AirQualityRequest(float latitude, float longitude, String startDate, String endDate, String[] hourly) {
        super(latitude, longitude, startDate, endDate, hourly);
    }

    public AirQualityRequest(float latitude, float longitude) {
        super(latitude, longitude);
        this.setHourly(hourlyParameters);
        String dateInString = new SimpleDateFormat(pattern).format(new Date());
        this.setStartDate(dateInString);
        this.setEndDate(dateInString);
    }

    @Override
    public String toString() {
        return super.toString();
    }

}

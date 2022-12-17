package pwr.smart.home.common.weather.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class WeatherRequest {
    protected final static String pattern = "yyyy-MM-dd";
    private Float latitude;
    private Float longitude;
    private String timezone;
    private String startDate;
    private String endDate;
    private String [] hourly;

    public WeatherRequest(Float latitude, Float longitude, String startDate, String endDate, String[] hourly) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = "auto";
        this.startDate = startDate;
        this.endDate = endDate;
        this.hourly = hourly;
    }

    public WeatherRequest(Float latitude, Float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timezone = "auto";
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("?")
                .append("latitude=").append(latitude)
                .append("&longitude=").append(longitude)
                .append("&timezone=").append(timezone)
                .append("&start_date=").append(getStartDate())
                .append("&end_date=").append(getEndDate());
        if (hourly != null)
            sb.append("&hourly=").append(String.join(",", hourly));
        return sb.toString();
    }
}

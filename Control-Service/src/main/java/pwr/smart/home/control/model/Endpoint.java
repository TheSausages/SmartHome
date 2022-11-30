package pwr.smart.home.control.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class Endpoint {
    @Value("${data-service.url}")
    private String dataServiceUrl;

    @Value("${air-conditioner.url}")
    private String airConditionerUrl;

    @Value("${air-humidifier.url}")
    private String airHumidifierUrl;

    @Value("${air-filter.url}")
    private String airFilterUrl;

    public String getAirConditionerUrl() {
        return airConditionerUrl + "/setTarget";
    }

    public String getAirFilterUrl() {
        return  airFilterUrl + "/setTarget";
    }

    public String getAirHumidifierUrl() {
        return airHumidifierUrl + "/setTarget";
    }

    public String getDataServiceUrl() {
        return dataServiceUrl;
    }
}

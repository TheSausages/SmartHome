package pwr.smart.home.control.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Endpoint {
    @Value("${data-service.url}")
    private String dataServiceUrl;

    @Value("${air-conditioner.url}")
    private String airConditionerUrl;

    @Value("${air-humidifier.url}")
    private String airHumidifierUrl;

    @Value("${air-filter.url}")
    private String airFilterUrl;

    public String getAirConditionerUrl(String serialNumber) {
        return airConditionerUrl + "/" + serialNumber;
    }

    public String getAirFilterUrl(String serialNumber) {
        return  airFilterUrl + "/" + serialNumber;
    }

    public String getAirHumidifierUrl(String serialNumber) {
        return airHumidifierUrl + "/" + serialNumber;
    }

    public String getDataServiceUrl() {
        return dataServiceUrl;
    }
}

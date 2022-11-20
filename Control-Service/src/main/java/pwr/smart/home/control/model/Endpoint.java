package pwr.smart.home.control.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class Endpoint {
//    AIR_HUMIDIFIER_URL("http://localhost:8082/setTarget"),
//    AIR_CONDITIONER_URL("http://localhost:8083/setTarget"),
//    AIR_FILTER_URL("http://localhost:8084/setTarget"),
//    DATA_SERVICE_URL("http://localhost:8081/api/data");
//
//    public final String url;
//
//    Endpoint(String url) {
//        this.url = url;
//    }

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
        return airFilterUrl + "/setTarget";
    }

    public String getAirHumidifierUrl() {
        return airHumidifierUrl + "/setTarget";
    }
}

package pwr.smart.home.air.conditioning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import pwr.smart.home.air.conditioning.sensor.model.Sensor;
import pwr.smart.home.air.conditioning.sensor.model.SensorType;
import pwr.smart.home.air.conditioning.sensor.model.TemperatureData;
import pwr.smart.home.air.conditioning.sensor.service.StatusService;
import pwr.smart.home.common.model.Location;

@SpringBootApplication
public class AirConditioningApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirConditioningApplication.class, args);

		StatusService.setCurrentTemperature(21.0);
	}

	@Bean
	public Sensor sensor() {
		Sensor sensor = new Sensor();
		sensor.setBrand("KAISAI");
		sensor.setType(SensorType.TEMPERATURE);
		sensor.setSerialNumber("HIBWCDUIYHWASDAE");
		sensor.setLocation(new Location(51.10747999302326f,17.06091170465116f));
		return sensor;
	}
}

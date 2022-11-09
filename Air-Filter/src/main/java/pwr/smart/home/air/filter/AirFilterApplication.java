package pwr.smart.home.air.filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pwr.smart.home.air.filter.sensor.model.Sensor;
import pwr.smart.home.air.filter.sensor.model.SensorType;
import pwr.smart.home.air.filter.sensor.service.DataEmitter;
import pwr.smart.home.air.filter.sensor.service.StatusService;


@SpringBootApplication
public class AirFilterApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirFilterApplication.class, args);

		StatusService.setCurrentAirQuality(25.0);
	}

	@Bean
	public Sensor sensor() {
		Sensor sensor = new Sensor();
		sensor.setBrand("Philips");
		sensor.setType(SensorType.AIR_POLLUTION);
		sensor.setSerialNumber("HIBWCDUIYHWASDAD");

		return sensor;
	}

}

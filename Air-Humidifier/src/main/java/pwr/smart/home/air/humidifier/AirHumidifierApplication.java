package pwr.smart.home.air.humidifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pwr.smart.home.air.humidifier.sensor.service.DataEmitter;
import pwr.smart.home.air.humidifier.sensor.model.Sensor;
import pwr.smart.home.air.humidifier.sensor.model.SensorType;
import pwr.smart.home.air.humidifier.sensor.service.StatusService;

@SpringBootApplication
public class AirHumidifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirHumidifierApplication.class, args);

		StatusService.setCurrentHumidity(45.0);
	}

	@Bean
	public Sensor sensor() {
		Sensor sensor = new Sensor();
		sensor.setBrand("Philips");
		sensor.setType(SensorType.AIR_HUMIDITY);
		sensor.setSerialNumber("HIBWCDUIYHWASDAF");

		return sensor;
	}
}

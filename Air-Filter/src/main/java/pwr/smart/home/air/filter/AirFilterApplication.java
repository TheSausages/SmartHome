package pwr.smart.home.air.filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pwr.smart.home.air.filter.sensor.model.Sensor;
import pwr.smart.home.air.filter.sensor.model.SensorType;
import pwr.smart.home.air.filter.sensor.service.StatusService;
import pwr.smart.home.common.model.Location;


@SpringBootApplication
public class AirFilterApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirFilterApplication.class, args);

		StatusService.setCurrentPM25(10.0);
		StatusService.setCurrentIAI(3);
		StatusService.setCurrentGas(2);
	}

	@Bean
	public Sensor sensor() {
		Sensor sensor = new Sensor();
		sensor.setBrand("Philips");
		sensor.setType(SensorType.AIR_POLLUTION);
		sensor.setSerialNumber("HIBWCDUIYHWASDAD");
		sensor.setLocation(new Location(51.10747999302326f,17.06091170465116f));
		return sensor;
	}

}

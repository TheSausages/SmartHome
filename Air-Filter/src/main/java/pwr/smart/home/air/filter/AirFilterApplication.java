package pwr.smart.home.air.filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pwr.smart.home.air.filter.sensor.model.Sensor;
import pwr.smart.home.air.filter.sensor.model.SensorType;
import pwr.smart.home.air.filter.sensor.service.DataEmitter;
import pwr.smart.home.air.filter.sensor.service.StatusService;


@SpringBootApplication
public class AirFilterApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirFilterApplication.class, args);
		Sensor sensor = new Sensor();
		sensor.setBrand("Philips");
		sensor.setType(SensorType.AIR_POLLUTION);
		sensor.setSerialNumber("HIBWCDUIYHWASDAD");

		StatusService.setCurrentAirQuality(25.0);

		DataEmitter dataEmitter = new DataEmitter(sensor);
		while (true) {
			dataEmitter.emit();
		}
	}

}

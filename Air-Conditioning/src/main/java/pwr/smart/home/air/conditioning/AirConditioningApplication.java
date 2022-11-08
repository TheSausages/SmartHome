package pwr.smart.home.air.conditioning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pwr.smart.home.air.conditioning.sensor.model.Sensor;
import pwr.smart.home.air.conditioning.sensor.model.SensorType;
import pwr.smart.home.air.conditioning.sensor.service.DataEmitter;
import pwr.smart.home.air.conditioning.sensor.service.StatusService;

@SpringBootApplication
public class AirConditioningApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirConditioningApplication.class, args);
		Sensor sensor = new Sensor();
		sensor.setBrand("KAISAI");
		sensor.setType(SensorType.TEMPERATURE);
		sensor.setSerialNumber("HIBWCDUIYHWASDAE");

		StatusService.setCurrentTemperature(24.0);

		DataEmitter dataEmitter = new DataEmitter(sensor);
		while (true) {
			dataEmitter.emit();
		}
	}

}

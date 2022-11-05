package pwr.smart.home.air.conditioning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pwr.smart.home.air.conditioning.sensor.DataEmitter;
import pwr.smart.home.air.conditioning.sensor.Sensor;
import pwr.smart.home.air.conditioning.sensor.SensorType;

@SpringBootApplication
public class AirConditioningApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirConditioningApplication.class, args);
		Sensor sensor = new Sensor();
		sensor.setBrand("KAISAI");
		sensor.setType(SensorType.TEMPERATURE);
		sensor.setSerialNumber("HIBWCDUIYHWASDAE");

		DataEmitter dataEmitter = new DataEmitter(sensor);
		while (true) {
			dataEmitter.emit();
		}
	}

}

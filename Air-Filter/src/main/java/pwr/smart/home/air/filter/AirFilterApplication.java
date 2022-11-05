package pwr.smart.home.air.filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pwr.smart.home.air.filter.sensor.DataEmitter;
import pwr.smart.home.air.filter.sensor.Sensor;
import pwr.smart.home.air.filter.sensor.SensorType;

@SpringBootApplication
public class AirFilterApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirFilterApplication.class, args);
		Sensor sensor = new Sensor();
		sensor.setBrand("Philips");
		sensor.setType(SensorType.AIR_POLLUTION);
		sensor.setSerialNumber("HIBWCDUIYHWASDAD");

		DataEmitter dataEmitter = new DataEmitter(sensor);
		while (true) {
			dataEmitter.emit();
		}
	}

}

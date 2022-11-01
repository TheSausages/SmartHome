package pwr.smart.home.air.filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AirFilterApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirFilterApplication.class, args);
		Sensor sensor = new Sensor();
		sensor.setBrand("Philips");
		sensor.setType("Air Purifier");
		sensor.setSerialNumber("HIBWCDUIYHWASDAD");

		DataEmitter dataEmitter = new DataEmitter(sensor);
		while (true) {
			dataEmitter.emit(sensor);
		}
	}

}

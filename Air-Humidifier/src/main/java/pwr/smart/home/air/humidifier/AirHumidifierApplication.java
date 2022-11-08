package pwr.smart.home.air.humidifier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pwr.smart.home.air.humidifier.sensor.DataEmitter;
import pwr.smart.home.air.humidifier.sensor.Sensor;
import pwr.smart.home.air.humidifier.sensor.SensorType;

@SpringBootApplication
public class AirHumidifierApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirHumidifierApplication.class, args);

		Sensor sensor = new Sensor();
		sensor.setBrand("Philips");
		sensor.setType(SensorType.AIR_HUMIDITY);
		sensor.setSerialNumber("HIBWCDUIYHWASDAF");

		DataEmitter dataEmitter = new DataEmitter(sensor);
		while (true) {
			dataEmitter.emit();
		}
	}

}

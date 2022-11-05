package pwr.smart.home.air.conditioning.sensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Random;

public class DataEmitter {
    private final Logger logger = LoggerFactory.getLogger(DataEmitter.class);
    private final String URL = "http://localhost:8081/api/data/temperature";
    private final Sensor sensor;

    public DataEmitter(Sensor sensor) {
        this.sensor = sensor;
    }

    public void emit() {
        sendToServer();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    private boolean sendToServer() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Serial-Number", sensor.getSerialNumber());

        TemperatureData data = getData();
        logger.info("SENT " + data);

        HttpEntity<TemperatureData> entity = new HttpEntity<>(data, headers);
        ResponseEntity<TemperatureData> response = restTemplate
                .exchange(URL, HttpMethod.POST, entity, TemperatureData.class);

        return response.getStatusCode() == HttpStatus.OK;
    }

    private TemperatureData getData() {
        TemperatureData data = new TemperatureData();
        data.setSerialNumber(sensor.getSerialNumber());
        data.setTimestamp(getSystemTimestamp());
        data.setType(sensor.getType());
        data.setTemperature(getTemperature());
        return data;
    }

    private int getTemperature() {
        Random random = new Random();
        return random.nextInt(26);
    }

    private Timestamp getSystemTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}

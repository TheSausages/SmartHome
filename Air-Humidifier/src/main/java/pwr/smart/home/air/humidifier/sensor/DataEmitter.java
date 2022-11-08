package pwr.smart.home.air.humidifier.sensor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Random;

public class DataEmitter {
    private final Logger logger = LoggerFactory.getLogger(DataEmitter.class);
    private final String URL = "http://localhost:8081/api/data/humidifier";
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

        AirHumidifierData data = getData();
        logger.info("SENT " + data);

        HttpEntity<AirHumidifierData> entity = new HttpEntity<>(data, headers);
        ResponseEntity<AirHumidifierData> response = restTemplate
                .exchange(URL, HttpMethod.POST, entity, AirHumidifierData.class);

        return response.getStatusCode() == HttpStatus.OK;
    }

    private AirHumidifierData getData() {
        AirHumidifierData data = new AirHumidifierData();
        data.setSerialNumber(sensor.getSerialNumber());
        data.setTimestamp(getSystemTimestamp());
        data.setType(sensor.getType());
        data.setHumidity(getHumidity());
        return data;
    }

    private int getHumidity() {
        Random random = new Random();
        int max = 65;
        int min = 45;
        return random.nextInt(max - min + 1) + min;
    }

    private Timestamp getSystemTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}

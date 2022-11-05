package pwr.smart.home.air.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Random;

public class DataEmitter {
    private final Logger logger = LoggerFactory.getLogger(DataEmitter.class);
    private final String URL = "http://localhost:8081/api/data/filter";
    private final Sensor sensor;

    DataEmitter(Sensor sensor) {
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

    public boolean sendToServer() {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Serial-Number", sensor.getSerialNumber());
        AirFilterData data = getData();

        logger.info("SENT " + data.toString());
        HttpEntity<AirFilterData> entity = new HttpEntity<>(data, headers);

        ResponseEntity<AirFilterData> response = restTemplate
                .exchange(URL, HttpMethod.POST, entity, AirFilterData.class);

        return response.getStatusCode() == HttpStatus.OK;
    }

    public AirFilterData getData() {
        AirFilterData data = new AirFilterData();
        data.setSerialNumber(sensor.getSerialNumber());
        data.setTimestamp(getSystemTimestamp());
        data.setType(sensor.getType());
        data.setPM25(getIAI());
        data.setIAI(getIAI());
        data.setGas(getIAI());
        return data;
    }

    private int getIAI() {
        Random random = new Random();
        return random.nextInt(30);
    }

    public Timestamp getSystemTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}

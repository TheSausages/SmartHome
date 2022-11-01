package pwr.smart.home.air.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Random;

public class DataEmitter {
    private final Logger logger = LoggerFactory.getLogger(DataEmitter.class);
    private String URL = "http://localhost:8081/api/data/filter";
    private Sensor sensor;

    DataEmitter(Sensor sensor) {
        this.sensor = sensor;
    }

    public void emit(Sensor sensor) {
        sendToServer(sensor);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
    }

    public boolean sendToServer(Sensor sensor) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        Data data = getData(sensor);

        logger.info("SENT " + data.toString());

        HttpEntity<Data> entity = new HttpEntity<>(data, headers);

        ResponseEntity<Data> response = restTemplate
                .exchange(URL, HttpMethod.POST, entity, Data.class);

        return response.getStatusCode() == HttpStatus.OK;
    }

    public Data getData(Sensor sensor) {
        Data data = new Data();
        data.setSerialNumber(sensor.getSerialNumber());
        data.setTimestamp(getSystemTimestamp());
        data.setType(sensor.getType());
        data.setPM10(getPM());
        data.setPM25(getPM());
        data.setIAI(getIAI());
        data.setGas(getIAI());
        return data;
    }

    private float getPM() {
        Random random = new Random();
        return random.nextFloat();
    }

    private int getIAI() {
        Random random = new Random();
        return random.nextInt(30);
    }

    public Timestamp getSystemTimestamp() {
        return new Timestamp(System.currentTimeMillis() / 1000);
    }
}

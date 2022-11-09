package pwr.smart.home.air.filter.sensor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import pwr.smart.home.air.filter.sensor.model.AirFilterData;
import pwr.smart.home.air.filter.sensor.model.Sensor;


import java.sql.Timestamp;
import java.util.Random;

@Service
public class DataEmitter {
    private final Logger logger = LoggerFactory.getLogger(DataEmitter.class);

    @Value("${data-service.endpoint-url}")
    private String URL;

    private final Sensor sensor;

    public DataEmitter(Sensor sensor) {
        this.sensor = sensor;
    }

    @Scheduled(fixedDelay = 10000)
    public void sendToServer() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Serial-Number", sensor.getSerialNumber());

        AirFilterData data = getData();
        logger.info("SENT " + data);

        HttpEntity<AirFilterData> entity = new HttpEntity<>(data, headers);

        try {
            restTemplate.exchange(URL, HttpMethod.POST, entity, AirFilterData.class);
        }
        catch (ResourceAccessException e){
            logger.error(e.getMessage());
        }
    }

    private AirFilterData getData() {
        AirFilterData data = new AirFilterData();
        data.setSerialNumber(sensor.getSerialNumber());
        data.setTimestamp(getSystemTimestamp());
        data.setType(sensor.getType());
        data.setPM25((int) Math.round(StatusService.getCurrentAirQuality()));
        data.setIAI(StatusService.getIAI());
        data.setGas(getIAI());
        return data;
    }

    private int getIAI() {
        Random random = new Random();
        return random.nextInt(30);
    }

    private Timestamp getSystemTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}

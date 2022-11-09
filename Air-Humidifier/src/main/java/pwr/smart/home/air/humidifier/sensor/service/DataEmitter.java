package pwr.smart.home.air.humidifier.sensor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import pwr.smart.home.air.humidifier.sensor.model.AirHumidifierData;
import pwr.smart.home.air.humidifier.sensor.model.Sensor;

import java.sql.Timestamp;

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

        AirHumidifierData data = getData();
        logger.info("SENT " + data);

        HttpEntity<AirHumidifierData> entity = new HttpEntity<>(data, headers);

        try {
            restTemplate.exchange(URL, HttpMethod.POST, entity, AirHumidifierData.class);
        } catch (ResourceAccessException e) {
            logger.error(e.getMessage());
        }
    }

    private AirHumidifierData getData() {
        AirHumidifierData data = new AirHumidifierData();
        data.setSerialNumber(sensor.getSerialNumber());
        data.setTimestamp(getSystemTimestamp());
        data.setType(sensor.getType());
        data.setHumidity((int) Math.round(StatusService.getCurrentHumidity()));
        return data;
    }


    private Timestamp getSystemTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}

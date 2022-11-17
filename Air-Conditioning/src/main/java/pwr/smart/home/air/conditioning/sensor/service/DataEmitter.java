package pwr.smart.home.air.conditioning.sensor.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import pwr.smart.home.air.conditioning.sensor.model.Sensor;
import pwr.smart.home.air.conditioning.sensor.model.TemperatureData;

import java.sql.Timestamp;

@Service
public class DataEmitter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataEmitter.class);

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

        TemperatureData data = getData();
        LOGGER.info("SENT " + data);

        HttpEntity<TemperatureData> entity = new HttpEntity<>(data, headers);
        try {
            restTemplate.exchange(URL, HttpMethod.POST, entity, TemperatureData.class);
        }
        catch (ResourceAccessException e){
            LOGGER.error(e.getMessage());
        }
    }

    private TemperatureData getData() {
        TemperatureData data = new TemperatureData();
        data.setSerialNumber(sensor.getSerialNumber());
        data.setTimestamp(getSystemTimestamp());
        data.setType(sensor.getType());
        data.setTemperature(StatusService.getCurrentTemperature());
        return data;
    }


    private Timestamp getSystemTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}

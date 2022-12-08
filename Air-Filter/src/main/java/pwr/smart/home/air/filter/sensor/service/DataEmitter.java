package pwr.smart.home.air.filter.sensor.service;

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
import pwr.smart.home.air.filter.device.model.ConsumptionData;
import pwr.smart.home.air.filter.sensor.model.AirFilterData;
import pwr.smart.home.air.filter.sensor.model.Sensor;

import java.sql.Timestamp;

@Service
public class DataEmitter {
    private static final Logger LOGGER = LoggerFactory.getLogger(DataEmitter.class);

    @Value("${data-service.endpoint-url}")
    private String URL;

    @Value("${data-service.consumption-url}")
    private String consumptionURL;

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
        LOGGER.info("SENT " + data);

        HttpEntity<AirFilterData> entity = new HttpEntity<>(data, headers);

        try {
            restTemplate.exchange(URL, HttpMethod.POST, entity, AirFilterData.class);
        }
        catch (ResourceAccessException e){
            LOGGER.error(e.getMessage());
        }
    }

    public void reportConsumption(double consumption){
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Serial-Number", sensor.getSerialNumber());

        ConsumptionData data = new ConsumptionData(sensor.getSerialNumber(), consumption, getSystemTimestamp());
        LOGGER.info("SENT " + data);

        HttpEntity<ConsumptionData> entity = new HttpEntity<>(data, headers);
        try {
            restTemplate.exchange(consumptionURL, HttpMethod.POST, entity, ConsumptionData.class);
        }
        catch (ResourceAccessException e){
            LOGGER.error(e.getMessage());
        }
    }

    private AirFilterData getData() {
        AirFilterData data = new AirFilterData();
        data.setSerialNumber(sensor.getSerialNumber());
        data.setTimestamp(getSystemTimestamp());
        data.setType(sensor.getType());
        data.setPM25((int) Math.round(StatusService.getCurrentAirQuality()));
        data.setIAI(StatusService.getIAI());
        data.setGas(StatusService.getGas());
        return data;
    }

    private Timestamp getSystemTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}

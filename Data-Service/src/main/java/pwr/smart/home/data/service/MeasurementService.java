package pwr.smart.home.data.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pwr.smart.home.data.dao.Measurement;
import pwr.smart.home.data.dao.Sensor;
import pwr.smart.home.common.model.enums.SensorType;
import pwr.smart.home.data.model.MeasurementQueue;
import pwr.smart.home.data.repository.AggregatedMeasurementRepository;
import pwr.smart.home.data.repository.MeasurementRepository;
import pwr.smart.home.data.repository.SensorRepository;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;

@Service
public class MeasurementService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementService.class);
    private final long SEVEN_DAYS_IN_MILLISECONDS = 604_800_000;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private AggregatedMeasurementRepository aggregatedMeasurementRepository;

    @Autowired
    private SensorRepository sensorRepository;

    // Intellij doesn't see bean
    @Autowired(required = false)
    private JmsTemplate jmsTemplate;

    @Autowired
    private Queue queue;

    @Autowired
    private MessageConverter messageConverter;

    public boolean isSensorCompatibleType(String serialNumber, SensorType sensorType) {
        Optional<Sensor> sensor = sensorRepository.findBySerialNumber(serialNumber);
        return sensor.filter(value -> value.getType() == sensorType).isPresent();
    }

    public List<Measurement> getAllMeasurements(String sensorSerialNumber, Pageable pageableSetting) {
        Optional<Sensor> sensor = sensorRepository.findBySerialNumber(sensorSerialNumber);

        if (sensor.isPresent()) {
            Long sensorId = sensor.get().getId();
            return measurementRepository.findAllBySensorId(sensorId, pageableSetting);
        } else return new ArrayList<>();
    }

    public List<Measurement> getMeasurementsBetweenDates(String sensorSerialNumber, Timestamp from, Timestamp to) {
        Optional<Sensor> sensor = sensorRepository.findBySerialNumber(sensorSerialNumber);
        if (sensor.isPresent()) {
            if (to.getTime() - from.getTime() < SEVEN_DAYS_IN_MILLISECONDS) {
                return measurementRepository.findAllBySensorIdAndCreatedAtIsBetween(sensor.get().getId(), from, to);
            } else {
                return aggregatedMeasurementRepository.findAllBySensorIdAndCreatedAtIsBetween(sensor.get().getId(), from, to)
                        .stream().map(Measurement::fromAggregated).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    public void saveMeasurement(MeasurementQueue measurement) {
        // Send it to the queue
        jmsTemplate.convertAndSend(queue, measurement);
    }

    /**
     * This will be run every minute
     */
    @Scheduled(cron = "*/30 * * * * *")
    public void saveToDatabase() {
        LOGGER.info("Measurement queue saving started");

        List<MeasurementQueue> measurements = jmsTemplate.execute(session -> {
            Enumeration<?> enumeration = session.createBrowser(queue).getEnumeration();
            LOGGER.info("Nr. of messages: " + Collections.list(enumeration).size());

            try (final MessageConsumer consumer = session.createConsumer(queue)) {
                List<MeasurementQueue> messages = new ArrayList<>();
                Message message;
                while ((message = consumer.receiveNoWait()) != null) {
                    messages.add((MeasurementQueue) messageConverter.fromMessage(message));
                }

                return messages;
            } catch (Exception e) {
                LOGGER.error("error msg", e);
                return null;
            }
        }, true);

        if (Objects.nonNull(measurements) && !measurements.isEmpty()) {
            LOGGER.info("Saving " + measurements.size() + " elements");
            List<String> uniqueSensors = measurements.stream().map(MeasurementQueue::getSerialNumber).distinct().collect(Collectors.toList());

            Map<String, Long> map = sensorRepository
                    .findAllBySerialNumberIn(uniqueSensors)
                    .stream()
                    .collect(Collectors.toMap(Sensor::getSerialNumber, Sensor::getId));

            measurementRepository.saveAll(
                    measurements
                            .stream()
                            .map(measurementQueue ->
                                    new Measurement(
                                            measurementQueue.getType(),
                                            measurementQueue.getValue(),
                                            map.get(measurementQueue.getSerialNumber()),
                                            measurementQueue.getCreatedAt()
                                    ))
                            .collect(Collectors.toList())
            );
        } else {
            LOGGER.info("No elements saved");
        }
    }
}

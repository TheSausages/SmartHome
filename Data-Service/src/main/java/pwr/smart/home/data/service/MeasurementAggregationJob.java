package pwr.smart.home.data.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pwr.smart.home.common.model.enums.MeasurementType;
import pwr.smart.home.data.dao.Measurement;
import pwr.smart.home.data.dao.Measurement_Aggregated;
import pwr.smart.home.data.repository.AggregatedMeasurementRepository;
import pwr.smart.home.data.repository.MeasurementRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
public class MeasurementAggregationJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementAggregationJob.class);

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private AggregatedMeasurementRepository aggregatedMeasurementRepository;

    /**
     * This every day at 3 AM
     */
    // @Scheduled(cron = "* * 3 * * *")
    @Scheduled(cron = "*/50 * * * * *")
    public void addAggregationData() {
        LOGGER.info("Add aggregation data");

        // Get last aggregated one, so the date will  be the starting point for aggregation
        Optional<Measurement_Aggregated> lastAggregated = aggregatedMeasurementRepository.findTopByOrderByCreatedAtDesc();

        Timestamp startingDate = null;
        if (lastAggregated.isPresent()) {
            // If a value is present, we add 1 and use it as the starting date to aggregate further
            startingDate = Timestamp.valueOf(lastAggregated.get().getCreatedAt().toLocalDateTime().toLocalDate().plus(1, ChronoUnit.DAYS).atStartOfDay());
        } else {
            // If not, get the first measurement date and start from there
            Optional<Measurement> firstMeasurement = measurementRepository.findTopByOrderByCreatedAtAsc();

            // If there are no measurement at all, end the job
            if (firstMeasurement.isEmpty()) return;

            startingDate = firstMeasurement.get().getCreatedAt();
        }

        // Group the measurement by the sensor
        Map<Pair<Long, MeasurementType>, List<Measurement>> groupedMeasurements =  measurementRepository
                // Get all for a single day
                .getAllForDayGroupedBySensor(startingDate)
                .stream()
                // Map them into Map (sensorId, List<Measurements>)
                .collect(Collectors.groupingBy(ms -> Pair.of(ms.getSensorId(), ms.getType()), toList()));

        // Group the measurement into 6h intervals
        Map<Pair<Long, MeasurementType>, Map<Integer, List<Measurement>>> groupedTimedMeasurements = groupedMeasurements
                .entrySet()
                .stream()
                // Map the measurements into Map (sensorId, Map (interval, List<Measurements>))
                .collect(Collectors.toMap(Map.Entry::getKey,
                        // This groups the measurement into keys of:
                        //      - 0 (from 0.00 to 5.59),
                        //      - 1 (from 6.00 to 11.59),
                        //      - 2 (from 12.00 to 17.59)
                        //      - 3 (from 18:00 to 23.59)
                        entry -> entry.getValue().stream().collect(Collectors.groupingBy(ms -> ms.getCreatedAt().toLocalDateTime().getHour() / 6))));

        List<Measurement_Aggregated> agg = groupedTimedMeasurements
                .values()
                .stream()
                .map(integerListMap -> integerListMap
                        .entrySet()
                        .stream()
                        // Only use the itervals, where measurements exist
                        .filter(pair -> !pair.getValue().isEmpty())
//                        .collect(Collectors.groupingBy(entry -> entry.getKey()))
                        .map(pair -> {
                            // Get one measurement for information
                            Measurement exampleMs = pair.getValue().get(0);

                            // Get the average value in the time period
                            OptionalDouble meanValue = pair.getValue()
                                    .stream()
                                    .mapToDouble(Measurement::getValue)
                                    .average();

                            // Map the interval into a timestamp
                            Timestamp time = mapIntervalToTimestamp(pair.getKey(), exampleMs.getCreatedAt());

                            // As we filtered the empty lists out earlier we always have a value
                            return new Measurement_Aggregated(exampleMs.getType(), meanValue.getAsDouble(), exampleMs.getSensorId(), time);
                        })
                        .collect(toList()))
                // Flatmap the list of lists (each sensor had a list of max 4 elemements) into a single list (get rid of the sensorId index)
                .flatMap(List::stream)
                .collect(toList());

        // save the aggregations
        aggregatedMeasurementRepository.saveAll(agg);
    }

    private Timestamp mapIntervalToTimestamp(int interval, Timestamp timestamp) {
        LocalDate date = timestamp.toLocalDateTime().toLocalDate();
        switch (interval) {
            case 0:
                return Timestamp.valueOf(date.atTime(LocalTime.of(3, 0)));
            case 1:
                return Timestamp.valueOf(date.atTime(LocalTime.of(9, 0)));
            case 2:
                return Timestamp.valueOf(date.atTime(LocalTime.of(15, 0)));
            case 3:
                return Timestamp.valueOf(date.atTime(LocalTime.of(21, 0)));
            default:
                throw new RuntimeException("Interval mapped to unknown group!");
        }

    }
}

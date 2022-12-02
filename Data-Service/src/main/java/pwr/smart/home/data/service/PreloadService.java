package pwr.smart.home.data.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import pwr.smart.home.data.dao.Measurement;
import pwr.smart.home.data.model.enums.MeasurementType;
import pwr.smart.home.data.repository.MeasurementRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Configuration
public class PreloadService {

    @Bean
    CommandLineRunner loadCSV(MeasurementRepository measurementRepository) {
        return args -> {

            String line = "";
            String splitBy = ";";
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource("temp.csv").getInputStream()));
                int i = 0;
                while ((line = br.readLine()) != null) {
                    String[] record = line.split(splitBy);

                    float temperatureValue = Float.parseFloat(record[1]);
                    int humidityValue = Integer.parseInt(record[2]);
                    int pm25Value = Integer.parseInt(record[3]);

                    LocalDateTime date = LocalDateTime.now().minusHours(96-i);
                    Timestamp timestamp = Timestamp.valueOf(date);

                    measurementRepository.save(new Measurement(MeasurementType.CELSIUS, temperatureValue, 2L, timestamp));
                    measurementRepository.save(new Measurement(MeasurementType.HUMIDITY, humidityValue, 3L, timestamp));
                    measurementRepository.save(new Measurement(MeasurementType.PM25, pm25Value, 1L, timestamp));
                    measurementRepository.save(new Measurement(MeasurementType.GAS, UnitConversionService.resolveGas(pm25Value), 1L, timestamp));
                    measurementRepository.save(new Measurement(MeasurementType.IAI, UnitConversionService.resolveIAI(pm25Value), 1L, timestamp));
                    i++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }
}
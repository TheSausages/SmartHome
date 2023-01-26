package pwr.smart.home.data;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.YearMonth;

@SpringBootApplication
@EnableScheduling
public class DataServiceApplication {
	public static void main(String[] args) throws IOException {
//			BufferedWriter writer = new BufferedWriter(new FileWriter("./test.txt", true));
//
//			for (int i = 8; i < 13; i++) {
//				for (int j = 1; j < YearMonth.of(2022, i).lengthOfMonth() + 1; j++) {
//					for (int x = 0; x < 24; x++) {
//						for (int y = 0; y <= 59; y+= 5) {
//							writer.write("2022-" + String.format("%02d", i) + "-" + String.format("%02d", j)  + "T" + String.format("%02d", x) + ":" + String.format("%02d", y) + ":00.000+01:00;23.35;28;2");
//							writer.newLine();
//						}
//					}
//				}
//			}
//
//			for (int j = 1; j < YearMonth.of(2023, 1).lengthOfMonth() + 1; j++) {
//				for (int x = 0; x < 24; x++) {
//					for (int y = 0; y <= 59; y+= 5) {
//						writer.write("2023-01-" + String.format("%02d", j)  + "T" + String.format("%02d", x) + ":" + String.format("%02d", y) + ":00.000+01:00;23.35;28;2");
//						writer.newLine();
//					}
//				}
//			}
//
//			writer.close();

		SpringApplication.run(DataServiceApplication.class, args);
	}
}

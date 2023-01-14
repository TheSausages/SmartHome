package pwr.smart.home.selenium;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pwr.smart.home.selenium.pages.KeycloakLoginPage;
import pwr.smart.home.selenium.pages.SmartHomeHistoryPage;
import pwr.smart.home.selenium.pages.SmartHomeMainPage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SensorHistoryTest extends BaseSeleniumTest {
    private final static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");

    @Test
    public void seeSensorHistory() {
        driver.navigate().to(getProperty("frontend.url"));

        // Log into keycloak and wait for redirect
        KeycloakLoginPage keycloakLoginPage = new KeycloakLoginPage(driver);
        keycloakLoginPage.loginUsingTestAccountAndWaitForElement("history");

        // Click history page
        SmartHomeMainPage mainPage = new SmartHomeMainPage(driver);
        mainPage.getHistoryElement().click();

        var timings = Stream
                .of(
                        LocalDate.of(2023, 1, 30),
                        LocalDate.of(2023, 1, 25),
                        LocalDate.of(2023, 1, 20),
                        LocalDate.of(2023, 1, 15),
                        LocalDate.of(2023, 1, 10),
                        LocalDate.of(2023, 1, 5),
                        LocalDate.of(2023, 1, 1)
                )
                .collect(Collectors.toMap(fromDate -> fromDate, this::getSensorHistoryForDuration));

        CsvMapper mapper = new CsvMapper();
        mapper.findAndRegisterModules();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        try(FileOutputStream strW = new FileOutputStream("./history-times.csv")) {
            var seqW = mapper
                    .writerFor(HistoryTestCSVSchema.class)
                    .with(mapper.schemaFor(HistoryTestCSVSchema.class).withHeader())
                    .writeValues(strW);

            timings.forEach((key, value) -> {
                ResourceTimings.PerformanceResourceTiming timing = value.getResourceTimings().getTimingsForResources("http://data-service:8081/api/data/measurements?sensorSerialNumber=HIBWCDUIYHWASDAE&fromDate=" + key.format(DateTimeFormatter.ofPattern("yyy-MM-dd"))).get(0);
                try {
                    seqW.write(new HistoryTestCSVSchema(
                            key,
                            timing.getFetchStart(),
                            timing.getResponseEnd(),
                            timing.getResponseEnd() - timing.getFetchStart(),
                            value.getAnimationStart(),
                            value.getAnimationEnd(),
                            value.getAnimationEnd().toEpochSecond(ZoneOffset.UTC) - value.getAnimationStart().toEpochSecond(ZoneOffset.UTC)
                    ));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private TimingsWithAnimationTimings getSensorHistoryForDuration(LocalDate fromDate) {
        LocalDate toDate = LocalDate.of(2023, 1, 31);

        // Click the dates and get the chart
        SmartHomeHistoryPage historyPage = new SmartHomeHistoryPage(driver);
        historyPage.getStartDateElement().sendKeys(fromDate.format(formatter));
        historyPage.getEndDateElement().sendKeys(toDate.format(formatter));
        historyPage.getHistoryButton().click();

        // Wait for a response and check if chart exists
        WebDriverWait wait = new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("chart")));

        // Assert some charts exist
        assertThat(historyPage.getCharts(), iterableWithSize(greaterThanOrEqualTo(1)));

        waitForItemInLocalStorage("Animation-Start", driver);
        waitForItemInLocalStorage("Animation-End", driver);

        TimingsWithAnimationTimings timings = getResourceTimingsWithAnimationTimes(driver);

        // Reset the page
        historyPage.getSettingsElement().click();
        historyPage.getHistoryElement().click();

        // We can use timings to get request values
        return timings;
    }
}

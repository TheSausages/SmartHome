package pwr.smart.home.selenium;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.junit.jupiter.api.Test;
import pwr.smart.home.selenium.pages.KeycloakLoginPage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MainPagePerformanceTest extends BaseSeleniumTest {
    @Test
    public void checkMainPageMetrics() {
        driver.navigate().to(getProperty("frontend.url"));

        // Log into keycloak and wait for redirect
        KeycloakLoginPage keycloakLoginPage = new KeycloakLoginPage(driver);

        // Wait for the menu to load
        keycloakLoginPage.loginUsingTestAccountAndWaitForElement("history");

        // Get performance and resource values
        Performance performance = getPerformanceMetrics(driver);
        ResourceTimings timings = getResourceTimings(driver);

        // Check if we get the expected metrics
        assertThat(performance, notNullValue());

        assertThat(timings.getTimings(), not(emptyIterable()));
        assertThat(timings.getTimingsForResources("home.jpg"), allOf(
                notNullValue(),
                not(emptyIterable())
        ));

        long fullyLoadTime = performance.getTiming().getLoadEventEnd() - performance.getTiming().getNavigationStart();
        Double timeToFirstByteSite = timings.getTimingsForResources("http://frontend/static/js").stream().map(ResourceTimings.PerformanceResourceTiming::getResponseStart).min(Double::compareTo).get();
        Double timeToFirstByteImage = timings.getTimingsForResources("home.jpg").get(0).getResponseStart();
        long timeToTitle = performance.getTiming().getResponseEnd();

        CsvMapper mapper = new CsvMapper();
        mapper.findAndRegisterModules();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        try(FileOutputStream strW = new FileOutputStream("./main-page-times.csv")) {
            var seqW = mapper
                    .writerFor(MainPageTestCSVSchema.class)
                    .with(mapper.schemaFor(MainPageTestCSVSchema.class).withHeader())
                    .writeValues(strW);

            seqW.write(new MainPageTestCSVSchema(
                    fullyLoadTime,
                    timeToTitle,
                    timeToFirstByteImage,
                    timeToFirstByteSite
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

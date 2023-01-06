package pwr.smart.home.selenium;

import org.apache.commons.configuration2.CombinedConfiguration;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.EnvironmentConfiguration;
import org.apache.commons.configuration2.builder.fluent.Configurations;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class BaseSeleniumTest {
    protected static Logger logger = Logger.getLogger(BaseSeleniumTest.class.getName());

    protected WebDriver driver;
    protected static Configuration configuration;

    @BeforeAll
    public static void loadProperties() {
        try
        {
            CombinedConfiguration cc = new CombinedConfiguration();
            // If exist, take from environment
            cc.addConfiguration(new EnvironmentConfiguration());

            // If not take from properties file with default values
            cc.addConfiguration(new Configurations().properties(new File("application.properties")));

            configuration = cc;
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();

        // Uncomment to add F12 by default
        // options.addArguments("--auto-open-devtools-for-tabs");

        driver = Objects.nonNull(getProperty("grid.url")) ?
                new RemoteWebDriver(new URL(getProperty("grid.url")), options) :
                new ChromeDriver(options);
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public String getProperty(String key) {
        String val = configuration.getString(key);

        if (Objects.nonNull(val)) {
            return val;
        } else {
            return configuration.getString(key.toUpperCase());
        }
    }

    public Object executeScript(WebDriver driver, String script) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;

        return jse.executeScript(script);
    }

    @SuppressWarnings("unchecked")
    public Performance getPerformanceMetrics(WebDriver driver) {
        Map<String, Object> main = (Map<String, Object>) executeScript(driver, "return window.performance");
        Map<String, Long> timing = (Map<String, Long>) executeScript(driver, "return window.performance.timing");
        Map<String, Long> memory = (Map<String, Long>) executeScript(driver, "return window.performance.memory");

        return Performance.builder()
                .timeOrigin(((Double) main.get("timeOrigin")).longValue())
                .memory(Performance.Memory.builder()
                        .jsHeapSizeLimit(memory.get("jsHeapSizeLimit"))
                        .totalJSHeapSize(memory.get("totalJSHeapSize"))
                        .usedJSHeapSize(memory.get("usedJSHeapSize"))
                        .build())
                .timing(Performance.Timing.builder()
                        .connectEnd(timing.get("connectEnd"))
                        .connectStart(timing.get("connectStart"))
                        .domComplete(timing.get("domComplete"))
                        .domContentLoadedEventEnd(timing.get("domContentLoadedEventEnd"))
                        .domContentLoadedEventStart(timing.get("domContentLoadedEventStart"))
                        .domInteractive(timing.get("domInteractive"))
                        .domLoading(timing.get("domLoading"))
                        .domainLookupEnd(timing.get("domainLookupEnd"))
                        .domainLookupStart(timing.get("domainLookupStart"))
                        .fetchStart(timing.get("fetchStart"))
                        .loadEventEnd(timing.get("loadEventEnd"))
                        .loadEventStart(timing.get("loadEventStart"))
                        .navigationStart(timing.get("navigationStart"))
                        .redirectEnd(timing.get("redirectEnd"))
                        .redirectStart(timing.get("redirectStart"))
                        .requestStart(timing.get("requestStart"))
                        .responseEnd(timing.get("responseEnd"))
                        .responseStart(timing.get("responseStart"))
                        .secureConnectionStart(timing.get("secureConnectionStart"))
                        .unloadEventEnd(timing.get("unloadEventEnd"))
                        .unloadEventStart(timing.get("unloadEventStart"))
                        .build())
                .build();
    }
}

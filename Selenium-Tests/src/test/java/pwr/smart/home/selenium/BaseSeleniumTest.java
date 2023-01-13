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
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
            cc.addConfiguration(new Configurations().properties(new File("application-docker.properties")));

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

    public void waitForItemInLocalStorage(String name, WebDriver driver) {
        while(Objects.isNull(executeScript(driver, "return localStorage.getItem('" + name + "')"))) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void cleanLocalStorage(String name, WebDriver driver) {
        executeScript(driver, "localStorage.removeItem('" + name + "')");
    }

    public Object executeScript(WebDriver driver, String script) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;

        return jse.executeScript(script);
    }

    public TimingsWithAnimationTimings getResourceTimingsWithAnimationTimes(WebDriver driver) {
        ResourceTimings timings = getResourceTimings(driver);
        String startString = (String) executeScript(driver, "return localStorage.getItem('Animation-Start')");
        String endString = (String) executeScript(driver, "return localStorage.getItem('Animation-End')");

        // Get rid of 'Z' at the end
        startString = startString.substring(0, startString.length() - 1);
        endString = endString.substring(0, endString.length() - 1);

        LocalDateTime start = LocalDateTime.parse(startString);
        LocalDateTime end = LocalDateTime.parse(endString);

        cleanLocalStorage("Animation-Start", driver);
        cleanLocalStorage("Animation-End", driver);

        return TimingsWithAnimationTimings.builder()
                .resourceTimings(timings)
                .animationStart(start)
                .animationEnd(end)
                .build();
    }

    @SuppressWarnings("unchecked")
    public ResourceTimings getResourceTimings(WebDriver driver) {
        List<Map<String, Object>> main = (List<Map<String, Object>>) executeScript(driver, "return performance.getEntriesByType(\"resource\")");

        List<ResourceTimings.PerformanceResourceTiming> timings = main.stream().map(pure ->
                ResourceTimings.PerformanceResourceTiming.builder()
                        .connectEnd(pure.get("connectEnd"))
                        .connectStart(pure.get("connectStart"))
                        .decodedBodySize(pure.get("decodedBodySize"))
                        .domainLookupStart(pure.get("domainLookupStart"))
                        .domainLookupEnd(pure.get("domainLookupEnd"))
                        .duration(pure.get("duration"))
                        .encodedBodySize(pure.get("encodedBodySize"))
                        .fetchStart(pure.get("fetchStart"))
                        .redirectEnd(pure.get("redirectEnd"))
                        .redirectStart(pure.get("redirectStart"))
                        .requestStart(pure.get("requestStart"))
                        .responseEnd(pure.get("responseEnd"))
                        .responseStart(pure.get("responseStart"))
                        .secureConnectionStart(pure.get("secureConnectionStart"))
                        .startTime(pure.get("startTime"))
                        .transferSize(pure.get("transferSize"))
                        .workerStart(pure.get("workerStart"))
                        .entryType((String) pure.get("entryType"))
                        .initiatorType((String) pure.get("initiatorType"))
                        .name((String) pure.get("name"))
                        .nextHopProtocol((String) pure.get("nextHopProtocol"))
                        .renderBlockingStatus((String) pure.get("renderBlockingStatus"))
                        .serverTiming(new Object[]{pure.get("serverTiming")})
                        .build())
                .collect(Collectors.toList());

        return new ResourceTimings(timings);
    }

    @SuppressWarnings("unchecked")
    public Performance getPerformanceMetrics(WebDriver driver) {
        Map<String, Object> main = (Map<String, Object>) executeScript(driver, "return window.performance");
        Map<String, Long> timing = (Map<String, Long>) executeScript(driver, "return window.performance.timing");
        Map<String, Long> memory = (Map<String, Long>) executeScript(driver, "return window.performance.memory");

        return Performance.builder()
                .timeOrigin(main.get("timeOrigin"))
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

package pwr.smart.home.selenium;

import org.junit.jupiter.api.Test;
import pwr.smart.home.selenium.pages.KeycloakLoginPage;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MainPageTest extends BaseSeleniumTest {
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
    }
}

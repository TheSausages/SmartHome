package pwr.smart.home.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pwr.smart.home.selenium.pages.KeycloakLoginPage;
import pwr.smart.home.selenium.pages.SmartHomeMainPage;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class MainPageWeatherTest extends BaseSeleniumTest {
    @Test
    public void changeTemperatureTest() {
        driver.navigate().to(getProperty("frontend.url"));

        // Log into keycloak and wait for redirect
        KeycloakLoginPage keycloakLoginPage = new KeycloakLoginPage(driver);

        // Wait for the menu to load
        keycloakLoginPage.loginUsingTestAccountAndWaitForElement("Temperature-Today-Max");

        // Check if the weather elements exists
        SmartHomeMainPage mainPage = new SmartHomeMainPage(driver);

        assertThat(mainPage.getTodayMaxTemperature().getText(), notNullValue());
        assertThat(mainPage.getTodayMinTemperature().getText(), notNullValue());
        assertThat(mainPage.getTomorrowMaxTemperature().getText(), notNullValue());
        assertThat(mainPage.getTomorrowMinTemperature().getText(), notNullValue());
    }
}

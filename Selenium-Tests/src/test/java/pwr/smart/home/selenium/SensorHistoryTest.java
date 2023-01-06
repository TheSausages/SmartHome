package pwr.smart.home.selenium;

import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pwr.smart.home.selenium.pages.KeycloakLoginPage;
import pwr.smart.home.selenium.pages.SmartHomeHistoryPage;
import pwr.smart.home.selenium.pages.SmartHomeMainPage;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SensorHistoryTest extends BaseSeleniumTest {

    @Test
    public void seeSensorHistory() {
        driver.navigate().to(getProperty("frontend.url"));

        // Log into keycloak and wait for redirect
        KeycloakLoginPage keycloakLoginPage = new KeycloakLoginPage(driver);
        keycloakLoginPage.loginUsingTestAccountAndWaitForElement("history");

        // Click history page
        SmartHomeMainPage mainPage = new SmartHomeMainPage(driver);
        mainPage.getHistoryElement().click();

        Performance performance = getPerformanceMetrics(driver);

        // Click the dates and get the chart
        SmartHomeHistoryPage historyPage = new SmartHomeHistoryPage(driver);
        historyPage.getStartDateElement().sendKeys("01052023");
        historyPage.getEndDateElement().sendKeys("01062023");
        historyPage.getHistoryButton().click();
        historyPage.getHistoryButton().click();

        // Wait for a response and check if chart exists
        WebDriverWait wait = new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS));
        wait.until(ExpectedConditions.elementToBeClickable(By.id("chart")));

        // Assert some charts exist
        assertThat(historyPage.getCharts(), iterableWithSize(greaterThanOrEqualTo(1)));
    }
}

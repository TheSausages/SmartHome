package pwr.smart.home.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import pwr.smart.home.selenium.pages.KeycloakLoginPage;
import pwr.smart.home.selenium.pages.SmartHomeMainPage;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MainPageTemperatureTest extends BaseSeleniumTest {
    @Test
    public void changeTemperatureTest() {
        driver.navigate().to(getProperty("frontend.url"));

        // Log into keycloak and wait for redirect
        KeycloakLoginPage keycloakLoginPage = new KeycloakLoginPage(driver);

        // Wait for the menu to load
        keycloakLoginPage.loginUsingTestAccountAndWaitForElement("history");

        // Click on temperature option
        SmartHomeMainPage mainPage = new SmartHomeMainPage(driver);
        mainPage.getTemperatureSelect().click();

        // Select 30 degrees (but wait for the element to appear)
        WebElement tempElem = mainPage.getTemperatureItemWithTemperature(20);
        WebDriverWait wait = new WebDriverWait(driver, Duration.of(5, ChronoUnit.SECONDS));
        wait.until(ExpectedConditions.visibilityOf(tempElem));
        tempElem.click();


        // Reload the page and see, if the value is 30
        mainPage.getHistoryElement().click();
        mainPage.getHomeElement().click();

        // We get rid of the last 2 symbols (°C)
        String tempText = mainPage.getTemperatureSelect().getText();
        assertThat(tempText.substring(0, tempText.length() - 2), is("20"));
    }
}

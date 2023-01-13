package pwr.smart.home.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pwr.smart.home.selenium.pages.KeycloakLoginPage;
import pwr.smart.home.selenium.pages.SmartHomeMainPage;
import pwr.smart.home.selenium.pages.SmartHomeSettingsPage;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class SettingsPagePowerLevelTest extends BaseSeleniumTest {
    @Test
    public void setPowerLevel() {
        String serialNumber = "HIBWCDUIYHWASDAD";
        driver.navigate().to(getProperty("frontend.url"));

        // Log into keycloak and wait for redirect
        KeycloakLoginPage keycloakLoginPage = new KeycloakLoginPage(driver);

        // Wait for the menu to load
        keycloakLoginPage.loginUsingTestAccountAndWaitForElement("settings");

        // Click on temperature option
        SmartHomeMainPage mainPage = new SmartHomeMainPage(driver);
        mainPage.getSettingsElement().click();

        // Click on edit button for device
        SmartHomeSettingsPage settingsPage = new SmartHomeSettingsPage(driver);
        WebElement editButton = settingsPage.getEditSensorButton(serialNumber);
        editButton.click();

        // Wait for the edit menu to appear and set Power Level 3 (but wait for the element to appear)
        settingsPage.getEditPowerLevelSelect().click();
        WebElement powerLevelEdit = settingsPage.getPowerLevelItemWithTemperature(1);
        WebDriverWait menuWait = new WebDriverWait(driver, Duration.of(5, ChronoUnit.SECONDS));
        menuWait.until(ExpectedConditions.visibilityOf(powerLevelEdit));
        powerLevelEdit.click();

        // Submit the change
        WebElement submit = settingsPage.getEditSensorSubmitButton();
        submit.click();

        // Check if the value updated
        WebDriverWait changeWait = new WebDriverWait(driver, Duration.of(5, ChronoUnit.SECONDS));
        changeWait.until(ExpectedConditions.textToBe(By.id("Device-Table-Power-Level-" + serialNumber), "1"));

        String powerLevelText = settingsPage.getDevicePowerLevel(serialNumber).getText();
        assertThat(powerLevelText, is("1"));

    }
}

package pwr.smart.home.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SmartHomeSettingsPage extends PageWithNavbar {

    public SmartHomeSettingsPage(WebDriver driver) {
        super(driver);
    }

    public WebElement getEditSensorButton(String serialNumber) {
        return driver.findElement(By.id("Edit-Sensor-Button-" + serialNumber));
    }

    public WebElement getEditPowerLevelSelect() {
        return driver.findElement(By.id("Select-Power-Level"));
    }

    public WebElement getDevicePowerLevel(String serialNumber) {
        return driver.findElement(By.id("Device-Table-Power-Level-" + serialNumber));
    }

    public WebElement getPowerLevelItemWithTemperature(int powerLevel) {
        return driver.findElement(By.id("Select-Power-Level-" + powerLevel));
    }

    public WebElement getEditSensorSubmitButton() {
        return driver.findElement(By.id("Device-Edit-Submit"));
    }
}

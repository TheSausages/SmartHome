package pwr.smart.home.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SmartHomeMainPage extends PageWithNavbar {

    public SmartHomeMainPage(WebDriver driver) {
        super(driver);
    }

    public WebElement getTodayMaxTemperature() {
        return driver.findElement(By.id("Temperature-Today-Max"));
    }

    public WebElement getTodayMinTemperature() {
        return driver.findElement(By.id("Temperature-Today-Min"));
    }

    public WebElement getTomorrowMaxTemperature() {
        return driver.findElement(By.id("Temperature-Tomorrow-Max"));
    }

    public WebElement getTomorrowMinTemperature() {
        return driver.findElement(By.id("Temperature-Tomorrow-Min"));
    }

    public WebElement getTemperatureSelect() {
        return driver.findElement(By.id("Select-Temperature"));
    }

    public WebElement getTemperatureItemWithTemperature(int temperature) {
        return driver.findElement(By.id("Temperature-Options-" + temperature));
    }
}

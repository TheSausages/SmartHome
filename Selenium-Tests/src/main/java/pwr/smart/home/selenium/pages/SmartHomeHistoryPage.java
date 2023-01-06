package pwr.smart.home.selenium.pages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

@AllArgsConstructor
@Builder
public class SmartHomeHistoryPage {
    private final WebDriver driver;

    public WebElement getStartDateElement() {
        return driver.findElement(By.id("date-start"));
    }

    public WebElement getEndDateElement() {
        return driver.findElement(By.id("date-end"));
    }

    public WebElement getHistoryButton() {
        return driver.findElement(By.id("history-get-button"));
    }

    public List<WebElement> getCharts() {
        return driver.findElements(By.id("chart"));
    }
}

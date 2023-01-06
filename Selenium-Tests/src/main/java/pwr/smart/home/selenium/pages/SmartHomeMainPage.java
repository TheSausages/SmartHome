package pwr.smart.home.selenium.pages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@AllArgsConstructor
@Builder
public class SmartHomeMainPage {
    private final WebDriver driver;

    public WebElement getHistoryElement() {
        return driver.findElement(By.id("history"));
    }
}

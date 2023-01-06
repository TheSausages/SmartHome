package pwr.smart.home.selenium.pages;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@AllArgsConstructor
@Builder
public class KeycloakLoginPage {
    private final WebDriver driver;

    public WebElement getUsernameInput() {
        return driver.findElement(By.id("username"));
    }

    public WebElement getPasswordInput() {
        return driver.findElement(By.id("password"));
    }

    public WebElement getLoginButton() {
        return driver.findElement(By.id("kc-login"));
    }

    public WebDriverWait loginUsingTestAccount() {
        getUsernameInput().sendKeys("test");
        getPasswordInput().sendKeys("Password1");
        getLoginButton().click();

        // Wait for keycloak to redirect
        return new WebDriverWait(driver, Duration.of(10, ChronoUnit.SECONDS));
    }

    public void loginUsingTestAccountAndWaitForElement(String elementId) {
        // Wait for keycloak to redirect
        WebDriverWait wait = loginUsingTestAccount();

        wait.until(ExpectedConditions.elementToBeClickable(By.id(elementId)));
    }
}

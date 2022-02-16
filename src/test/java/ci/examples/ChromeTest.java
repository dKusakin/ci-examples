package ci.examples;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileInputStream;

import static io.qameta.allure.Allure.addAttachment;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChromeTest {

    private WebDriver driver;

    @DisplayName("Chrome Test on Agent")
    @Test
    void startChrome() {
        driver = step("Create ChromeDriver", () -> new ChromeDriver());

        step("Maximize browser window", () ->
                driver.manage().window().maximize());
        step("Open ya.ru", () -> {
            driver.get("https://ya.ru");
            addAttachment("ya.ru", "image/png", new FileInputStream(
                    ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE)), "png");
        });

        step("Check search field visible", () ->
                assertTrue(driver.findElement(By.className("input__box"))
                        .isDisplayed()));

    }

    @AfterEach
    void closeDriver() {
        if (driver != null)
            driver.close();
    }
}

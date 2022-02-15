package ci.examples;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChromeTest {

    private WebDriver driver;

    @Test
    void startChrome() {
        System.out.println(System.getProperty("webdriver.chrome.args"));
        driver = new ChromeDriver();
        driver.get("https://ya.ru");
        assertTrue(driver.findElement(By.className("input__box"))
                        .isDisplayed());
    }

    @AfterEach
    void closeDriver() {
        if (driver != null)
            driver.close();
    }
}

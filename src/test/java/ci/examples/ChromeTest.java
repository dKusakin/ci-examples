package ci.examples;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.qameta.allure.Allure.addAttachment;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ChromeTest {

    private WebDriver driver;

    @DisplayName("Chrome Test")
    @ParameterizedTest(name = "{displayName} - {0}")
    @MethodSource("driverProvider")
    void startChrome(String driverName, Supplier<WebDriver> webDriverSupplier) {
        driver = step("Create " + driverName, webDriverSupplier::get);

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

    private static Stream<Arguments> driverProvider() {
        return Stream.of(isRemoteSet() ?
                arguments("Selenoid ChromeDriver", (Supplier<WebDriver>) ChromeTest::createRemoteWebdriver) :
                arguments("local ChromeDriver", (Supplier<WebDriver>) ChromeDriver::new)
        );
    }

    private static boolean isRemoteSet() {
        final String remoteHubUrl = System.getenv("REMOTE_HUB_URL");
        return remoteHubUrl != null && !remoteHubUrl.isBlank();
    }

    private static WebDriver createRemoteWebdriver() {
        MutableCapabilities capabilities = new ChromeOptions();
        capabilities.setCapability("selenoid:options", Map.of(
                "enableVNC", true,
                "enableVideo", false
        ));
        capabilities.setCapability("screenResolution", "1920x1080x24");
        capabilities.setCapability("name", "CI Example");
        try {
            return new RemoteWebDriver(URI.create(System.getenv("REMOTE_HUB_URL")).toURL(), capabilities);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Cannot resolve hub url");
        }
    }

    @AfterEach
    void closeDriver() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }
}

package com.Sample.base;

import com.Sample.utils.ScreenshotUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.LogManager;

import com.Sample.utils.WebElementUtil;

public class BaseTest {
    protected WebDriver driver;
    protected WebElementUtil webElementUtil;

    @BeforeEach
    public void setUp() {
        // Initialize ChromeDriver with ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.addArguments("–-incognito"); // Optional: opens a new incognito window each time
        //options.addArguments("--headless");  // Enable headless mode
        options.addArguments("--disable-gpu");  // Disable GPU for headless mode
        options.addArguments("--window-size=1920,1080"); //resizing for headless mode
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();

        // Set up WebElementUtil with a timeout
        webElementUtil = new WebElementUtil(driver, Duration.ofSeconds(TestConfig.ELEMENT_TIMEOUT_SECONDS));

        // Add timestamp to Allure report to verify the log based on the timestamp later. log file path can be inside the log4net.config file for each service under Config folder.
        String timestamp = webElementUtil.getFormattedTimestamp();
        Allure.addAttachment("SetUp Timestamp", "text/plain", timestamp);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(TestConfig.IMPLICIT_WAIT_SECONDS));

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(TestConfig.PAGE_LOAD_TIMEOUT_SECONDS));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            try {
                ScreenshotUtil.takeScreenshot(driver);
                Allure.addAttachment("Screenshot", "image/png", new ByteArrayInputStream(ScreenshotUtil.takeScreenshot(driver)), "png");
            } finally {
                driver.quit();
            }
        }
        // Add timestamp to Allure report to verify the log based on the timestamp later. log file path can be inside the log4net.config file for each service under Config folder.
        String timestamp = webElementUtil.getFormattedTimestamp();
        Allure.addAttachment("TearDown Timestamp", "text/plain", timestamp);
    }
}

package com.Sample.utils;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Tag;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class WebElementUtil {

    private WebDriver driver;
    private WebDriverWait wait;

    public WebElementUtil(WebDriver driver, Duration timeoutDuration) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeoutDuration);
    }

    public boolean isClickable(WebElement element) {
        try {
            return element.isDisplayed() && element.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public void waitForElementToBeClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void dynamicWaitForElementToBeClickable(By element, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(element));
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (StaleElementReferenceException e) {
            wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(element)));
            wait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(element)));
        }
    }

    // Method to click an element using JavascriptExecutor
    public void clickElementByJavaScript(WebElement element) {
        try {
            JavascriptExecutor executor = (JavascriptExecutor) driver;
            executor.executeScript("arguments[0].click();", element);
        } catch (Exception e) {
            throw new RuntimeException("Error clicking element using JavaScript: " + e.getMessage());
        }
    }

    public void waitForElementVisibility(By locator) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception e) {
            throw new RuntimeException("Thread.Sleep exception");
        }
    }

    public void waitForNewTab() {
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
    }

    public int getColumnIndex(String columnName) {
        try {
            List<WebElement> headers = driver.findElement(By.xpath("//table")).findElements(By.xpath("//thead/tr/th[not(contains(@style, 'display: none;'))]"));
            for (int i = 1; i < headers.size(); i++) {
                if (headers.get(i).getText().trim().equals(columnName)) {
                    return i;
                }
            }
            throw new NoSuchElementException("Column with name '" + columnName + "' not found in the visible columns of the table.");
        } catch (Exception e) {
            throw new RuntimeException("Error while searching for column: " + e.getMessage());
        }
    }

    public void navigateBack() {
        driver.navigate().back();
    }

    public void jsWaitForPageLoad(long time) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(time));
        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete';"));
    }

    public void fluentWaitForElement(Duration timeout, Class<? extends Throwable> ignoreException, By elementToWaitFor) {
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(timeout)
                    .pollingEvery(Duration.ofMillis(500)) // Polling interval
                    .ignoring(ignoreException); // Ignore specific exceptions if needed
            wait.until(new Function<WebDriver, Boolean>() {
                public Boolean apply(WebDriver driver) {
                    boolean documentReadyState = ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                    // Check for the presence of the specified element
                    if (elementToWaitFor != null) {
                        try {
                            WebElement element = driver.findElement(elementToWaitFor);
                            return documentReadyState && element.isDisplayed();
                        } catch (NoSuchElementException | StaleElementReferenceException e) {
                            return false;
                        }
                    }
                    return documentReadyState;
                }
            });
        } catch (TimeoutException e) {
            System.out.println("Timeout fluent wait");
        }
    }

    public String getFormattedTimestamp() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }

    public void waitForElementToDisappear(By locator) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitForJavaScriptExecution() {
        wait.until(ExpectedConditions.jsReturnsValue("return (document.readyState == 'complete' && jQuery.active == 0)"));
    }

    public void compareTestTagWithProjectDomain(Method testMethod, String projectDomainName) {
        // Get all tags of the test method
        Tag[] tags = testMethod.getAnnotationsByType(Tag.class);
        // Check if either tag matches the project domain name
        boolean tagMatch = false;
        for (Tag tag : tags) {
            String tagValue = tag.value();
            if (projectDomainName.contains(tagValue.substring(0, 2))) {
                tagMatch = true;
                break;
            }
        }
        // Validate that at least one tag matches the project domain name
        Assumptions.assumeTrue(tagMatch, "Test skipped because tags do not match the project domain");
    }
    public void psopWait(Duration timeout, Class<? extends Throwable> ignoreException, By elementToWaitFor) {
        try {
            FluentWait<WebDriver> wait = new FluentWait<>(driver)
                    .withTimeout(timeout)
                    .pollingEvery(Duration.ofMillis(500)) // Polling interval
                    .ignoring(ignoreException); // Ignore specific exceptions if needed

            wait.until(new Function<WebDriver, Boolean>() {
                public Boolean apply(WebDriver driver) {
                    boolean documentReadyState = ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                    Object jqueryInactive = ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0");
                    boolean isJqueryInactive = jqueryInactive instanceof Boolean && (Boolean) jqueryInactive;
                    // Check for the presence of the specified element
                    if (elementToWaitFor != null) {
                        try {
                            WebDriverWait dynamicWait = new WebDriverWait(driver, Duration.ZERO);
                            dynamicWait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfElementLocated(elementToWaitFor)));
                            dynamicWait.until(ExpectedConditions.refreshed(ExpectedConditions.elementToBeClickable(elementToWaitFor)));
                            return documentReadyState && isJqueryInactive;
                        } catch (NoSuchElementException | StaleElementReferenceException e) {
                            return false;
                        }
                    }
                    return documentReadyState && isJqueryInactive;
                }
            });
        } catch (TimeoutException e) {
            System.out.println("Timeout fluent wait");
        }
    }
}

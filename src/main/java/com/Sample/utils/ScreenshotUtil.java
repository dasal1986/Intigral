package com.Sample.utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtil {

    public static byte[] takeScreenshot(WebDriver driver) {
        try {
            if (driver == null) {
                System.err.println("The WebDriver is null, screenshot cannot be taken");
                return new byte[0];
            }
            // Cast the driver to TakesScreenshot and get a screenshot as output type BYTES
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            System.err.println("Exception while taking screenshot: " + e.getMessage());
            return new byte[0];
        }
    }

}

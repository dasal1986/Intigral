package com.Sample.tests;

import com.Sample.base.BaseTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeviceCreation extends BaseTest {

    static {
        RestAssured.baseURI = "https://api.restful-api.dev";
    }

    @Test
    @Order(1)
    @Tag("SampleTest")
    public void testSubscriptionPackages() {
        driver.get("https://subscribe.stctv.com/sa-en");
        validatePackagesDisplayed("name-lite", "name-classic", "name-premium");
        validatePrices(new String[] {"currency-lite", "currency-classic", "currency-premium"}, new String[] {"15 SAR/month", "25 SAR/month", "60 SAR/month"});

        driver.findElement(By.xpath("//span[@id='country-name']")).click();
        driver.findElement(By.xpath("//span[@id='bh-contry-lable']")).click();
        validatePrices(new String[] {"currency-lite", "currency-classic", "currency-premium"}, new String[] {"2 BHD/month", "3 BHD/month", "6 BHD/month"});

        driver.findElement(By.xpath("//span[@id='country-name']")).click();
        driver.findElement(By.xpath("//div[@id='kw-contry-flag']")).click();
        validatePrices(new String[] {"currency-lite", "currency-classic", "currency-premium"}, new String[] {"1.2 KWD/month", "2.5 KWD/month", "4.8 KWD/month"});
    }

    private void validatePackagesDisplayed(String... ids) {
        for (String id : ids) {
            WebElement packageElement = driver.findElement(By.xpath("//strong[@id='" + id + "']"));
            assertTrue(packageElement.isDisplayed(), id.toUpperCase() + " package is not displayed");
        }
    }

    private void validatePrices(String[] ids, String[] expectedPrices) {
        for (int i = 0; i < ids.length; i++) {
            validatePrice(ids[i], expectedPrices[i]);
        }
    }

    private void validatePrice(String id, String expectedPrice) {
        WebElement priceElement = driver.findElement(By.id(id));
        String priceText = priceElement.getText().trim();
        assertEquals(expectedPrice, priceText, "Price for " + id + " is not as expected");
    }

    @Test
    @Order(2)
    @Tag("SampleTest")
    public void testAddDevice() {
        driver.get("https://api.restful-api.dev/objects");
        String requestBody = """
        {
            "name": "Apple Max Pro 1TB",
            "data": {
                "year": 2023,
                "price": 7999.99,
                "CPU model": "Apple ARM A7",
                "Hard disk size": "1 TB"
            }
        }
        """;

        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post("/objects");

        String deviceId = response.jsonPath().getString("id");
        String createdAt = response.jsonPath().getString("createdAt");

        response.then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("createdAt", notNullValue())
                .body("name", equalTo("Apple Max Pro 1TB"))
                .body("data.year", equalTo(2023))
                .body("data.price", equalTo(7999.99F))
                .body("data['CPU model']", equalTo("Apple ARM A7"))
                .body("data['Hard disk size']", equalTo("1 TB"));

        if (deviceId != null && createdAt != null) {
            System.out.println("Test Result: PASS");
            System.out.println("Device ID: " + deviceId);
            System.out.println("Created At: " + createdAt);
        } else {
            System.out.println("Test Result: FAIL");
            if (deviceId == null) System.out.println("Device ID is missing.");
            if (createdAt == null) System.out.println("Created At is missing.");
        }
    }
}
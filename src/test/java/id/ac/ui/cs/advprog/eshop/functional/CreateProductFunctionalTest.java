package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {
    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void createProduct_isSuccessful(ChromeDriver driver) throws Exception {
        String productName = "Sampo Cap Bambang " + UUID.randomUUID();
        int productQuantity = 100;

        driver.get(baseUrl + "/product/list");
        driver.findElement(By.id("createProductButton")).click();

        driver.findElement(By.id("productName")).sendKeys(productName);

        WebElement quantityInput = driver.findElement(By.id("productQuantity"));
        quantityInput.clear();
        quantityInput.sendKeys(String.valueOf(productQuantity));

        driver.findElement(By.id("submitButton")).click();

        List<WebElement> productRows = driver.findElements(By.cssSelector("tbody tr"));
        boolean isFound = false;
        for (WebElement productRow : productRows) {
            String name = productRow.findElement(By.className("productName")).getText();
            String quantity = productRow.findElement(By.className("productQuantity")).getText();

            if (productName.equals(name)) {
                assertEquals(String.valueOf(productQuantity), quantity);
                isFound = true;
                break;
            }
        }

        assertTrue(isFound);
    }
}


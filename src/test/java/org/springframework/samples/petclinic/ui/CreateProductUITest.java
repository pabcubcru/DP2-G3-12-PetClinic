package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateProductUITest {
	
	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();
	
	@LocalServerPort
	private int port;

	@BeforeEach
	public void setUp() throws Exception {
		System.setProperty("webdriver.gecko.driver", System.getenv("webdriver.gecko.driver"));
		driver = new FirefoxDriver();
		driver.get("http://localhost:" + port);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testCreateNewOrderSuccess() throws Exception {
		loginAsAdmin();
		fillCreateProductFormSuccess();
		checkProductHasBeenCreatedSuccess();
	}

	@Test
	public void testCreateNewProductProductNameDuplicated() throws Exception {
		loginAsAdmin();
		fillCreateProductFormProductNameDuplicatedAndCheckErrorMessage();
	}

	public void loginAsAdmin() throws Exception {
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("4dm1n");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	public void fillCreateProductFormSuccess() throws Exception {
		driver.findElement(By.xpath("//a[contains(@href, '/shops/1')]")).click();
		driver.findElement(By.linkText("Add Product")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("productUITest");
		driver.findElement(By.id("price")).clear();
		driver.findElement(By.id("price")).sendKeys("10");
		driver.findElement(By.id("stock")).clear();
		driver.findElement(By.id("stock")).sendKeys("50");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.linkText("productUITest")).click();
	}

	public void checkProductHasBeenCreatedSuccess() throws Exception {
		assertEquals("productUITest", driver.findElement(By.xpath("//td")).getText());
		assertEquals("10.0", driver.findElement(By.xpath("//tr[2]/td")).getText());
		assertEquals("50", driver.findElement(By.xpath("//tr[3]/td")).getText());
	}

	public void fillCreateProductFormProductNameDuplicatedAndCheckErrorMessage() throws Exception {
		driver.findElement(By.xpath("//a[contains(@href, '/shops/1')]")).click();
		driver.findElement(By.linkText("Add Product")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("product1");
		driver.findElement(By.id("price")).clear();
		driver.findElement(By.id("price")).sendKeys("10");
		driver.findElement(By.id("stock")).clear();
		driver.findElement(By.id("stock")).sendKeys("50");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("This name already exist",
				driver.findElement(By.xpath("//form[@id='add-product-form']/div/div/div/span[2]")).getText());
	}

	@AfterEach
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}
}

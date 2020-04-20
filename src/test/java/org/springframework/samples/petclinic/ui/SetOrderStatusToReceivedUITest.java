package org.springframework.samples.petclinic.ui;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SetOrderStatusToReceivedUITest {
	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();

	@LocalServerPort
	private int port;

	@BeforeEach
	public void setUp() throws Exception {
		String pathToGeckoDriver = "C:\\Users\\Pablo\\WebDriver";
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver + "\\geckodriver.exe");
		driver = new FirefoxDriver();
		driver.get("http://localhost:" + port);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testSetOrderStatusToReceivedSuccess() throws Exception {
		loginAsAdmin();
		setOrder2ReceivedAndCheckStatusSuccess();
	}

	@Test
	public void testSetOrderStatusToReceivedRedirectToExceptionView() throws Exception {
		loginAsAdmin();
		setOrder1ReceivedAndCheckStatusRedirectToExceptionView();
	}

	public void loginAsAdmin() throws Exception {
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("4dm1n");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	public void setOrder2ReceivedAndCheckStatusSuccess() throws Exception {
		driver.findElement(By.xpath("//a[contains(@href, '/shops/1')]")).click();
		driver.findElement(By.linkText("order2")).click();
		driver.findElement(By.xpath("//a[contains(text(),'Order\n			Received')]")).click();
		assertEquals("RECEIVED", driver.findElement(By.xpath("//tr[6]/td")).getText());
	}

	public void setOrder1ReceivedAndCheckStatusRedirectToExceptionView() throws Exception {
		driver.findElement(By.xpath("//a[contains(@href, '/shops/1')]")).click();
		driver.findElement(By.linkText("order1")).click();
		driver.get("http://localhost:" + port + "/shops/1/orders/1/received");
		assertEquals("Something happened...", driver.findElement(By.xpath("//h2")).getText());
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

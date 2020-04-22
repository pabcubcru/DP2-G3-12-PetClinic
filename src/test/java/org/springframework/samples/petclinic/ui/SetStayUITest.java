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
public class SetStayUITest {
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
	public void testSetOrderStatusToReceivedSuccess() throws Exception {
		loginAsAdmin();
		fillEditStayFormSuccess();
		checkStayHasBeenEditedSuccess();
	}

	@Test
	public void testSetOrderStatusToReceivedRedirectToExceptionView() throws Exception {
		loginAsAdmin();
		setStayDateAndCheckDateRedirectToExceptionView();
	}

	public void loginAsAdmin() throws Exception {
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("4dm1n");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	public void fillEditStayFormSuccess() throws Exception {
		driver.findElement(By.xpath("//a[contains(@href, '/owners/find')]")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.linkText("George Franklin")).click();
		driver.findElement(By.linkText("Update")).click();
		driver.findElement(By.id("finishdate")).clear();
		driver.findElement(By.id("finishdate")).sendKeys("2020/10/15");
		driver.findElement(By.id("specialCares")).clear();
		driver.findElement(By.id("specialCares")).sendKeys("specialPrueba");
		driver.findElement(By.id("price")).clear();
		driver.findElement(By.id("price")).sendKeys("100");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	public void checkStayHasBeenEditedSuccess() throws Exception {
		assertEquals("2020-10-15", driver.findElement(By.xpath("//td[3]")).getText());
		assertEquals("specialPrueba", driver.findElement(By.xpath("//td[4]")).getText());
		assertEquals("100.0", driver.findElement(By.xpath("//td[5]")).getText());
	}

	public void setStayDateAndCheckDateRedirectToExceptionView() throws Exception {
		driver.findElement(By.xpath("//a[contains(@href, '/owners/find')]")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.linkText("George Franklin")).click();
		driver.findElement(By.linkText("Update")).click();
		driver.findElement(By.id("startdate")).clear();
		driver.findElement(By.id("startdate")).sendKeys("2020/10/01");
		driver.findElement(By.id("finishdate")).clear();
		driver.findElement(By.id("finishdate")).sendKeys("2020/09/01");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("The finish date must be after than start date",
				driver.findElement(By.xpath("//form[@id='stay']/div/div[2]/div/span[2]")).getText());
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

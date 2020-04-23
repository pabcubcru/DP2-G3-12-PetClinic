package org.springframework.samples.petclinic.ui;

import java.time.LocalDate;
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
public class CreateStayUITest {
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
	public void testCreateNewStaySuccess() throws Exception {
		loginAsAdmin();
		fillCreateStayFormSuccess();
		checkStayHasBeenCreatedSuccess();
	}

	@Test
	public void testCreateNewStayHasError() throws Exception {
		loginAsAdmin();
		fillCreateStayFormFinishDateNullAndCheckErrorMessage();
	}

	public void loginAsAdmin() throws Exception {
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("4dm1n");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	public void fillCreateStayFormSuccess() throws Exception {
		driver.findElement(By.xpath("//a[contains(@href, '/owners/find')]")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.linkText("David Schroeder")).click();
		driver.findElement(By.linkText("Add Stay")).click();
		driver.findElement(By.id("finishdate")).clear();
		driver.findElement(By.id("finishdate")).sendKeys("2020/04/30");
		driver.findElement(By.id("specialCares")).clear();
		driver.findElement(By.id("specialCares")).sendKeys("hairdressing");
		driver.findElement(By.id("price")).clear();
		driver.findElement(By.id("price")).sendKeys("25");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	public void checkStayHasBeenCreatedSuccess() throws Exception {
		assertEquals(LocalDate.now().toString(), driver.findElement(By.xpath("//td[2]/table/tbody/tr/td[2]")).getText());
		assertEquals("2020-04-30", driver.findElement(By.xpath("//td[3]")).getText());
		assertEquals("hairdressing", driver.findElement(By.xpath("//td[4]")).getText());
		assertEquals("25.0", driver.findElement(By.xpath("//td[5]")).getText());
	}

	public void fillCreateStayFormFinishDateNullAndCheckErrorMessage() throws Exception {
		driver.findElement(By.xpath("//a[contains(@href, '/owners/find')]")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.linkText("David Schroeder")).click();
		driver.findElement(By.linkText("Add Stay")).click();
		driver.findElement(By.id("specialCares")).clear();
		driver.findElement(By.id("specialCares")).sendKeys("hairdressing");
		driver.findElement(By.id("price")).clear();
		driver.findElement(By.id("price")).sendKeys("25");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("no puede ser null", driver.findElement(By.xpath("//form[@id='stay']/div/div[2]/div/span[2]")).getText());
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

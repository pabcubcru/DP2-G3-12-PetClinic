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
public class CreateHospitalisationUITest {
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
	public void testCreateNewHospitalisationSuccess() throws Exception {
		loginAsAdmin();
		fillCreateHospitalisationFormSuccess();
		checkHospitalisationHasBeenCreatedSuccess();
	}

	@Test
	public void testCreateNewHospitalisationEmptyField() throws Exception {
		loginAsAdmin();
		fillCreateHospitalisationFormEmptyField();
	}

	public void loginAsAdmin() throws Exception {
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("4dm1n");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	public void fillCreateHospitalisationFormSuccess() throws Exception {
		driver.findElement(By.xpath("//a[contains(@href, '/owners/find')]")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.linkText("Betty Davis")).click();
		driver.findElement(By.linkText("Hospitalise")).click();
		driver.findElement(By.id("treatment")).clear();
		driver.findElement(By.id("treatment")).sendKeys("NONE");
		driver.findElement(By.id("diagnosis")).clear();
		driver.findElement(By.id("diagnosis")).sendKeys("NONE");
		driver.findElement(By.id("totalPrice")).clear();
		driver.findElement(By.id("totalPrice")).sendKeys("100");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	public void checkHospitalisationHasBeenCreatedSuccess() throws Exception {
		assertEquals("SICK", driver.findElement(By.xpath("//dd[4]")).getText());
	}
	
	private void fillCreateHospitalisationFormEmptyField() {
		driver.findElement(By.xpath("//a[contains(@href, '/owners/find')]")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    driver.findElement(By.linkText("Betty Davis")).click();
	    driver.findElement(By.linkText("Hospitalise")).click();
	    driver.findElement(By.id("treatment")).clear();
	    driver.findElement(By.id("treatment")).sendKeys("NONE");
	    driver.findElement(By.id("diagnosis")).clear();
	    driver.findElement(By.id("diagnosis")).sendKeys("NONE");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    assertEquals("no puede ser null", driver.findElement(By.xpath("//form[@id='hospitalisation']/div/div[3]/div/span[2]")).getText());
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

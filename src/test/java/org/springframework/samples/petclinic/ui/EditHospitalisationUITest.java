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
public class EditHospitalisationUITest {
	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();

	@LocalServerPort
	private int port;

	@BeforeEach
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		System.setProperty("webdriver.gecko.driver", System.getenv("webdriver.gecko.driver"));
		driver.get("http://localhost:" + port);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testUpdateHospitalisationSuccess() throws Exception {
		loginAsAdmin();
		fillEditHospitalisationFormSuccess();
		checkHospitalisationHasBeenEditedSuccess();
	}

	@Test
	public void testUpdateHospitalisation() throws Exception {
		loginAsAdmin();
		setHospitalisationError();
	}

	public void loginAsAdmin() throws Exception {
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("4dm1n");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}
	
	  public void fillEditHospitalisationFormSuccess() throws Exception {
	    driver.findElement(By.xpath("//a[contains(@href, '/owners/find')]")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    driver.findElement(By.linkText("Jean Coleman")).click();
	    driver.findElement(By.xpath("(//a[contains(text(),'Update')])[3]")).click();
	    driver.findElement(By.id("treatment")).clear();
	    driver.findElement(By.id("treatment")).sendKeys("test1");
	    driver.findElement(By.id("diagnosis")).clear();
	    driver.findElement(By.id("diagnosis")).sendKeys("test1");
	    driver.findElement(By.id("totalPrice")).clear();
	    driver.findElement(By.id("totalPrice")).sendKeys("50.5");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	  }
	
	public void checkHospitalisationHasBeenEditedSuccess() throws Exception {
		assertEquals("test1", driver.findElement(By.xpath("//tbody[3]/tr[2]/td[4]")).getText());
	    assertEquals("test1", driver.findElement(By.xpath("//tbody[3]/tr[2]/td[5]")).getText());
	    assertEquals("HOSPITALISED", driver.findElement(By.xpath("//tbody[3]/tr[2]/td[6]")).getText());
	    assertEquals("50.5", driver.findElement(By.xpath("//tbody[3]/tr[2]/td[7]")).getText());
	    assertEquals("2020-04-22", driver.findElement(By.xpath("//tbody[3]/tr[2]/td[2]")).getText());
	}

	  public void setHospitalisationError() throws Exception {
	    driver.findElement(By.xpath("//a[contains(@href, '/owners/find')]")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    driver.findElement(By.linkText("Jean Coleman")).click();
	    driver.findElement(By.xpath("(//a[contains(text(),'Update')])[3]")).click();
	    driver.findElement(By.id("treatment")).clear();
	    driver.findElement(By.id("treatment")).sendKeys("test2");
	    driver.findElement(By.id("diagnosis")).clear();
	    driver.findElement(By.id("diagnosis")).sendKeys("test2");
	    driver.findElement(By.id("totalPrice")).clear();
	    driver.findElement(By.id("totalPrice")).sendKeys("-25.0");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    assertEquals("tiene que estar entre 1 y 9223372036854775807", driver.findElement(By.xpath("//form[@id='hospitalisation']/div/div[3]/div/span[2]")).getText());
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


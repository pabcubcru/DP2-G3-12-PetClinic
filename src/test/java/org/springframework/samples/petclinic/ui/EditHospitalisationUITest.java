package org.springframework.samples.petclinic.ui;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EditHospitalisationUITest {
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
	public void testEditHospitalisationSuccess() throws Exception {
		loginAsAdmin();
		fillEditHospitalisationFormSuccess();
		checkHospitalisationHasBeenEditedSuccess();
	}

	@Test
	public void testEditHospitalisation() throws Exception {
		loginAsAdmin();
		editHospitalisationError();
	}

	@Given("Un administrador con id=1 que quiere modificar los datos de hospitalización de una mascota con id=1")
	public void loginAsAdmin() throws Exception {
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("4dm1n");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	@When("actualiza la información sin dejar ningún campo en blanco")
	@And("Pulsa el botón de confirmación")
	@Then("Se le redirige a la vista de detalles del dueño de la mascota con id=1.")
	public void fillEditHospitalisationFormSuccess() throws Exception {
		driver.findElement(By.xpath("//a[contains(@href, '/owners/find')]")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.linkText("Jean Coleman")).click();
		driver.findElement(By.xpath("//a[contains(@href, '/owners/6/pets/7/hospitalisations/3/edit')]")).click();
		driver.findElement(By.id("treatment")).clear();
		driver.findElement(By.id("treatment")).sendKeys("testTreatment");
		driver.findElement(By.id("diagnosis")).clear();
		driver.findElement(By.id("diagnosis")).sendKeys("testDiagnosis");
		driver.findElement(By.id("totalPrice")).clear();
		driver.findElement(By.id("totalPrice")).sendKeys("50.5");
		new Select(driver.findElement(By.id("hospitalisationStatus.name"))).selectByVisibleText("DISCHARGED");
		driver.findElement(By.xpath("//option[@value='DISCHARGED']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	public void checkHospitalisationHasBeenEditedSuccess() throws Exception {
		assertEquals("HEALTHY", driver.findElement(By.xpath("//tr[2]/td/dl/dd[4]")).getText());
	}

	@When("Actualiza el informe pero introduce el campo precio en negativo")
	@And("Pulsa el botón de confirmación")
	@Then("Le salta un error que indica que el precio no puede ser negativo.")
	public void editHospitalisationError() throws Exception {
		driver.findElement(By.xpath("//a[contains(@href, '/owners/find')]")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.linkText("George Franklin")).click();
		driver.findElement(By.xpath("//a[contains(@href, '/owners/1/pets/1/hospitalisations/4/edit')]")).click();
		driver.findElement(By.id("treatment")).clear();
		driver.findElement(By.id("treatment")).sendKeys("testTreatment");
		driver.findElement(By.id("diagnosis")).clear();
		driver.findElement(By.id("diagnosis")).sendKeys("testDiagnosis");
		driver.findElement(By.id("totalPrice")).clear();
		driver.findElement(By.id("totalPrice")).sendKeys("-25.0");
		new Select(driver.findElement(By.id("hospitalisationStatus.name"))).selectByVisibleText("DISCHARGED");
		driver.findElement(By.xpath("//option[@value='DISCHARGED']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("tiene que estar entre 0 y 9223372036854775807",
				driver.findElement(By.xpath("//form[@id='hospitalisation']/div/div[3]/div/span[2]")).getText());
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

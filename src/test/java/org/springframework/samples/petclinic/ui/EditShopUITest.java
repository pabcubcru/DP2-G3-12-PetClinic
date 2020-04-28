
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

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EditShopUITest {

	private WebDriver		driver;
	private StringBuffer	verificationErrors	= new StringBuffer();

	@LocalServerPort
	private int				port;


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
		fillEditShopFormSuccess();
		checkShopHasBeenEditedSuccess();
	}

	@Test
	public void testUpdateHospitalisation() throws Exception {
		loginAsAdmin();
		setShopError();
	}

	@Given("Un administrador que quiere modificar una tienda con id=1")
	public void loginAsAdmin() throws Exception {
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("4dm1n");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	@When("Pulsa el botón de modificar tienda en la vista de detalle y rellena el campo nombre correctamente")
	@And("Pulsa el botón de confirmación")
	@Then("Se le muestra la vista detalles de la tienda con id=1.")
	public void fillEditShopFormSuccess() throws Exception {
		driver.findElement(By.xpath("//a[contains(@href, '/shops/1')]")).click();
		driver.findElement(By.linkText("Edit shop")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("testShop");
		driver.findElement(By.xpath("//button[@type='submit']")).click();

	}

	public void checkShopHasBeenEditedSuccess() throws Exception {
		assertEquals("testShop Information", driver.findElement(By.xpath("//h2")).getText());
	}

	@When("Pulsa el botón de modificar tienda en la vista de detalle y deja en blanco el campo nombre")
	@And("Pulsa el botón de confirmación")
	@Then("Se le muestra la vista de modificación de la tienda mostrando el error de que no puede dejar en blanco el campo nombre.")
	public void setShopError() throws Exception {
		driver.findElement(By.xpath("//a[contains(@href, '/shops/1')]")).click();
		driver.findElement(By.linkText("Edit shop")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("el tamaño tiene que estar entre 3 y 50", driver.findElement(By.xpath("//form[@id='update-shop-form']/div/div/div/span[2]")).getText());
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

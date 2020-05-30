package org.springframework.samples.petclinic.ui;

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
public class CreateOrderUITest {
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
	public void testCreateNewOrderProductNumberIs0() throws Exception {
		loginAsAdmin();
		fillCreateOrderFormProductNumberIs0AndCheckErrorMessage();
	}

	@Test
	public void testCreateNewOrderSuccess() throws Exception {
		loginAsAdmin();
		fillCreateOrderFormSuccess();
		checkOrderHasBeenCreatedSuccess();
	}

	@Given("Un administrador con id=1 que quiere hacer un pedido")
	public void loginAsAdmin() throws Exception {
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("4dm1n");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	@When("Rellena los campos de proveedor y número de productos, selecciona el producto")
	@And("Pulsa el botón de confirmación")
	@Then("Se le redirige a la vista de detalles de la tienda con el pedido añadido.")
	public void fillCreateOrderFormSuccess() throws Exception {
		driver.findElement(By.xpath("//a[contains(@href, '/shops/1')]")).click();
		driver.findElement(By.linkText("Add Order")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("orderTestUI");
		driver.findElement(By.id("supplier")).clear();
		driver.findElement(By.id("supplier")).sendKeys("supplierTestUI");
		driver.findElement(By.id("productNumber")).clear();
		driver.findElement(By.id("productNumber")).sendKeys("10");
		new Select(driver.findElement(By.id("product.name"))).selectByVisibleText("product1");
		driver.findElement(By.xpath("//option[@value='product1']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.linkText("orderTestUI")).click();
	}

	public void checkOrderHasBeenCreatedSuccess() throws Exception {
		assertEquals("orderTestUI", driver.findElement(By.xpath("//td")).getText());
		assertEquals("supplierTestUI", driver.findElement(By.xpath("//tr[2]/td")).getText());
		assertEquals("10", driver.findElement(By.xpath("//tr[4]/td")).getText());
		assertEquals("INPROCESS", driver.findElement(By.xpath("//tr[6]/td")).getText());
		assertEquals("product1", driver.findElement(By.linkText("product1")).getText());
	}
	
	@When("Rellena los campos de proveedor, pero el número de productos es 0, selecciona el producto")
	@And("Pulsa el botón de confirmación")
	@Then("Salta un mensaje de error indicando que el número de productos tiene que ser como mínimo 1.")
	public void fillCreateOrderFormProductNumberIs0AndCheckErrorMessage() throws Exception {
		driver.findElement(By.xpath("//a[contains(@href, '/shops/1')]")).click();
		driver.findElement(By.linkText("Add Order")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys("orderTestUI");
		driver.findElement(By.id("supplier")).clear();
		driver.findElement(By.id("supplier")).sendKeys("supplierTestUI");
		new Select(driver.findElement(By.id("product.name"))).selectByVisibleText("product1");
		driver.findElement(By.xpath("//option[@value='product1']")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("tiene que estar entre 1 y 9223372036854775807",
				driver.findElement(By.xpath("//form[@id='add-order-form']/div/div[3]/div/span[2]")).getText());
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

package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalDate;
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

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateDiscountForProductUITest {

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
	public void testCreateNewDiscountForProductSuccess() throws Exception {
		loginAsAdmin();
		fillCreateDiscountForProductFormSuccessAndCheckPriceBeforeCreateDiscount();
	}

	@Test
	public void testCreateNewDiscountForProductNullFinishDate() throws Exception {
		loginAsAdmin();
		fillCreateDiscountForProductFormNullFinishDateAndCheckErrorMessageAndPriceNoChange();
	}

	@Given("Un administrador que quiere aplicar un descuento a un producto con id=1")
	public void loginAsAdmin() throws Exception {
		driver.findElement(By.xpath("//a[contains(text(),'Login')]")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("admin1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("4dm1n");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	@When("Entra en la vista del producto, pulsa el botón de crear descuento, rellena correctamente los campos de porcentaje, nombre, fecha inicio y fecha fin")
	@And("Pulsa el botón de confirmación")
	@Then("Se le muestra la vista del producto con id=1 con el descuento creado y el precio con el descuento aplicado si el descuento está activo.")
	public void fillCreateDiscountForProductFormSuccessAndCheckPriceBeforeCreateDiscount() throws Exception {
		driver.findElement(By.xpath("//a[contains(@href, '/shops/1')]")).click();
		driver.findElement(By.linkText("product2")).click();
		String priceBefore = driver.findElement(By.xpath("//tr[2]/td")).getText();
		driver.findElement(By.linkText("Create Discount")).click();
		driver.findElement(By.id("percentage")).clear();
		driver.findElement(By.id("percentage")).sendKeys("30");
		driver.findElement(By.id("finishDate")).clear();
		driver.findElement(By.id("finishDate")).sendKeys("2020/09/01");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		checkDiscountForProductHasBeenCreatedSuccessAndDiscountHasBeenApplied(priceBefore);
	}

	public void checkDiscountForProductHasBeenCreatedSuccessAndDiscountHasBeenApplied(String priceBefore) throws Exception {
		assertEquals("30.0", driver.findElement(By.xpath("//table[2]/tbody/tr/td")).getText());
		assertEquals(LocalDate.now().toString().replace("-", "/"), driver.findElement(By.xpath("//table[2]/tbody/tr[2]/td")).getText());
		assertEquals("2020/09/01", driver.findElement(By.xpath("//table[2]/tbody/tr[3]/td")).getText());
		assertTrue(Double.parseDouble(priceBefore) > Double.parseDouble(driver.findElement(By.xpath("//tr[2]/td")).getText()));
		assertEquals("Price With Discount (EUR)", driver.findElement(By.xpath("//tr[2]/th")).getText());
	}

	@When("Entra en la vista del producto, pulsa el botón de crear descuento, rellena correctamente los campos de porcentaje, nombre y fecha inicio pero deja en blanco fecha fin")
	@And("Pulsa el botón de confirmación")
	@Then("Se le muestra la vista de creación del descuento con el error de que no puede ser nulo el campo de fecha fin.")
	public void fillCreateDiscountForProductFormNullFinishDateAndCheckErrorMessageAndPriceNoChange() throws Exception {
		driver.findElement(By.xpath("//a[contains(@href, '/shops/1')]")).click();
		driver.findElement(By.linkText("product4")).click();
		String priceBefore = driver.findElement(By.xpath("//tr[2]/td")).getText();
		driver.findElement(By.linkText("Create Discount")).click();
		driver.findElement(By.id("percentage")).clear();
		driver.findElement(By.id("percentage")).sendKeys("30");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("no puede ser null",
				driver.findElement(By.xpath("//form[@id='add-discount-form']/div/div[3]/div/span[2]")).getText());
		driver.findElement(By.xpath("//a[contains(@href, '/shops/1')]")).click();
		driver.findElement(By.linkText("product4")).click();
		assertEquals(priceBefore, driver.findElement(By.xpath("//tr[2]/td")).getText());
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
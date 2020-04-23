package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Shop;
import org.springframework.samples.petclinic.service.ShopService;
import org.springframework.samples.petclinic.web.ProductController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductControllerIntegrationTest {

	@Autowired
	private ProductController productController;
	
	@Autowired
	private ShopService shopService;

	@Test
	void testInitNewProductForm() throws Exception {
		ModelMap model = new ModelMap();
		String view = productController.initNewProductForm(model);
		
		assertEquals(view, "products/createOrUpdateProductForm");
		assertNotNull(model.get("product"));
	}
	
	@Test
	void testProcessNewProductFormSuccess() throws Exception {
		Product product = new Product();
		Shop shop = shopService.findShopById(1);
		product.setName("productTestNewSuccess");
		product.setPrice(15.0);
		product.setStock(10);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		String view = productController.processNewProductForm(product, result, shop);
		
		assertEquals(view, "redirect:/shops/" + shop.getId());
	}
	
	@Test
	void testProcessNewProductFormHasErrors() throws Exception {
		Product product = new Product();
		Shop shop = shopService.findShopById(1);
		product.setName("productTest");
		product.setPrice(null);
		product.setStock(10);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		result.rejectValue("price", "nullPrice");
		String view = productController.processNewProductForm(product, result, shop);
		
		assertEquals(view, "products/createOrUpdateProductForm");
		assertEquals(result.getFieldErrorCount("price"), 1); // Price is null
	}
	
	@Test
	void testProcessNewProductFormHasErrorsDuplicatedName() throws Exception {
		Product product = new Product();
		Shop shop = shopService.findShopById(1);
		product.setName("product1");
		product.setPrice(10.5);
		product.setStock(10);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		String view = productController.processNewProductForm(product, result, shop);
		
		assertEquals(view, "products/createOrUpdateProductForm");
		assertEquals(result.getFieldError("name").getCode(), "duplicatedName"); // Name is duplicated
	}
	
	@Test
	void testInitUpdateProductForm() throws Exception {
		ModelMap model = new ModelMap();
		String view = productController.initUpdateProductForm(1, model);
		
		assertEquals(view, "products/createOrUpdateProductForm");
		assertNotNull(model.get("product"));
	}
	
	@Test
	void testProcessUpdateProductFormSuccess() throws Exception {
		Product product = new Product();
		Shop shop = shopService.findShopById(1);
		product.setName("productTestUpdate");
		product.setPrice(15.0);
		product.setStock(10);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		int productId = 1;
		String view = productController.processUpdateProductForm(product, result, productId, shop);
		
		assertEquals(view, "redirect:/shops/" + shop.getId() + "/products/" + productId);
	}
	
	@Test
	void testProcessUpdateProductFormHasErrors() throws Exception {
		Product product = new Product();
		Shop shop = shopService.findShopById(1);
		product.setName("productTestUpdate");
		product.setPrice(15.0);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		result.rejectValue("stock", "nullStock");
		int productId = 1;
		String view = productController.processUpdateProductForm(product, result, productId, shop);
		
		assertEquals(view, "products/createOrUpdateProductForm");
		assertEquals(result.getFieldErrorCount("stock"), 1); // Stock is null
	}
	
	@Test
	void testProcessUpdateProductFormHasErrorsDuplicatedName() throws Exception {
		Product product = new Product();
		Shop shop = shopService.findShopById(1);
		product.setName("product2");
		product.setPrice(10.5);
		product.setStock(10);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		int productId = 1;
		String view = productController.processUpdateProductForm(product, result, productId, shop);
		
		assertEquals(view, "products/createOrUpdateProductForm");
		assertEquals(result.getFieldError("name").getCode(), "duplicatedName"); // Name is duplicated
	}
	
	@Test
	void testProcessDeleteProductFormHasOrdersInProcess() throws Exception {
		ModelMap model = new ModelMap();
		Shop shop = shopService.findShopById(1);
		int productId = 4;
		String view = productController.deleteProduct(model, productId, shop);
		
		assertEquals(view, "/exception");
	}
	
	@Test
	void testProcessDeleteProductFormSuccess() throws Exception {
		ModelMap model = new ModelMap();
		Shop shop = shopService.findShopById(1);
		int productId = 6;
		String view = productController.deleteProduct(model, productId, shop);
		
		assertEquals(view, "redirect:/shops/" + shop.getId());
	}
	
	@Test
	void testShowProduct() throws Exception {
		String view = productController.showProduct(1).getViewName();
		
		assertEquals(view, "products/productDetails");
	}
}

package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertThat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Discount;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Shop;
import org.springframework.samples.petclinic.service.DiscountService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ShopService;
import org.springframework.samples.petclinic.web.DiscountController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DiscountControllerIntegrationTest {
	
	@Autowired
	private DiscountController discountController;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ShopService shopService;
	
	@Autowired
	private DiscountService discountService;

	@Test
	void testInitNewDiscountForm() throws Exception {
		ModelMap model = new ModelMap();
		String view = discountController.initNewDiscountForm(model);
		
		assertEquals(view, "discounts/createOrUpdateDiscountForm");
	}

	
	@Test
	void testProcessNewDiscountFormSuccess() throws Exception {
		Discount discount = new Discount();
		discount.setFinishDate(LocalDate.of(2020, 10, 23));
		discount.setStartDate(LocalDate.of(2020, 7, 11));
		discount.setPercentage(20.0);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		Shop shop = shopService.findShopById(1);
		Product product = productService.findProductById(2);
		String view = discountController.processNewDiscountForm(discount, result, product, shop.getId());
		
		assertEquals(view, "redirect:/shops/1/products/{productId}");
		
	}
	

	@Test
	void testProcessNewDiscountFormHasErrors() throws Exception {
		Discount discount = new Discount();
		discount.setFinishDate(LocalDate.of(2020, 10, 23));
		discount.setStartDate(LocalDate.of(2020, 7, 11));
		discount.setPercentage(null);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		result.rejectValue("percentage", "nullPercentage");
		Product product = productService.findProductById(2);
		int shopId = 1;
		String view = discountController.processNewDiscountForm(discount, result, product, shopId);
		
		assertEquals(view, "discounts/createOrUpdateDiscountForm");
		assertEquals(result.getFieldErrorCount("percentage"), 1); 
	}
	

	@Test
	void testInitEditDiscountForm() throws Exception {
		ModelMap model = new ModelMap();
		Discount discount = discountService.findDiscountById(1);
		Product product1 = new Product();
		model.put("discount", discount);
		String view = discountController.initEditDiscountForm(model, product1, discount.getId());
		
		assertEquals(view, "discounts/createOrUpdateDiscountForm");
	}

	
	@Test
	void testProcessEditDiscountFormSuccess() throws Exception {
		Discount discount = new Discount();
		discount.setStartDate(LocalDate.of(2020, 6, 10));
		discount.setFinishDate(LocalDate.of(2020, 7, 10));
		discount.setPercentage(30.0);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		Shop shop = shopService.findShopById(1);
		Product product = productService.findProductById(2);
		String view = discountController.processNewDiscountForm(discount, result, product, shop.getId());
		
		
		assertEquals(view, "redirect:/shops/1/products/{productId}");
	}
	
	@Test
	void testProcessEditDiscountFormHasErrors() throws Exception {
		Discount discount = new Discount();
		discount.setFinishDate(LocalDate.of(2020, 10, 23));
		discount.setStartDate(LocalDate.of(2020, 7, 11));
		discount.setPercentage(null);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		result.rejectValue("percentage", "nullPercentage");
		Product product = productService.findProductById(2);
		int shopId = 1;
		String view = discountController.processNewDiscountForm(discount, result, product, shopId);
		
		assertEquals(view, "discounts/createOrUpdateDiscountForm");
		assertEquals(result.getFieldErrorCount("percentage"), 1);
	}
	
	@Test
	void testProcessDeleteDiscountSuccess() throws Exception {
		Product product = productService.findProductById(1);
		String view = discountController.processDeleteDiscount(1, product, 1);
		
		assertEquals(view, "redirect:/shops/1/products/{productId}");
		assertEquals(productService.findProductById(1).getDiscount(), null);
	}
	
	@Test
	void testProcessDeleteDiscountHasError() throws Exception {
		Product product = productService.findProductById(2);
		String view = discountController.processDeleteDiscount(2, product, 1);
		
		assertEquals(view, "/exception");
		assertEquals(productService.findProductById(3).getDiscount(), discountService.findDiscountById(2));
	}
}

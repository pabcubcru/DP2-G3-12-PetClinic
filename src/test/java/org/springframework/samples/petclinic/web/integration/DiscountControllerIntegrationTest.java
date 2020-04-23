package org.springframework.samples.petclinic.web.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Discount;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.web.DiscountController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class DiscountControllerIntegrationTest {
	
	@Autowired
	private DiscountController discountController;
	
	@Autowired
	private ProductService productService;

	@Test
	void testInitNewDiscountForm() throws Exception {
		ModelMap model = new ModelMap();
		String view = discountController.initNewDiscountForm(model);
		
		assertEquals(view, "discounts/createOrUpdateDiscountForm");
	}
	
	@Test
	void testProcessNewDiscountFormSuccess() throws Exception {
		Discount discount = new Discount();
		discount.setFinishDate(LocalDate.of(2020, 4, 23));
		discount.setStartDate(LocalDate.of(2020, 7, 11));
		discount.setPercentage(20.0);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		Product product = productService.findProductById(2);
		int shopId = 1;
		String view = discountController.processNewDiscountForm(discount, result, product, shopId);
		
		assertEquals(view, "redirect:/shops/" + shopId + "/products/{productId}");
		
	}
}

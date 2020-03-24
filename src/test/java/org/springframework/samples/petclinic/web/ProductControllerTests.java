
package org.springframework.samples.petclinic.web;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.model.Discount;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Shop;
import org.springframework.samples.petclinic.service.DiscountService;
import org.springframework.samples.petclinic.service.OrderService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ShopService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = ProductController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)

public class ProductControllerTests {

	private static final int	TEST_PRODUCT_ID	= 1;
	private static final int	TEST_SHOP_ID	= 1;
	
	@MockBean
	private ShopService shopService;
	
	@MockBean
	private OrderService orderService;
	
	@MockBean
	private DiscountService discountService;
	
	@Autowired
	private MockMvc				mockMvc;

	@MockBean
	private ProductService		clinicService;

	
	@BeforeEach
	void setup() {
		//PRODUCT1
		Product product1 = new Product();
		product1.setName("product1");
		product1.setPrice(18.0);
		product1.setId(TEST_PRODUCT_ID);
		product1.setStock(6);

		Discount discount1 = new Discount();
		discount1.setId(1);
		discount1.setName("discount1");
		discount1.setStartDate(LocalDate.of(2020, 06, 16));
		discount1.setFinishDate(LocalDate.of(2020, 06, 25));
		discount1.setPercentage(25.0);

		product1.setDiscount(discount1);

		Shop shop1 = new Shop();
		shop1.setId(1);
		shop1.setName("shop1");

		product1.setShop(shop1);

		
		given(this.clinicService.findProductsNames()).willReturn(Lists.newArrayList("product1", "product2"));
		given(this.clinicService.findProductById(TEST_PRODUCT_ID)).willReturn(product1);

	}


	@WithMockUser(value = "spring")
	@Test
	void testInitNewProductForm() throws Exception {
		mockMvc.perform(get("/shops/{shopId}/products/new", TEST_SHOP_ID))
		.andExpect(status().isOk()).andExpect(view().name("products/createOrUpdateProductForm"))
		.andExpect(model().attributeExists("product"));
	}
	
	@WithMockUser(value = "spring")
    @Test
    void testProcessNewProductFormSuccess() throws Exception {
		mockMvc.perform(post("/shops/{shopId}/products/new", TEST_SHOP_ID)
						.with(csrf())
						.param("name", "product1")
						.param("price", "18.0")
						.param("stock", "6"))
			.andExpect(status().is2xxSuccessful());
}

	@WithMockUser(value = "spring")
    @Test
    void testProcessNewProductFormHasErrors() throws Exception {
	mockMvc.perform(post("/shops/{shopId}/products/new", TEST_SHOP_ID)
						.with(csrf())
						.param("name", "product1")
						.param("stock", "6"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasErrors("product"))
			.andExpect(model().attributeHasFieldErrors("product", "price"))
			.andExpect(view().name("products/createOrUpdateProductForm"));
}

	//post bien y mal
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateProductForm() throws Exception {
		mockMvc.perform(get("/shops/{shopId}/products/{productId}/edit", TEST_SHOP_ID,TEST_PRODUCT_ID))
		.andExpect(status().isOk()).andExpect(model().attributeExists("product"))
		.andExpect(model().attribute("product", hasProperty("name", is("product1"))))
		.andExpect(model().attribute("product", hasProperty("price", is(18.0))))
		.andExpect(model().attribute("product", hasProperty("id", is(TEST_PRODUCT_ID))))
		.andExpect(model().attribute("product", hasProperty("stock", is(6))))
		.andExpect(view().name("products/createOrUpdateProductForm"));
	}
	
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateProductFormSuccess() throws Exception {
		mockMvc.perform(post("/shops/{shopId}/products/{productId}/edit", TEST_SHOP_ID,TEST_PRODUCT_ID)
			.with(csrf())
			.param("name", "product1")
			.param("price", "18.0")
			.param("stock", "6"))
		.andExpect(status().is2xxSuccessful());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateProductFormHasErrors() throws Exception {
		mockMvc.perform(post("/shops/{shopId}/products/{productId}/edit", TEST_SHOP_ID,TEST_PRODUCT_ID)
			.with(csrf())
			.param("name", "product1")
			.param("stock", "6"))
		.andExpect(status().isOk())
		.andExpect(model().attributeHasErrors("product"))
		.andExpect(model().attributeHasFieldErrors("product", "price"))
		.andExpect(view().name("products/createOrUpdateProductForm"));
	}
	
//	@WithMockUser(value = "spring")
//	@Test
//	void testProcessDeleteProductFormSuccess() throws Exception {
//		mockMvc.perform(get("/shops/{shopId}/products/{productId}/delete", TEST_SHOP_ID, TEST_PRODUCT_ID))
//		.andExpect(status().isOk()).andExpect(view().name("redirect:/shops/shopId"));
//		//.andExpect(model().attributeDoesNotExist("product"));
//	}
	

}

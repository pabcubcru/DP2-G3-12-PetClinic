
package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Shop;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.ShopService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ShopController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class ShopControllerTests {

	@MockBean
	private ShopService clinicService;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AuthoritiesService authoritiesService;

	private static final int TEST_SHOP_ID = 1;
	private static final int TEST_PRODUCT_ID = 1;
	private Shop shop1;
	private Product product1;

	@BeforeEach
	void setup() {
		shop1 = new Shop();
		shop1.setId(TEST_SHOP_ID);
		shop1.setName("shop1");

		product1 = new Product();
		product1.setName("product1");
		product1.setId(TEST_PRODUCT_ID);
		product1.setPrice(18.0);
		product1.setStock(6);

		Order order1 = new Order();
		order1.setId(1);
		order1.setProduct(product1);
		order1.setName("order1");
		order1.setSupplier("supplier");
		order1.setProductNumber(10);

		shop1.addOrder(order1);
		shop1.addProduct(product1);

		List<Shop> shops = new ArrayList<Shop>();
		shops.add(shop1);

		given(this.clinicService.findShops()).willReturn(shops);
		given(this.clinicService.findShopById(TEST_SHOP_ID)).willReturn(shop1);
	}

	// update shop

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateShopForm() throws Exception {
		mockMvc.perform(get("/shops/{shopId}/edit", TEST_SHOP_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("shop"))
				.andExpect(model().attribute("shop", hasProperty("name", is("shop1"))))
				.andExpect(view().name("shops/createOrUpdateShopForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateShopFormSuccess() throws Exception {
		mockMvc.perform(post("/shops/{shopId}/edit", TEST_SHOP_ID).with(csrf()).param("name", "shop2"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/shops/1"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateShopFormHasErrors() throws Exception {
		mockMvc.perform(post("/shops/{shopId}/edit", TEST_SHOP_ID).with(csrf()).param("name", ""))
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors("shop"))
				.andExpect(model().attributeHasFieldErrors("shop", "name"))
				.andExpect(view().name("shops/createOrUpdateShopForm"));
	}

	// shop details

	@WithMockUser(value = "spring")
	@Test
	void testShowShopDetails() throws Exception {
		mockMvc.perform(get("/shops/1")).andExpect(status().isOk())
				.andExpect(model().attribute("shop", hasProperty("name", is("shop1"))))
				.andExpect(view().name("shops/shopDetails"));
	}
}

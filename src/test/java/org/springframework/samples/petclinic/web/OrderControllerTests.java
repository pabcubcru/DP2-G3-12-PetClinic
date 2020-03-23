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
import org.springframework.samples.petclinic.service.OrderService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ShopService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = OrderController.class,
excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), 
excludeAutoConfiguration = SecurityConfiguration.class)
class OrderControllerTests {

	private static final int TEST_ORDER_ID = 1;
	private static final int TEST_PRODUCT_ID = 2;
	private static final int TEST_SHOP_ID = 1;

	@Autowired
	private OrderController orderController;

	@MockBean
	private OrderService clinicService;

	@MockBean
	private ProductService productService;
	
	@MockBean
	private ShopService shopService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setup() {

		Order testOrder = new Order();
		testOrder.setId(TEST_ORDER_ID);
		testOrder.setName("testOrder");
		Product testProduct = productService.findProductById(TEST_PRODUCT_ID);
		testOrder.setProduct(testProduct);
		testOrder.setProductNumber(50);
		testOrder.setShop(testProduct.getShop());
		testOrder.setSupplier("supplier");
		
		given(this.clinicService.findOrderById(TEST_ORDER_ID)).willReturn(testOrder);
		given(this.productService.findProductById(TEST_PRODUCT_ID)).willReturn(testProduct);

	}

	@WithMockUser(value = "spring")
	@Test
	void testInitNewOrderForm() throws Exception {
		mockMvc.perform(get("/shops/{shopId}/orders/new")).andExpect(status().isOk())
				.andExpect(view().name("orders/createOrUpdateOrderForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewOrderFormSuccess() throws Exception {
		mockMvc.perform(post("/shops/1/orders/new").with(csrf())
					.param("name", "New order")
					.param("orderDate", "2020/07/02")
					.param("orderStatus", "INPROCESS")
					.param("productNumber", "100")
					.param("supplier", "Groc Groc"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/shops/" + TEST_SHOP_ID + "/orders/" + TEST_ORDER_ID));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewOrderFormHasErrors() throws Exception {
		mockMvc.perform(post("/shops/1/orders/new").with(csrf())
				.param("name", "New order")
				.param("orderDate", "2020/07/02")
				.param("orderStatus", "INPROCESS")
				.param("productNumber", "100"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("order"))
				.andExpect(model().attributeHasFieldErrors("order", "supplier"))
				.andExpect(view().name("orders/createOrUpdateOrderForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessOrderReceivedSuccess() throws Exception {
		mockMvc.perform(get("/shops/1/orders/{orderId}/received", TEST_ORDER_ID)).andExpect(status().isOk())
				.andExpect(view().name("redirect:/shops/{shopId}/orders/{orderId}"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessOrderCanceledSuccess() throws Exception {
		mockMvc.perform(get("/shops/1/orders/{orderId}/canceled", TEST_ORDER_ID)).andExpect(status().isOk())
				.andExpect(view().name("redirect:/shops/{shopId}/orders/{orderId}"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessOrderReceivedError() throws Exception {
		mockMvc.perform(get("/shops/1/orders/{orderId}/received", TEST_ORDER_ID)).andExpect(status().isOk())
				.andExpect(view().name("/exception"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessOrderCanceledError() throws Exception {
		mockMvc.perform(get("/shops/1/orders/{orderId}/canceled", TEST_ORDER_ID)).andExpect(status().isOk())
				.andExpect(view().name("/exception"));
	}	

	@WithMockUser(value = "spring")
	@Test
	void testShowOwner() throws Exception {
		mockMvc.perform(get("/shops/1/orders/{orderId}", TEST_ORDER_ID)).andExpect(status().isOk())
				.andExpect(model().attribute("order", hasProperty("supplier", is("supplier"))))
				.andExpect(model().attribute("order", hasProperty("productNumber", is("50"))))
				.andExpect(model().attribute("order", hasProperty("name", is("Order test"))))
				.andExpect(view().name("orders/orderDetails"));
	}

}

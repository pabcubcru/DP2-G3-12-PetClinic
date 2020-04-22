package org.springframework.samples.petclinic.e2e;

import static org.hamcrest.Matchers.hasProperty;

import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.OrderStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class OrderControllerE2ETest {

	private static final int TEST_ORDER_ID_1 = 1;
	private static final int TEST_ORDER_ID_2 = 2;
	private static final int TEST_ORDER_ID_3 = 3;
	private static final int TEST_ORDER_ID_4 = 4;
	private static final int TEST_ORDER_ID_5 = 5;

	@Autowired
	private MockMvc mockMvc;
	
	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testInitNewOrderForm() throws Exception {
		mockMvc.perform(get("/shops/1/orders/new")).andExpect(status().isOk())
				.andExpect(view().name("orders/createOrUpdateOrderForm"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessNewOrderFormSuccess() throws Exception {
		mockMvc.perform(post("/shops/1/orders/new").with(csrf())
					.param("name", "New order")
					.param("productNumber", "100")
					.param("supplier", "Groc Groc")
					.param("product.name", "product1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/shops/1"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessNewOrderFormHasErrors() throws Exception {
		mockMvc.perform(post("/shops/1/orders/new").with(csrf())
				.param("name", "New order")
				.param("productNumber", "100")
				.param("product.name", "product1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("order"))
				.andExpect(model().attributeHasFieldErrors("order", "supplier"))
				.andExpect(view().name("orders/createOrUpdateOrderForm"));
	}
	
	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessOrderReceivedSuccess() throws Exception {
		mockMvc.perform(get("/shops/1/orders/{orderId}/received", TEST_ORDER_ID_2)).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/shops/1/orders/" + TEST_ORDER_ID_2));
	}
	
	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessOrderCanceledSuccess() throws Exception {
		mockMvc.perform(get("/shops/1/orders/{orderId}/canceled", TEST_ORDER_ID_1)).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/shops/1/orders/" + TEST_ORDER_ID_1));
	}
	
	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessOrderReceivedError() throws Exception {
		mockMvc.perform(get("/shops/1/orders/{orderId}/received", TEST_ORDER_ID_3)).andExpect(status().isOk())
				.andExpect(view().name("/exception"));
	}
	
	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessOrderCanceledError() throws Exception {
		mockMvc.perform(get("/shops/1/orders/{orderId}/canceled", TEST_ORDER_ID_4)).andExpect(status().isOk())
				.andExpect(view().name("/exception"));
	}	

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testShowOrder() throws Exception {
		mockMvc.perform(get("/shops/1/orders/{orderId}", TEST_ORDER_ID_2)).andExpect(status().isOk())
				.andExpect(model().attribute("order", hasProperty("supplier", is("supplier2"))))
				.andExpect(model().attribute("order", hasProperty("productNumber", is(25))))
				.andExpect(model().attribute("order", hasProperty("name", is("order2"))))
				.andExpect(model().attribute("order", hasProperty("orderStatus", is(OrderStatus.INPROCESS))))
				.andExpect(view().name("orders/orderDetails"));
	}
	
	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessDeleteOrdersFormInProcessThrowException() throws Exception {
		mockMvc.perform(get("/shops/1/orders/{orderId}/delete", TEST_ORDER_ID_2)).andExpect(status().isOk())
				.andExpect(view().name("/exception"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessDeleteOrderFormSuccess() throws Exception {
		mockMvc.perform(get("/shops/1/orders/{orderId}/delete", TEST_ORDER_ID_5))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/shops/1"));
	}
}

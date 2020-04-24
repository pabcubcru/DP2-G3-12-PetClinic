package org.springframework.samples.petclinic.web.e2e;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class ShopControllerE2ETest {

	@Autowired
	private MockMvc mockMvc;

	private static final int TEST_SHOP_ID_1 = 1;

	// update shop

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testInitUpdateShopForm() throws Exception {
		mockMvc.perform(get("/shops/{shopId}/edit", TEST_SHOP_ID_1)).andExpect(status().isOk())
				.andExpect(model().attributeExists("shop"))
				.andExpect(view().name("shops/createOrUpdateShopForm"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessUpdateShopFormSuccess() throws Exception {
		mockMvc.perform(post("/shops/{shopId}/edit", TEST_SHOP_ID_1).with(csrf()).param("name", "shop3"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/shops/1"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessUpdateShopFormHasErrors() throws Exception {
		mockMvc.perform(post("/shops/{shopId}/edit", TEST_SHOP_ID_1).with(csrf()).param("name", ""))
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors("shop"))
				.andExpect(model().attributeHasFieldErrors("shop", "name"))
				.andExpect(view().name("shops/createOrUpdateShopForm"));
	}

	// shop details

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testShowShopDetails() throws Exception {
		mockMvc.perform(get("/shops/1")).andExpect(status().isOk())
				.andExpect(view().name("shops/shopDetails"));
	}
}

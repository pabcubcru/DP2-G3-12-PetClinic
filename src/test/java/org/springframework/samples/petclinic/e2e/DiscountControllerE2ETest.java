package org.springframework.samples.petclinic.e2e;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class DiscountControllerE2ETest {

	private static final int TEST_PRODUCT_ID_1 = 1;
	private static final int TEST_PRODUCT_ID_2 = 2;
	private static final int TEST_PRODUCT_ID_3 = 3;
	private static final int TEST_PRODUCT_ID_4 = 4;
	private static final int TEST_PRODUCT_ID_5 = 5;
	private static final int TEST_DISCOUNT_ID_1 = 1;
	private static final int TEST_DISCOUNT_ID_2 = 2;
	private static final int TEST_DISCOUNT_ID_3 = 3;


	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testInitNewDiscountForm() throws Exception {
		mockMvc.perform(get("/shops/1/products/{productId}/discounts/new", TEST_PRODUCT_ID_2)).andExpect(status().isOk())
				.andExpect(view().name("discounts/createOrUpdateDiscountForm"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessNewDiscountFormSuccess() throws Exception {
		mockMvc.perform(post("/shops/1/products/{productId}/discounts/new", TEST_PRODUCT_ID_2).with(csrf())
				.param("startDate", "2020/05/02")
				.param("finishDate", "2020/06/03")
				.param("percentage", "20.0")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/shops/1/products/{productId}"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessNewDiscountFormHasErrors() throws Exception {
		mockMvc.perform(post("/shops/1/products/{productId}/discounts/new", TEST_PRODUCT_ID_4).with(csrf())
				.param("startDate", "2020/05/06").param("finishDate", "2020/05/01"))
				.andExpect(model().attributeHasErrors("discount")).andExpect(model().attributeHasFieldErrors("discount", "percentage"))
				.andExpect(model().attributeHasFieldErrorCode("discount", "finishDate", "wrongDate")).andExpect(status().isOk())
				.andExpect(view().name("discounts/createOrUpdateDiscountForm"));
	}

	
	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testInitEditDiscountForm() throws Exception {
		mockMvc.perform(get("/shops/1/products/{productId}/discounts/{discountId}/edit", TEST_PRODUCT_ID_5, TEST_DISCOUNT_ID_3)).andExpect(status().isOk())
				.andExpect(model().attributeExists("discount"))
				.andExpect(model().attribute("discount", hasProperty("startDate", is(LocalDate.of(2020, 8, 01)))))
				.andExpect(model().attribute("discount", hasProperty("finishDate", is(LocalDate.of(2020, 9, 01)))))
				.andExpect(model().attribute("discount", hasProperty("percentage", is(30.0))))
				.andExpect(model().attribute("discount", hasProperty("id", is(TEST_DISCOUNT_ID_3))))
				.andExpect(view().name("discounts/createOrUpdateDiscountForm"));
	}
	
	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessEditDiscountFormSuccess() throws Exception {
		mockMvc.perform(post("/shops/1/products/{productId}/discounts/{discountId}/edit", TEST_PRODUCT_ID_1, TEST_DISCOUNT_ID_1).with(csrf())
				.param("startDate", "2020/05/02")
				.param("finishDate", "2020/06/03")
				.param("percentage", "20.0").param("id", "1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/shops/1/products/{productId}"));
	}
	
	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessEditDiscountFormHasErrors() throws Exception {
		mockMvc.perform(post("/shops/1/products/{productId}/discounts/{discountId}/edit", TEST_PRODUCT_ID_3, TEST_DISCOUNT_ID_2).with(csrf())
				.param("startDate", "2020/05/06")
				.param("finishDate", "2020/05/01").param("id", "2"))
				.andExpect(model().attributeHasErrors("discount")).andExpect(model().attributeHasFieldErrors("discount", "percentage"))
				.andExpect(model().attributeHasFieldErrorCode("discount", "finishDate", "wrongDate")).andExpect(status().isOk())
				.andExpect(view().name("discounts/createOrUpdateDiscountForm"));
	}
}

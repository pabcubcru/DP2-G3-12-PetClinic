
package org.springframework.samples.petclinic.web.e2e;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class ProductControllerE2ETest {

	private static final int TEST_PRODUCT_ID_1 = 1;
	private static final int TEST_PRODUCT_ID_2 = 2;
	private static final int TEST_PRODUCT_ID_3 = 3;
	private static final int TEST_PRODUCT_ID_4 = 4;
	private static final int TEST_PRODUCT_ID_5 = 5;
	private static final int TEST_PRODUCT_ID_6 = 6;

	@Autowired
	private MockMvc mockMvc;

//	INSERT PRODUCT

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testInitNewProductForm() throws Exception {
		mockMvc.perform(get("/shops/1/products/new")).andExpect(model().attributeExists("product"))
				.andExpect(status().isOk()).andExpect(view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessNewProductFormSuccess() throws Exception {
		mockMvc.perform(post("/shops/1/products/new").with(csrf()).param("name", "product15").param("price", "18.0")
				.param("stock", "6")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/shops/1"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessNewProductFormHasErrors() throws Exception {
		mockMvc.perform(post("/shops/1/products/new").with(csrf()).param("name", "product20").param("stock", "6"))
				.andExpect(status().isOk()).andExpect(model().attributeHasErrors("product"))
				.andExpect(model().attributeHasFieldErrors("product", "price"))
				.andExpect(view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessNewProductFormHasErrorsDuplicatedName() throws Exception {
		mockMvc.perform(post("/shops/1/products/new").with(csrf()).param("name", "product1")
				.param("stock", "6").param("price", "18.0")).andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("product"))
				.andExpect(model().attributeHasFieldErrorCode("product", "name", "duplicatedName"))
				.andExpect(view().name("products/createOrUpdateProductForm"));
	}

//  EDIT PRODUCT

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testInitUpdateProductForm() throws Exception {
		mockMvc.perform(get("/shops/1/products/{productId}/edit", TEST_PRODUCT_ID_5)).andExpect(status().isOk())
				.andExpect(model().attributeExists("product"))
				.andExpect(model().attribute("product", hasProperty("name", is("product5"))))
				.andExpect(model().attribute("product", hasProperty("price", is(15.0))))
				.andExpect(model().attribute("product", hasProperty("id", is(TEST_PRODUCT_ID_5))))
				.andExpect(model().attribute("product", hasProperty("stock", is(10))))
				.andExpect(view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessUpdateProductFormSuccess() throws Exception {
		mockMvc.perform(post("/shops/1/products/{productId}/edit", TEST_PRODUCT_ID_6).with(csrf())
				.param("name", "product6").param("price", "18.0").param("stock", "6"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/shops/1/products/" + TEST_PRODUCT_ID_6));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessUpdateProductFormHasErrors() throws Exception {
		mockMvc.perform(post("/shops/1/products/{productId}/edit", TEST_PRODUCT_ID_1).with(csrf())
				.param("name", "product1").param("stock", "6").param("id", "1")).andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("product"))
				.andExpect(model().attributeHasFieldErrors("product", "price"))
				.andExpect(view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessUpdateProductFormHasErrorsDuplicatedName() throws Exception {
		mockMvc.perform(post("/shops/1/products/{productId}/edit", TEST_PRODUCT_ID_4).with(csrf())
				.param("name", "product1").param("stock", "6").param("price", "18.0")).andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("product"))
				.andExpect(model().attributeHasFieldErrorCode("product", "name", "duplicatedName"))
				.andExpect(view().name("products/createOrUpdateProductForm"));
	}

//	DELETE PRODUCT

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessDeleteProductFormHasOrdersRealizedWithIt() throws Exception {
		mockMvc.perform(get("/shops/1/products/{productId}/delete", TEST_PRODUCT_ID_2)).andExpect(status().isOk())
				.andExpect(view().name("/exception"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessDeleteProductFormSuccess() throws Exception {
		mockMvc.perform(get("/shops/1/products/{productId}/delete", TEST_PRODUCT_ID_3))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/shops/1"));
	}

//	SHOW PRODUCT

	@WithAnonymousUser
	@Test
	void testShowProductWithDiscountProduct3() throws Exception {
		mockMvc.perform(get("/shops/1/products/{productId}", TEST_PRODUCT_ID_3))
				.andExpect(model().attributeExists("activeDiscount", "priceWithDiscount", "canDeleteIt", "product"))
				.andExpect(model().attribute("canDeleteIt", true))
				.andExpect(model().attribute("priceWithDiscount", 9.0)) // 18 - (18 * 50) / 100
				.andExpect(model().attribute("activeDiscount", true))
				.andExpect(model().attribute("product", hasProperty("name", is("product3"))))
				.andExpect(model().attribute("product", hasProperty("price", is(18.0))))
				.andExpect(model().attribute("product", hasProperty("id", is(TEST_PRODUCT_ID_3))))
				.andExpect(model().attribute("product", hasProperty("stock", is(15)))).andExpect(status().isOk())
				.andExpect(view().name("products/productDetails"));
	}

}

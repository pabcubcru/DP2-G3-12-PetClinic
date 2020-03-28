
package org.springframework.samples.petclinic.web;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.security.AccessControlContext;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;
import javax.security.auth.SubjectDomainCombiner;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.internal.util.collections.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.model.Discount;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.model.OrderStatus;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Shop;
import org.springframework.samples.petclinic.service.DiscountService;
import org.springframework.samples.petclinic.service.OrderService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ShopService;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = ProductController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class ProductControllerTests {

	private static final int TEST_PRODUCT_ID_1 = 1;
	private static final int TEST_PRODUCT_ID_2 = 2;
	private static final int TEST_PRODUCT_ID_3 = 3;
	private static final int TEST_SHOP_ID = 1;

	@MockBean
	private ShopService shopService;

	@MockBean
	private OrderService orderService;

	@MockBean
	private DiscountService discountService;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ProductService clinicService;

	private Product testProduct1;
	private Product testProduct2;
	private Product testProduct3;
	
	@BeforeEach
	void setup() {
		Shop shop1 = new Shop();
		shop1.setId(TEST_SHOP_ID);
		shop1.setName("shop1");

		testProduct1 = new Product();
		testProduct1.setName("product1");
		testProduct1.setId(TEST_PRODUCT_ID_1);
		testProduct1.setPrice(18.0);
		testProduct1.setStock(6);

		Discount discount1 = new Discount();
		discount1.setFinishDate(LocalDate.now().plusDays(2));
		discount1.setName("test discount 1");
		discount1.setPercentage(30.0);
		discount1.setStartDate(LocalDate.now());
		discount1.setId(1);
		testProduct1.setDiscount(discount1);

		testProduct2 = new Product();
		testProduct2.setName("product2");
		testProduct2.setId(TEST_PRODUCT_ID_2);
		testProduct2.setPrice(30.0);
		testProduct2.setStock(10);
		
		testProduct3 = new Product();
		testProduct3.setName("product5");
		testProduct3.setId(TEST_PRODUCT_ID_3);
		testProduct3.setPrice(18.0);
		testProduct3.setStock(6);

		Discount discount2 = new Discount();
		discount2.setFinishDate(LocalDate.now());
		discount2.setName("test discount 2");
		discount2.setPercentage(30.0);
		discount2.setStartDate(LocalDate.now().minusDays(2));
		discount2.setId(2);
		testProduct3.setDiscount(discount2);

		Order order1 = new Order();
		order1.setId(1);
		order1.setProduct(testProduct1);
		order1.setName("order 1");
		order1.setSupplier("supplier");
		order1.setProductNumber(10);

		shop1.addOrder(order1);
		shop1.addProduct(testProduct1);
		shop1.addProduct(testProduct2);
		shop1.addProduct(testProduct3);

		List<Order> ordersP1 = new ArrayList<Order>();
		ordersP1.add(order1);

		List<String> productsNames = new ArrayList<String>();
		productsNames.add("product1");
		productsNames.add("product2");

		List<Shop> shops = new ArrayList<Shop>();
		shops.add(shop1);

		given(this.orderService.findOrdersByProductId(TEST_PRODUCT_ID_1)).willReturn(ordersP1);
		given(this.orderService.findOrdersByProductId(TEST_PRODUCT_ID_2)).willReturn(new ArrayList<Order>());
		given(this.orderService.findOrdersByProductId(TEST_PRODUCT_ID_3)).willReturn(new ArrayList<Order>());
		given(this.clinicService.findProductById(TEST_PRODUCT_ID_1)).willReturn(this.testProduct1);
		given(this.clinicService.findProductById(TEST_PRODUCT_ID_2)).willReturn(this.testProduct2);
		given(this.clinicService.findProductById(TEST_PRODUCT_ID_3)).willReturn(this.testProduct3);
		given(this.clinicService.findProductsNames()).willReturn(productsNames);
		given(this.shopService.findShops()).willReturn(shops);
	}

//	INSERT PRODUCT

	@WithMockUser(value = "spring")
	@Test
	void testInitNewProductForm() throws Exception {
		mockMvc.perform(get("/shops/1/products/new"))
				.andExpect(model().attributeExists("product")).andExpect(status().isOk())
				.andExpect(view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewProductFormSuccess() throws Exception {
		mockMvc.perform(post("/shops/1/products/new").with(csrf()).param("name", "product3").param("price", "18.0")
				.param("stock", "6")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/shops/1"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewProductFormHasErrors() throws Exception {
		mockMvc.perform(post("/shops/1/products/new").with(csrf()).param("name", "product3")
				.param("stock", "6")).andExpect(status().isOk()).andExpect(model().attributeHasErrors("product"))
				.andExpect(model().attributeHasFieldErrors("product", "price"))
				.andExpect(view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewProductFormHasErrorsDuplicatedName() throws Exception {
		mockMvc.perform(post("/shops/{shopId}/products/new", TEST_SHOP_ID).with(csrf()).param("name", "product1")
				.param("stock", "6").param("price", "18.0")).andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("product"))
				.andExpect(model().attributeHasFieldErrorCode("product", "name", "duplicatedName"))
				.andExpect(view().name("products/createOrUpdateProductForm"));
	}

//  EDIT PRODUCT

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateProductForm() throws Exception {
		mockMvc.perform(get("/shops/1/products/{productId}/edit", TEST_PRODUCT_ID_1)).andExpect(status().isOk())
				.andExpect(model().attributeExists("product"))
				.andExpect(model().attribute("product", hasProperty("name", is("product1"))))
				.andExpect(model().attribute("product", hasProperty("price", is(18.0))))
				.andExpect(model().attribute("product", hasProperty("id", is(TEST_PRODUCT_ID_1))))
				.andExpect(model().attribute("product", hasProperty("stock", is(6))))
				.andExpect(view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateProductFormSuccess() throws Exception {
		mockMvc.perform(post("/shops/1/products/{productId}/edit", TEST_PRODUCT_ID_1).with(csrf())
				.param("name", "product6").param("price", "18.0").param("stock", "6").param("id", "1"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/shops/1/products/" + TEST_PRODUCT_ID_1));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateProductFormHasErrors() throws Exception {
		mockMvc.perform(post("/shops/{shopId}/products/{productId}/edit", TEST_SHOP_ID, TEST_PRODUCT_ID_1).with(csrf())
				.param("name", "product1").param("stock", "6").param("id", "1")).andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("product"))
				.andExpect(model().attributeHasFieldErrors("product", "price"))
				.andExpect(view().name("products/createOrUpdateProductForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateProductFormHasErrorsDuplicatedName() throws Exception {
		mockMvc.perform(post("/shops/1/products/{productId}/edit", TEST_PRODUCT_ID_3).with(csrf()).param("name", "product1")
				.param("stock", "6").param("price", "18.0")).andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("product"))
				.andExpect(model().attributeHasFieldErrorCode("product", "name", "duplicatedName"))
				.andExpect(view().name("products/createOrUpdateProductForm"));
	}

//	DELETE PRODUCT

	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteProductFormHasOrdersInProcess() throws Exception {
		mockMvc.perform(get("/shops/1/products/{productId}/delete", TEST_PRODUCT_ID_1)).andExpect(status().isOk())
				.andExpect(view().name("/exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteProductFormSuccess() throws Exception {
		mockMvc.perform(get("/shops/1/products/{productId}/delete", TEST_PRODUCT_ID_3))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/shops/1"));
	}

//	SHOW PRODUCT

	@WithMockUser(value = "spring")
	@Test
	void testShowProductWithDiscountProduct1() throws Exception {
		mockMvc.perform(get("/shops/1/products/{productId}", TEST_PRODUCT_ID_1))
				.andExpect(model().attributeExists("activeDiscount", "priceWithDiscount", "canDeleteIt", "product"))
				.andExpect(model().attribute("canDeleteIt", false))
				.andExpect(model().attribute("priceWithDiscount", 12.6)) // 18 - (18 * 30) / 100
				.andExpect(model().attribute("activeDiscount", true))
				.andExpect(model().attribute("product", hasProperty("name", is("product1"))))
				.andExpect(model().attribute("product", hasProperty("price", is(18.0))))
				.andExpect(model().attribute("product", hasProperty("id", is(TEST_PRODUCT_ID_1))))
				.andExpect(model().attribute("product", hasProperty("stock", is(6))))
				.andExpect(status().isOk()).andExpect(view().name("products/productDetails"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowProductWithDiscountProduct3() throws Exception {
		mockMvc.perform(get("/shops/1/products/{productId}", TEST_PRODUCT_ID_3))
				.andExpect(model().attributeExists("activeDiscount", "priceWithDiscount", "canDeleteIt", "product"))
				.andExpect(model().attribute("canDeleteIt", true))
				.andExpect(model().attribute("priceWithDiscount", 12.6)) // 18 - (18 * 30) / 100
				.andExpect(model().attribute("activeDiscount", true))
				.andExpect(model().attribute("product", hasProperty("name", is("product5"))))
				.andExpect(model().attribute("product", hasProperty("price", is(18.0))))
				.andExpect(model().attribute("product", hasProperty("id", is(TEST_PRODUCT_ID_3))))
				.andExpect(model().attribute("product", hasProperty("stock", is(6))))
				.andExpect(status().isOk()).andExpect(view().name("products/productDetails"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testShowProductWithOutDiscount() throws Exception {
		mockMvc.perform(get("/shops/1/products/{productId}", TEST_PRODUCT_ID_2))
				.andExpect(model().attributeExists("canDeleteIt", "product"))
				.andExpect(model().attribute("canDeleteIt", true))
				.andExpect(model().attribute("product", hasProperty("name", is("product2"))))
				.andExpect(model().attribute("product", hasProperty("price", is(30.0))))
				.andExpect(model().attribute("product", hasProperty("id", is(TEST_PRODUCT_ID_2))))
				.andExpect(model().attribute("product", hasProperty("stock", is(10))))
				.andExpect(status().isOk()).andExpect(view().name("products/productDetails"));
	}

}

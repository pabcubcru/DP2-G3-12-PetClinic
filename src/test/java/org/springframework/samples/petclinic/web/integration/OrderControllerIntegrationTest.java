package org.springframework.samples.petclinic.web.integration;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.model.OrderStatus;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Shop;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.service.OrderService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ShopService;
import org.springframework.samples.petclinic.web.OrderController;
import org.springframework.samples.petclinic.web.StayController;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.servlet.ModelAndView;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class OrderControllerIntegrationTest {

	@Autowired
	private OrderController orderController;

	@Autowired
	private ProductService productService;

	@Autowired
	private ShopService shopService;

	@Autowired
	private OrderService orderService;

	// INSERT ORDER

	@Test
	void testInitNewOrderForm() throws Exception {
		ModelMap model = new ModelMap();
		String view = orderController.initNewOrderForm(model);

		assertEquals(view, "orders/createOrUpdateOrderForm");
		assertNotNull(model.get("order"));

	}

	@Test
	void testProcessNewOrderFormSuccess() throws Exception {
		ModelMap model = new ModelMap();
		Order order = new Order();
		order.setName("order1");
		order.setSupplier("supplier");
		order.setProductNumber(10);
		Product p = productService.findProductById(1);
		order.setProduct(p);
		Shop s = shopService.findShopById(1);

		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		String view = orderController.processNewOrderForm(order, result, s, model);

		assertEquals(view, "redirect:/shops/1");
	}

	@Test
	void testProcessNewOrderFormHasErrors() throws Exception {
		ModelMap model = new ModelMap();
		Order order = new Order();
		order.setName("order1");
		order.setSupplier(null);
		order.setProductNumber(10);
		Product p = productService.findProductById(1);
		order.setProduct(p);
		Shop s = shopService.findShopById(1);

		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		result.rejectValue("supplier", "nullSupplier");
		String view = orderController.processNewOrderForm(order, result, s, model);

		assertEquals(view, "orders/createOrUpdateOrderForm");
		assertEquals(result.getFieldErrorCount("supplier"), 1);

	}

	@Test
	void testProcessOrderReceivedSuccess() throws Exception {
		Order order = orderService.findOrderById(2);
		Shop s = shopService.findShopById(1);

		String view = orderController.processOrderReceived(order.getId(), s.getId());
		assertEquals(view, "redirect:/shops/" + s.getId() + "/orders/" + order.getId());
	}

	@Test
	void testProcessOrderCanceledSuccess() throws Exception {
		Order order = orderService.findOrderById(1);
		Shop s = shopService.findShopById(1);

		String view = orderController.processOrderReceived(order.getId(), s.getId());
		assertEquals(view, "redirect:/shops/" + s.getId() + "/orders/" + order.getId());
	}

	@Test
	void testProcessOrderReceivedError() throws Exception {
		Order order = orderService.findOrderById(3);
		Shop s = shopService.findShopById(1);

		String view = orderController.processOrderReceived(order.getId(), s.getId());
		assertEquals(view, "/exception");
	}

	@Test
	void testProcessOrderCanceledError() throws Exception {
		Order order = orderService.findOrderById(5);
		Shop s = shopService.findShopById(1);

		String view = orderController.processOrderReceived(order.getId(), s.getId());
		assertEquals(view, "/exception");
	}

//	// DELETE ORDER
//
	@Test
	void testProcessDeleteOrdersFormInProcess() throws Exception {
		Order order = orderService.findOrderById(4);
		Shop s = shopService.findShopById(1);

		String view = orderController.processOrderDetele(order.getId(), s);
		assertEquals(view, "/exception");

	}

	@Test
	void testProcessDeleteOrderFormSuccess() throws Exception {
		Order order = orderService.findOrderById(5);
		Shop s = shopService.findShopById(1);

		String view = orderController.processOrderDetele(order.getId(), s);
		assertEquals(view, "redirect:/shops/" + s.getId());
	}

	@Test
	void testShowOrder() throws Exception {
		Order order = orderService.findOrderById(1);
		ModelAndView view = orderController.showOrder(order.getId());

		assertEquals(view.getViewName(), "orders/orderDetails");
	}

}

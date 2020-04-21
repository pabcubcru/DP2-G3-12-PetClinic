package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;


import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Shop;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class OrderServiceTests {

	@Autowired
	protected OrderService orderService;

	@Autowired
	protected ProductService productService;

	@Autowired
	protected ShopService shopService;

	@Test
	@Transactional
	public void shouldMakeOrder() {
		Product product = productService.findProductById(1);
		int numOrdersBefore = this.orderService.countOrdersByProductId(product.getId());
		Shop shop = product.getShop();

		Order order = new Order();
		order.setName("order3");
		order.setProduct(product);
		order.setProductNumber(50);
		order.setShop(shop);
		order.setSupplier("supplier3");

		this.orderService.saveOrder(order);
		assertThat(order.getId().longValue()).isNotEqualTo(0);

		int numOrdersAfter = this.orderService.countOrdersByProductId(product.getId());
		assertThat(numOrdersAfter).isEqualTo(numOrdersBefore + 1);
	}

	@Test
	@Transactional
	public void shouldThrowExceptionMakingOrder() {
		Product product = productService.findProductById(1);
		Shop shop = product.getShop();

		Order order = new Order();
		order.setName("order3");
		order.setProduct(product);
		order.setProductNumber(50);
		order.setShop(shop);
		order.setSupplier("");
		shop.addOrder(order);

		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			shop.addOrder(order);
			orderService.saveOrder(order);
		});
	}

	@Test
	void shouldFindOrderById() {
		Order order = this.orderService.findOrderById(1);
		assertThat(order.getName().equals("order1"));
		assertThat(order.getShop().getId().equals(1));

	}

	@Test
	void shouldFindOrdersByProductId() {
		int numOrders = orderService.countOrdersByProductId(1);
		assertThat(numOrders == 0);

		numOrders = orderService.countOrdersByProductId(2);
		assertThat(numOrders == 0);
	}

	@Test
	void shouldFindOrders() {
		Iterable<Order> orders = orderService.findOrders();
		assertThat(orders).asList().size().isEqualTo(3);
	}

	// DELETE ORDER
	@Test
	@Transactional
	public void shouldDeleteOrder() throws Exception {
		Product product = productService.findProductById(2);
		int numOrdersBefore = this.orderService.countOrdersByProductId(product.getId());
		Order order1 = this.orderService.findOrderById(3);
		Shop shop1 = shopService.findShops().iterator().next();
		shop1.deleteOrder(order1);

		this.orderService.deleteOrder(order1);

		int numOrdersAfter = this.orderService.countOrdersByProductId(product.getId());
		assertThat(numOrdersAfter).isEqualTo(numOrdersBefore - 1);

	}
}

package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.model.OrderStatus;
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

	@Test
	@Transactional
	public void shouldMakeOrder() {
		Product product = productService.findProductById(1);
		List<Order> orders = this.orderService.findOrdersByProductId(product.getId());
		int tam = orders.size();
		Shop shop = product.getShop();

		Order order = new Order();
		order.setName("order3");
		order.setOrderDate(LocalDateTime.now());
		order.setOrderStatus(OrderStatus.INPROCESS);
		order.setProduct(product);
		order.setProductNumber(50);
		order.setShop(shop);
		order.setSupplier("supplier3");

		this.orderService.saveOrder(order);
		assertThat(order.getId().longValue()).isNotEqualTo(0);

		orders = this.orderService.findOrdersByProductId(product.getId());
		assertThat(orders.size()).isEqualTo(tam + 1);
	}

	@Test
	@Transactional
	public void shouldUpdateOrder() {
		Order order = orderService.findOrderById(1);
		order.setOrderStatus(OrderStatus.CANCELED);
		this.orderService.saveOrder(order);

		order = this.orderService.findOrderById(1);
		assertThat(order.getOrderStatus().equals(OrderStatus.CANCELED));

	}

	@Test
	void shouldFindOrderById() {
		Order order = this.orderService.findOrderById(1);
		assertThat(order.getName().equals("order1"));
		assertThat(order.getShop().getId().equals(1));
		
	}
	
	@Test
	void shouldFindOrdersByProductId() {
		List<Order> orders = orderService.findOrdersByProductId(1);
		assertThat(orders.isEmpty()).isTrue();
		
		orders = orderService.findOrdersByProductId(2);
		assertThat(orders.size()).isEqualTo(2);
	}
	
	@Test
	void shouldFindOrders() {
		Iterable<Order> orders = orderService.findOrders();
		assertThat(orders).asList().size().isEqualTo(2);
	}

}

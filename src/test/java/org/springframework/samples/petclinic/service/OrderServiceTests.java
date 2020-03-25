package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
	
	@Test
	@Transactional
	public void shouldMakeOrder() {
		Product product = productService.findProductById(1);
		List<Order> orders = this.orderService.findOrdersByProductId(product.getId());
		int tam = orders.size();
		Shop shop = product.getShop();

		Order order = new Order();
		order.setName("order3");
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

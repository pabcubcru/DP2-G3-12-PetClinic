package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.repository.springdatajpa.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class OrderService {

	private OrderRepository orderRepository;
	
	@Autowired
	public OrderService(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}	
	
	@Transactional(readOnly = true)
	public Order findOrderById(int id) throws DataAccessException {
		return orderRepository.findById(id);
	}
	
	@Transactional(readOnly = true)	
	public Iterable<Order> findOrders() throws DataAccessException {
		return orderRepository.findAll();
	}	
	
	@Transactional
	public void saveOrder(Order order) throws DataAccessException {
		this.orderRepository.save(order);
	}
	
	@Transactional
	public List<Order> findOrdersByProductId(int productId) throws DataAccessException {
		return this.orderRepository.findOrderByProductId(productId);
	}
}

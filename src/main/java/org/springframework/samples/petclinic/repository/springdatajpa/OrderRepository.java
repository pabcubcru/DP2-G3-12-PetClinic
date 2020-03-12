package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer>{

	@Query("SELECT o FROM Order o WHERE o.id = ?1")
	public Order findById(int id);
	
	@Query("SELECT o FROM Order o WHERE o.product.id = ?1")
	public List<Order> findOrderByProductId(int idProduct);
}

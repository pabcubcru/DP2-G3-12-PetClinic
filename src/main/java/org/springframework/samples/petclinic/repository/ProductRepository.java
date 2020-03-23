package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer>{

	
	@Query("select p.name from Product p")
	public List<String> getNames();
	
	@Query("SELECT p FROM Product p WHERE p.name = ?1")
	public Product findByName(String name);
}

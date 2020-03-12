package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer>{

	@Query("SELECT p FROM Product p WHERE p.id = ?1")
	public Product findById(int id);
}

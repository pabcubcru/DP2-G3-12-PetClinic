package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.repository.springdatajpa.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

	private ProductRepository productRepository;
	
	@Autowired
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}	
	
	@Transactional(readOnly = true)
	public Product findProductById(int id) throws DataAccessException {
		return productRepository.findById(id);
	}
	
	@Transactional(readOnly = true)	
	public Iterable<Product> findProducts() throws DataAccessException {
		return productRepository.findAll();
	}
	
	@Transactional
	public void saveProduct(Product product) throws DataAccessException {
		this.productRepository.save(product);
	}
}

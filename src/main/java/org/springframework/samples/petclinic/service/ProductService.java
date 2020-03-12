package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.repository.ProductRepository;
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
	public List<String> findProductsNames() throws DataAccessException {
		return productRepository.getNames();
	}
	
	@Transactional
	public void saveProduct(Product product) throws DataAccessException {
		this.productRepository.save(product);
	}
	
	@Transactional
	public void deleteProduct(Product product) throws DataAccessException {
		this.productRepository.delete(product);
	}
	
	@Transactional
	public Product findByName(String name) throws DataAccessException {
		return this.productRepository.findByName(name);
	}
}


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
	public ProductService(final ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Product findProductById(final int id) throws DataAccessException {
		return this.productRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public List<String> findProductsNames() throws DataAccessException {
		return this.productRepository.getNames();
	}

	@Transactional
	public void saveProduct(final Product product) throws DataAccessException {
		this.productRepository.save(product);
	}

	@Transactional
	public void deleteProduct(final Product product) throws DataAccessException {
		this.productRepository.delete(product);
	}

	@Transactional
	public Product findByName(final String name) throws DataAccessException {
		return this.productRepository.findByName(name);
	}
}

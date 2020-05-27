
package org.springframework.samples.petclinic.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
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

	@Transactional(readOnly = true)
	public Product findProductById(final int id) throws DataAccessException {
		return this.productRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public List<String> findProductsNames() throws DataAccessException {
		return this.productRepository.getNames();
	}

	@Transactional
	@CacheEvict(cacheNames = "shopById", allEntries = true)
	public void saveProduct(Product product) throws DataAccessException {
		this.productRepository.save(product);
	}

	@Transactional
	@CacheEvict(cacheNames = "shopById", allEntries = true)
	public void deleteProduct(final Product product) throws DataAccessException {
		this.productRepository.delete(product);
	}

	@Transactional(readOnly = true)
	public Product findByName(final String name) throws DataAccessException {
		return this.productRepository.findByName(name);
	}
}

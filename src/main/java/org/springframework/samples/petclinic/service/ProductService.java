
package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.repository.ProductRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedProductNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class ProductService {

	private ProductRepository productRepository;

	@Autowired
	public ProductService(final ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public Product findProductById(final int id) throws DataAccessException {
		return this.productRepository.findById(id).get();
	}

	@Transactional(readOnly = true)
	public List<String> findProductsNames() throws DataAccessException {
		return this.productRepository.getNames();
	}

	@Transactional(rollbackFor = DuplicatedProductNameException.class)
	public void saveProduct(Product product) throws DataAccessException, DuplicatedProductNameException {
		
		if (product.getId() == null) { // CREAR PRODUCTO
			if (StringUtils.hasLength(product.getName()) && (!findProductsNames().contains(product.getName()))) {
				this.productRepository.save(product);

			} else {
				throw new DuplicatedProductNameException();
			}
		} else { // EDITAR PRODUCTO
			String oldName = this.productRepository.findById(product.getId()).get().getName();
			if (StringUtils.hasLength(product.getName())
					&& (oldName.equals(product.getName()) || !findProductsNames().contains(product.getName()))) {
				this.productRepository.save(product);

			} else {
				throw new DuplicatedProductNameException();
			}
		}
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

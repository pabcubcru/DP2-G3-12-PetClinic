/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Discount;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Shop;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test of the Service and the Repository layer.
 * <p>
 * ClinicServiceSpringDataJpaTests subclasses benefit from the following
 * services provided by the Spring TestContext Framework:
 * </p>
 * <ul>
 * <li><strong>Spring IoC container caching</strong> which spares us unnecessary
 * set up time between test execution.</li>
 * <li><strong>Dependency Injection</strong> of test fixture instances, meaning
 * that we don't need to perform application context lookups. See the use of
 * {@link Autowired @Autowired} on the <code>{@link
 * ClinicServiceTests#clinicService clinicService}</code> instance variable,
 * which uses autowiring <em>by type</em>.
 * <li><strong>Transaction management</strong>, meaning each test method is
 * executed in its own transaction, which is automatically rolled back by
 * default. Thus, even if tests insert or otherwise change database state, there
 * is no need for a teardown or cleanup script.
 * <li>An {@link org.springframework.context.ApplicationContext
 * ApplicationContext} is also inherited and can be used for explicit bean
 * lookup if necessary.</li>
 * </ul>
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Dave Syer
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace=Replace.NONE)
class ProductServiceTests {

	@Autowired
	protected ProductService productService;

	@Autowired
	protected ShopService shopService;

	@Autowired
	protected DiscountService discountService;

	@PersistenceContext
	private EntityManager entityManager;

//	FIND PRODUCT BY ID

	@Test
	void shouldFindProductWithCorrectId() throws Exception {
		Product product2 = this.productService.findProductById(2);
		assertThat(product2.getStock()).isEqualTo(10);
		assertThat(product2.getPrice()).isEqualTo(25.0);
	}

//	FIND PRODUCTS NAMES

	@Test
	void shoudFindProductsNames() throws Exception {
		List<String> products = this.productService.findProductsNames();
		assertThat(products.contains("product5")).isTrue();
		assertThat(products.contains("product15")).isFalse();
	}

//	 INSERT PRODUCT

	@Test
	@Transactional
	void shouldInsertNewProduct() throws Exception {
		List<String> products = this.productService.findProductsNames();
		int tamaño = products.size();
		Shop shop1 = shopService.findShops().iterator().next();
		Product product = new Product();
		product.setName("productTest");
		product.setPrice(50.0);
		product.setStock(20);
		shop1.addProduct(product);
		try {
			this.productService.saveProduct(product);
		} catch (Exception ex) {
			Logger.getLogger(ProductServiceTests.class.getName()).log(Level.SEVERE, null, ex);
		}
		products = this.productService.findProductsNames();
		assertThat(products.size()).isEqualTo(tamaño + 1);
		assertThat(product.getId()).isNotNull();
	}

	@Test
	@Transactional
	void shouldThrowExceptionInsertingNewProductNullParameter() throws Exception {
		Shop shop1 = shopService.findShops().iterator().next();
		Product product = new Product();
		product.setName("product3");
		product.setPrice(null);
		product.setStock(20);
		assertThrows(ConstraintViolationException.class, () -> {
			this.productService.saveProduct(product);
			shop1.addProduct(product);
		});
	}

	@Test
	@Transactional
	void shouldThrowExceptionInsertingNewProductDuplicatedName() throws Exception {
		Shop shop1 = shopService.findShopById(1 );
		Product product = new Product();
		product.setName("product1");
		product.setPrice(50.0);
		product.setStock(20);
		try {
			this.productService.saveProduct(product);
			shop1.addProduct(product);
		} catch (Exception ex) {
			Logger.getLogger(ProductServiceTests.class.getName()).log(Level.SEVERE, null, ex);
		}
		Product productTest = new Product();
		productTest.setName("product1");
		productTest.setPrice(50.0);
		productTest.setStock(20);
		assertThrows(Exception.class, () -> {
			this.productService.saveProduct(productTest);
			shop1.addProduct(productTest);
		});
	}

//	 DELETE PRODUCT

	@Test
	@Transactional
	public void shouldDeleteProduct() throws Exception {
		List<String> products = this.productService.findProductsNames();
		Product product3 = this.productService.findProductById(3);
		int tamaño = products.size();
		Shop shop1 = shopService.findShopById(1);
		shop1.deleteProduct(product3);
		productService.deleteProduct(product3);
		if (product3.getDiscount() != null) {
			discountService.deleteDiscount(product3.getDiscount().getId());
		}
		entityManager.flush();
		products = this.productService.findProductsNames();
		assertThat(products.size()).isEqualTo(tamaño - 1);
	}

//	 EDIT PRODUCT

	@Test
	@Transactional
	public void shouldEditProductName() throws Exception {
		Product product1 = this.productService.findProductById(1);
		String oldName = product1.getName();

		String newName = oldName + "X";
		product1.setName(newName);

		try {
			this.productService.saveProduct(product1);
		} catch (Exception ex) {
			Logger.getLogger(ProductServiceTests.class.getName()).log(Level.SEVERE, null, ex);
		}
		product1 = this.productService.findProductById(1);
		assertThat(product1.getName()).isEqualTo(newName);
	}

	@Test
	@Transactional
	public void shouldThrowExceptionEditingProductNullParameter() throws Exception {
		Shop shop1 = shopService.findShops().iterator().next();
		Product product = new Product();
		product.setName("product2");
		product.setPrice(50.0);
		product.setStock(20);
		shop1.addProduct(product);
		assertThrows(Exception.class, () -> {
			this.productService.saveProduct(product);
		});
	}

	@Test
	@Transactional
	public void shouldThrowExceptionEditingProductDuplicatedName() throws Exception {
		Shop shop1 = shopService.findShops().iterator().next();
		Product product1 = new Product();
		product1.setName("productTest1");
		product1.setPrice(50.0);
		product1.setStock(20);
		shop1.addProduct(product1);
		Product product2 = new Product();
		product2.setName("productTest2");
		product2.setPrice(50.0);
		product2.setStock(20);
		shop1.addProduct(product2);

		try {
			this.productService.saveProduct(product1);
			this.productService.saveProduct(product2);
		} catch (Exception ex) {
			Logger.getLogger(ProductServiceTests.class.getName()).log(Level.SEVERE, null, ex);
		}
		assertThrows(Exception.class, () -> {
			product1.setName("productTest2");
			entityManager.flush();
			this.productService.saveProduct(product1);
		});
	}

//	 FIND PRODUCTS BY NAME

	@Test
	void shouldFindProductByName() throws Exception {
		Product product1 = this.productService.findProductById(1);
		assertThat(product1.getStock()).isEqualTo(5);
		assertThat(product1.getPrice()).isEqualTo(15.0);
	}
}

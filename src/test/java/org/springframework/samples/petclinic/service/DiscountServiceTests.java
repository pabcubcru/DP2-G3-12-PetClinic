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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Discount;
import org.springframework.stereotype.Service;

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
 * DiscountServiceTests#clinicService clinicService}</code> instance variable,
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
class DiscountServiceTests {

	@Autowired
	protected DiscountService discountService;

	@Autowired
	protected ProductService productService;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	void shouldFindDiscountById() {

		Discount discount1 = this.discountService.findDiscountById(1);

		assertThat(discount1.getStartDate().isEqual(LocalDate.of(2020, 9, 1)));
		assertThat(discount1.getPercentage()).isEqualTo(30.0);

	}
	
//	INSERT DISCOUNT

	@Test
	@Transactional
	void shouldInsertDiscountForProduct() throws Exception {
		Product product2 = this.productService.findProductById(2);
		Discount discount = new Discount();
		discount.setFinishDate(LocalDate.now().plusDays(2));
		discount.setStartDate(LocalDate.now());
		discount.setPercentage(50.0);
		this.discountService.saveDiscount(discount);
		product2.setDiscount(discount);
		product2 = this.productService.findProductById(2);
		assertThat(discount.getId()).isNotNull();
		assertThat(product2.getDiscount()).isNotNull();
	}

	@Test
	@Transactional
	void shouldThowsExceptionInsertingDiscountNullParameter() throws Exception {
		Discount discount = new Discount();
		discount.setFinishDate(LocalDate.now().plusDays(2));
		discount.setStartDate(LocalDate.now());
		assertThrows(Exception.class, () -> {
			discount.setPercentage(null);
			this.discountService.saveDiscount(discount);
		});
	}

//	EDIT DISCOUNT

	@Test
	@Transactional
	void shouldEditDiscountForProduct() throws Exception {
		Product product1 = this.productService.findProductById(1);
		Discount discount = this.discountService.findDiscountById(1);
		discount.setFinishDate(LocalDate.now().plusDays(2));
		this.discountService.saveDiscount(discount);
		product1.setDiscount(discount);
		assertThat(product1.getDiscount()).isNotNull();
		assertThat(product1.getDiscount().getFinishDate()).isEqualTo(LocalDate.now().plusDays(2));
	}

	@Test
	@Transactional
	void shouldThrowsExceptionEditingDiscountNullParameter() throws Exception {
		Discount discount = new Discount();
		discount.setFinishDate(LocalDate.now().plusDays(2));
		discount.setStartDate(LocalDate.now());
		try {
			this.discountService.saveDiscount(discount);
		} catch (Exception ex) {
			Logger.getLogger(ProductServiceTests.class.getName()).log(Level.SEVERE, null, ex);
		}
		assertThrows(Exception.class, () -> {
			discount.setFinishDate(null); 
			entityManager.flush();
			this.discountService.saveDiscount(discount);
		});
	}

// DELETE DISCOUNT
	
	@Test
	@Transactional
	public void shouldDeleteDiscount() throws Exception {
		Product product = productService.findProductById(1);
		Discount discount1 = this.discountService.findDiscountById(1);

		try {
			product.setDiscount(null); productService.saveProduct(product);
			this.discountService.deleteDiscount(discount1);
		} catch (Exception ex) {
			Logger.getLogger(DiscountServiceTests.class.getName()).log(Level.SEVERE, null, ex);
		}

		assertThat(product.getDiscount()).isNull();
		assertThat(this.discountService.findDiscountById(1)).isNull();
	}
}

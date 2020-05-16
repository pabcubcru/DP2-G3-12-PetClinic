
package org.springframework.samples.petclinic.model;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class ProductTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldNotValidateWhenNameEmpty() {
		Product product = new Product();
		product.setName("");
		product.setPrice(18.0);
		product.setStock(6);
		Discount discount = new Discount();
		product.setDiscount(discount);
		Shop shop = new Shop();
		product.setShop(shop);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Product> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
		Assertions.assertThat(violation.getMessage()).isEqualTo("no puede estar vacío");

	}

	@Test
	void shouldNotValidateWhenPriceNull() {
		Product product = new Product();
		product.setName("Product 1");
		product.setPrice(null);
		product.setStock(6);
		Discount discount = new Discount();
		product.setDiscount(discount);
		Shop shop = new Shop();
		product.setShop(shop);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Product> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("price");
		Assertions.assertThat(violation.getMessage()).isEqualTo("no puede ser null");

	}

	@Test
	void shouldNotValidateWhenPriceWrongRange() {
		Product product = new Product();
		product.setName("Product 1");
		product.setPrice(-1.);
		product.setStock(6);
		Discount discount = new Discount();
		product.setDiscount(discount);
		Shop shop = new Shop();
		product.setShop(shop);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Product> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("price");
		Assertions.assertThat(violation.getMessage()).isEqualTo("tiene que estar entre 0 y 9223372036854775807");

	}

	//	@Test
	//	void shouldNotValidateWhenStockNull() {
	//		Product product = new Product();
	//		product.setName("Product 1");
	//		product.setPrice(20.);
	//		product.setStock((Integer) null);
	//		Discount discount = new Discount();
	//		product.setDiscount(discount);
	//		Shop shop = new Shop();
	//		product.setShop(shop);
	//
	//		Validator validator = this.createValidator();
	//		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);
	//
	//		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
	//		ConstraintViolation<Product> violation = constraintViolations.iterator().next();
	//		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("price");
	//		Assertions.assertThat(violation.getMessage()).isEqualTo("no puede ser null");
	//
	//	}

	@Test
	void shouldNotValidateWhenStockWrongRange() {
		Product product = new Product();
		product.setName("Product 1");
		product.setPrice(20.);
		product.setStock(-1);
		Discount discount = new Discount();
		product.setDiscount(discount);
		Shop shop = new Shop();
		product.setShop(shop);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Product>> constraintViolations = validator.validate(product);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Product> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("stock");
		Assertions.assertThat(violation.getMessage()).isEqualTo("tiene que estar entre 0 y 9223372036854775807");

	}
}

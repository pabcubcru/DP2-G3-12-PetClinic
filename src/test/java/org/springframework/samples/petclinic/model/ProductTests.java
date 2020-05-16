
package org.springframework.samples.petclinic.model;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class ProductTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldNotValidateWhenNameEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

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
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be blank");

	}

	@Test
	void shouldNotValidateWhenPriceNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

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
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be null");

	}

	@Test
	void shouldNotValidateWhenPriceWrongRange() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

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
		Assertions.assertThat(violation.getMessage()).isEqualTo("must be between 0 and 9223372036854775807");

	}

	@Test
	void shouldNotValidateWhenStockWrongRange() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

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
		Assertions.assertThat(violation.getMessage()).isEqualTo("must be between 0 and 9223372036854775807");

	}
}

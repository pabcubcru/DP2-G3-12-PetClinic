
package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class OrderValidatorTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldNotValidateWhenSupplierEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Order order = new Order();
		order.setSupplier("");
		order.setProductNumber(1);
		order.setOrderDate(LocalDateTime.of(2020, 02, 11, 12, 30));
		order.setOrderStatus(OrderStatus.INPROCESS);
		Shop shop1 = new Shop();
		order.setShop(shop1);
		Product product = new Product();
		order.setProduct(product);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Order>> constraintViolations = validator.validate(order);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Order> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("supplier");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be blank");

	}

	@Test
	void shouldNotValidateWhenProductNumberWrongRange() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Order order = new Order();
		order.setSupplier("Supplier");
		order.setProductNumber(0);
		order.setOrderDate(LocalDateTime.of(2020, 02, 11, 12, 30));
		order.setOrderStatus(OrderStatus.INPROCESS);
		Shop shop1 = new Shop();
		order.setShop(shop1);
		Product product = new Product();
		order.setProduct(product);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Order>> constraintViolations = validator.validate(order);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Order> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("productNumber");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must be between 1 and 9223372036854775807");

	}

	@Test
	void shouldNotValidateWhenOrderStatusNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Order order = new Order();
		order.setSupplier("Supplier");
		order.setProductNumber(1);
		order.setOrderDate(LocalDateTime.of(2020, 02, 11, 12, 30));
		order.setOrderStatus(null);
		Shop shop1 = new Shop();
		order.setShop(shop1);
		Product product = new Product();
		order.setProduct(product);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Order>> constraintViolations = validator.validate(order);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Order> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("orderStatus");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be null");

	}
}

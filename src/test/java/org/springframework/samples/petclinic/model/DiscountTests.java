
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class DiscountTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldNotValidateWhenPercentageNull() {
		Discount discount = new Discount();
		discount.setPercentage(null);
		discount.setStartDate(LocalDate.of(2020, 11, 10));
		discount.setFinishDate(LocalDate.of(2020, 12, 10));

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Discount>> constraintViolations = validator.validate(discount);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Discount> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("percentage");
		Assertions.assertThat(violation.getMessage()).isEqualTo("no puede ser null");

	}

	@Test
	void shouldNotValidateWhenPercentageWrongRange() {
		Discount discount = new Discount();
		discount.setPercentage(-1.);
		discount.setStartDate(LocalDate.of(2020, 11, 10));
		discount.setFinishDate(LocalDate.of(2020, 12, 10));

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Discount>> constraintViolations = validator.validate(discount);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Discount> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("percentage");
		Assertions.assertThat(violation.getMessage()).isEqualTo("tiene que estar entre 0 y 9223372036854775807");

	}

	@Test
	void shouldNotValidateWhenStartDateNull() {
		Discount discount = new Discount();
		discount.setPercentage(30.0);
		discount.setStartDate(null);
		discount.setFinishDate(LocalDate.of(2020, 12, 10));

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Discount>> constraintViolations = validator.validate(discount);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Discount> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("startDate");
		Assertions.assertThat(violation.getMessage()).isEqualTo("no puede ser null");

	}

	@Test
	void shouldNotValidateWhenFinishDateNull() {
		Discount discount = new Discount();
		discount.setPercentage(30.0);
		discount.setStartDate(LocalDate.of(2020, 11, 10));
		discount.setFinishDate(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Discount>> constraintViolations = validator.validate(discount);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Discount> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getMessage()).isEqualTo("no puede ser null");

	}

	@Test
	void shouldNotValidateWhenStartDateInPast() {
		Discount discount = new Discount();
		discount.setPercentage(30.0);
		discount.setStartDate(LocalDate.of(2019, 12, 10));
		discount.setFinishDate(LocalDate.of(2020, 12, 10));

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Discount>> constraintViolations = validator.validate(discount);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Discount> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("startDate");
		Assertions.assertThat(violation.getMessage()).isEqualTo("tiene que ser una fecha en el presente o en el futuro");

	}

	@Test
	void shouldNotValidateWhenFinishDateInPast() {
		Discount discount = new Discount();
		discount.setPercentage(30.0);
		discount.setStartDate(LocalDate.of(2020, 12, 10));
		discount.setFinishDate(LocalDate.of(2019, 12, 10));

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Discount>> constraintViolations = validator.validate(discount);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Discount> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("finishDate");
		Assertions.assertThat(violation.getMessage()).isEqualTo("tiene que ser una fecha en el presente o en el futuro");

	}

}


package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class DiscountValidatorTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldNotValidateWhenPercentageNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Discount discount = new Discount();
		discount.setPercentage(null);
		discount.setStartDate(LocalDate.of(2020, 11, 10));
		discount.setFinishDate(LocalDate.of(2020, 12, 10));

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Discount>> constraintViolations = validator.validate(discount);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Discount> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("percentage");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be null");

	}

	@Test
	void shouldNotValidateWhenPercentageWrongRange() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Discount discount = new Discount();
		discount.setPercentage(-1.);
		discount.setStartDate(LocalDate.of(2020, 11, 10));
		discount.setFinishDate(LocalDate.of(2020, 12, 10));

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Discount>> constraintViolations = validator.validate(discount);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Discount> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("percentage");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must be between 0 and 9223372036854775807");

	}

	@Test
	void shouldNotValidateWhenStartDateNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Discount discount = new Discount();
		discount.setPercentage(30.0);
		discount.setStartDate(null);
		discount.setFinishDate(LocalDate.of(2020, 12, 10));

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Discount>> constraintViolations = validator.validate(discount);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Discount> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("startDate");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be null");

	}

	@Test
	void shouldNotValidateWhenFinishDateNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Discount discount = new Discount();
		discount.setPercentage(30.0);
		discount.setStartDate(LocalDate.of(2020, 11, 10));
		discount.setFinishDate(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Discount>> constraintViolations = validator.validate(discount);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Discount> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be null");

	}

	@Test
	void shouldNotValidateWhenStartDateInPast() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Discount discount = new Discount();
		discount.setPercentage(30.0);
		discount.setStartDate(LocalDate.of(2019, 12, 10));
		discount.setFinishDate(LocalDate.of(2020, 12, 10));

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Discount>> constraintViolations = validator.validate(discount);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Discount> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("startDate");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must be a date in the present or in the future");

	}

	@Test
	void shouldNotValidateWhenFinishDateInPast() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Discount discount = new Discount();
		discount.setPercentage(30.0);
		discount.setStartDate(LocalDate.of(2020, 12, 10));
		discount.setFinishDate(LocalDate.of(2019, 12, 10));

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Discount>> constraintViolations = validator.validate(discount);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Discount> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("finishDate");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must be a date in the present or in the future");

	}

}

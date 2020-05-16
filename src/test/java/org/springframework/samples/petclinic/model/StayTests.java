
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

public class StayTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldNotValidateWhenStartDateNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Stay stay = new Stay();
		Pet pet = new Pet();
		stay.setPet(pet);
		stay.setStartdate(null);
		stay.setFinishdate(LocalDate.of(2020, 11, 10));
		stay.setPrice(10.);
		stay.setSpecialCares("Special care");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Stay>> constraintViolations = validator.validate(stay);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Stay> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("startdate");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be null");

	}

	@Test
	void shouldNotValidateWhenFinishDateNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Stay stay = new Stay();
		Pet pet = new Pet();
		stay.setPet(pet);
		stay.setStartdate(LocalDate.of(2020, 11, 10));
		stay.setFinishdate(null);
		stay.setPrice(10.);
		stay.setSpecialCares("Special care");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Stay>> constraintViolations = validator.validate(stay);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Stay> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("finishdate");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be null");

	}

	@Test
	void shouldNotValidateWhenFinishDateInPast() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Stay stay = new Stay();
		Pet pet = new Pet();
		stay.setPet(pet);
		stay.setStartdate(LocalDate.of(2020, 11, 10));
		stay.setFinishdate(LocalDate.of(2019, 11, 10));
		stay.setPrice(10.);
		stay.setSpecialCares("Special care");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Stay>> constraintViolations = validator.validate(stay);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Stay> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("finishdate");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must be a date in the present or in the future");

	}

	@Test
	void shouldNotValidateWhenPriceNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Stay stay = new Stay();
		Pet pet = new Pet();
		stay.setPet(pet);
		stay.setStartdate(LocalDate.of(2020, 11, 10));
		stay.setFinishdate(LocalDate.of(2020, 12, 10));
		stay.setPrice(null);
		stay.setSpecialCares("Special care");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Stay>> constraintViolations = validator.validate(stay);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Stay> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("price");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be null");

	}

	@Test
	void shouldNotValidateWhenPriceWrongRange() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Stay stay = new Stay();
		Pet pet = new Pet();
		stay.setPet(pet);
		stay.setStartdate(LocalDate.of(2020, 11, 10));
		stay.setFinishdate(LocalDate.of(2020, 12, 10));
		stay.setPrice(-1.);
		stay.setSpecialCares("Special care");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Stay>> constraintViolations = validator.validate(stay);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Stay> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("price");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must be between 0 and 9223372036854775807");

	}

	@Test
	void shouldNotValidateWhenSpecialCaresEmpty() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Stay stay = new Stay();
		Pet pet = new Pet();
		stay.setPet(pet);
		stay.setStartdate(LocalDate.of(2020, 11, 10));
		stay.setFinishdate(LocalDate.of(2020, 12, 10));
		stay.setPrice(18.);
		stay.setSpecialCares("");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Stay>> constraintViolations = validator.validate(stay);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Stay> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("specialCares");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be blank");

	}
}

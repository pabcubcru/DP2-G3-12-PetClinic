
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class StayTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldNotValidateWhenStartDateNull() {
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
		Assertions.assertThat(violation.getMessage()).isEqualTo("no puede ser null");

	}

	@Test
	void shouldNotValidateWhenFinishDateNull() {
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
		Assertions.assertThat(violation.getMessage()).isEqualTo("no puede ser null");

	}

	@Test
	void shouldNotValidateWhenFinishDateInPast() {
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
		Assertions.assertThat(violation.getMessage()).isEqualTo("tiene que ser una fecha en el presente o en el futuro");

	}

	@Test
	void shouldNotValidateWhenPriceNull() {
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
		Assertions.assertThat(violation.getMessage()).isEqualTo("no puede ser null");

	}

	@Test
	void shouldNotValidateWhenPriceWrongRange() {
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
		Assertions.assertThat(violation.getMessage()).isEqualTo("tiene que estar entre 0 y 9223372036854775807");

	}

	@Test
	void shouldNotValidateWhenSpecialCaresEmpty() {
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
		Assertions.assertThat(violation.getMessage()).isEqualTo("no puede estar vacío");

	}
}

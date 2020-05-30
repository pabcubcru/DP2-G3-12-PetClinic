
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

public class HospitalisationValidatorTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldNotValidateWhenStartDateNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Hospitalisation hospitalisation = new Hospitalisation();
		hospitalisation.setStartDate(null);
		hospitalisation.setFinishDate(LocalDate.of(2020, 11, 10));
		Pet pet = new Pet();
		hospitalisation.setPet(pet);
		HospitalisationStatus hs1 = new HospitalisationStatus();
		hospitalisation.setHospitalisationStatus(hs1);
		hospitalisation.setTreatment("Tratamiento 1");
		hospitalisation.setDiagnosis("Diagnostico 1");
		hospitalisation.setTotalPrice(30.0);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Hospitalisation>> constraintViolations = validator.validate(hospitalisation);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Hospitalisation> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("startDate");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be null");

	}

	@Test
	void shouldNotValidateWhenTreatmentNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Hospitalisation hospitalisation = new Hospitalisation();
		hospitalisation.setStartDate(LocalDate.of(2020, 11, 10));
		hospitalisation.setFinishDate(LocalDate.of(2020, 11, 15));
		Pet pet = new Pet();
		hospitalisation.setPet(pet);
		HospitalisationStatus hs1 = new HospitalisationStatus();
		hospitalisation.setHospitalisationStatus(hs1);
		hospitalisation.setTreatment(null);
		hospitalisation.setDiagnosis("Diagnostico 1");
		hospitalisation.setTotalPrice(30.0);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Hospitalisation>> constraintViolations = validator.validate(hospitalisation);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Hospitalisation> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("treatment");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be empty");

	}

	@Test
	void shouldNotValidateWhenDiagnosisNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Hospitalisation hospitalisation = new Hospitalisation();
		hospitalisation.setStartDate(LocalDate.of(2020, 11, 10));
		hospitalisation.setFinishDate(LocalDate.of(2020, 11, 15));
		Pet pet = new Pet();
		hospitalisation.setPet(pet);
		HospitalisationStatus hs1 = new HospitalisationStatus();
		hospitalisation.setHospitalisationStatus(hs1);
		hospitalisation.setTreatment("Tratamiento 1");
		hospitalisation.setDiagnosis(null);
		hospitalisation.setTotalPrice(30.0);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Hospitalisation>> constraintViolations = validator.validate(hospitalisation);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Hospitalisation> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("diagnosis");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be empty");

	}

	@Test
	void shouldNotValidateWhenTotalPriceNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Hospitalisation hospitalisation = new Hospitalisation();
		hospitalisation.setStartDate(LocalDate.of(2020, 11, 10));
		hospitalisation.setFinishDate(LocalDate.of(2020, 11, 15));
		Pet pet = new Pet();
		hospitalisation.setPet(pet);
		HospitalisationStatus hs1 = new HospitalisationStatus();
		hospitalisation.setHospitalisationStatus(hs1);
		hospitalisation.setTreatment("Tratamiento 1");
		hospitalisation.setDiagnosis("Dianostico 1");
		hospitalisation.setTotalPrice(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Hospitalisation>> constraintViolations = validator.validate(hospitalisation);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Hospitalisation> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("totalPrice");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must not be null");

	}

	@Test
	void shouldNotValidateWhenTotalPriceWrongRange() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Hospitalisation hospitalisation = new Hospitalisation();
		hospitalisation.setStartDate(LocalDate.of(2020, 11, 10));
		hospitalisation.setFinishDate(LocalDate.of(2020, 11, 15));
		Pet pet = new Pet();
		hospitalisation.setPet(pet);
		HospitalisationStatus hs1 = new HospitalisationStatus();
		hospitalisation.setHospitalisationStatus(hs1);
		hospitalisation.setTreatment("Tratamiento 1");
		hospitalisation.setDiagnosis("Dianostico 1");
		hospitalisation.setTotalPrice(-1.);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Hospitalisation>> constraintViolations = validator.validate(hospitalisation);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Hospitalisation> violation = constraintViolations.iterator().next();
		Assertions.assertThat(violation.getPropertyPath().toString()).isEqualTo("totalPrice");
		Assertions.assertThat(violation.getMessage()).isEqualTo("must be between 0 and 9223372036854775807");

	}

}

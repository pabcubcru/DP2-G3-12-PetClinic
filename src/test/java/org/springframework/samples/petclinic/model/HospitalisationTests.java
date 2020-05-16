
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class HospitalisationTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldNotValidateWhenStartDateNull() {
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
		Assertions.assertThat(violation.getMessage()).isEqualTo("no puede ser null");

	}

	@Test
	void shouldNotValidateWhenStartDateInPast() {
		Hospitalisation hospitalisation = new Hospitalisation();
		hospitalisation.setStartDate(LocalDate.of(2020, 01, 10));
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
		Assertions.assertThat(violation.getMessage()).isEqualTo("tiene que ser una fecha en el presente o en el futuro");

	}

	@Test
	void shouldNotValidateWhenTreatmentNull() {
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
		Assertions.assertThat(violation.getMessage()).isEqualTo("no puede estar vacío");

	}

	@Test
	void shouldNotValidateWhenDiagnosisNull() {
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
		Assertions.assertThat(violation.getMessage()).isEqualTo("no puede estar vacío");

	}

	@Test
	void shouldNotValidateWhenTotalPriceNull() {
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
		Assertions.assertThat(violation.getMessage()).isEqualTo("no puede ser null");

	}

	@Test
	void shouldNotValidateWhenTotalPriceWrongRange() {
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
		Assertions.assertThat(violation.getMessage()).isEqualTo("tiene que estar entre 0 y 9223372036854775807");

	}

}

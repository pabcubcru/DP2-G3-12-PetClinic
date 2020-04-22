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
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.samples.petclinic.model.Hospitalisation;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetStatus;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
 * ClinicServiceTests#clinicService clinicService}</code> instance variable,
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
class PetServiceTests {
	@Autowired
	protected PetService petService;

	@Autowired
	protected OwnerService ownerService;

	@PersistenceContext
	private EntityManager entityManager;

	@Test
	void shouldFindPetWithCorrectId() {
		Pet pet7 = this.petService.findPetById(7);
		assertThat(pet7.getName()).startsWith("Samantha");
		assertThat(pet7.getOwner().getFirstName()).isEqualTo("Jean");

	}
	
	@Test
	void shouldFindPetStatus() {
		Collection<PetStatus> status = this.petService.findPetStatus();
		assertThat(status.size()).isEqualTo(2);

	}

	@Test
	void shouldFindAllPetTypes() {
		Collection<PetType> petTypes = this.petService.findPetTypes();

		PetType petType1 = EntityUtils.getById(petTypes, PetType.class, 1);
		assertThat(petType1.getName()).isEqualTo("cat");
		PetType petType4 = EntityUtils.getById(petTypes, PetType.class, 4);
		assertThat(petType4.getName()).isEqualTo("snake");
	}

	@Test
	@Transactional
	public void shouldInsertPetIntoDatabaseAndGenerateId() {
		Owner owner6 = this.ownerService.findOwnerById(6);
		int found = owner6.getPets().size();

		Pet pet = new Pet();
		pet.setName("bowser");
		Collection<PetType> types = this.petService.findPetTypes();
		pet.setType(EntityUtils.getById(types, PetType.class, 2));
		pet.setBirthDate(LocalDate.now());
		owner6.addPet(pet);
		assertThat(owner6.getPets().size()).isEqualTo(found + 1);

		try {
			this.petService.savePet(pet);
		} catch (DuplicatedPetNameException ex) {
			Logger.getLogger(PetServiceTests.class.getName()).log(Level.SEVERE, null, ex);
		}
		this.ownerService.saveOwner(owner6);

		owner6 = this.ownerService.findOwnerById(6);
		assertThat(owner6.getPets().size()).isEqualTo(found + 1);
		// checks that id has been generated
		assertThat(pet.getId()).isNotNull();
	}

	@Test
	@Transactional
	public void shouldThrowExceptionInsertingPetsWithTheSameName() {
		Owner owner6 = this.ownerService.findOwnerById(6);
		Pet pet = new Pet();
		pet.setName("wario");
		Collection<PetType> types = this.petService.findPetTypes();
		pet.setType(EntityUtils.getById(types, PetType.class, 2));
		pet.setBirthDate(LocalDate.now());
		owner6.addPet(pet);
		try {
			petService.savePet(pet);
		} catch (DuplicatedPetNameException e) {
			// The pet already exists!
			e.printStackTrace();
		}

		Pet anotherPetWithTheSameName = new Pet();
		anotherPetWithTheSameName.setName("wario");
		anotherPetWithTheSameName.setType(EntityUtils.getById(types, PetType.class, 1));
		anotherPetWithTheSameName.setBirthDate(LocalDate.now().minusWeeks(2));
		Assertions.assertThrows(DuplicatedPetNameException.class, () -> {
			owner6.addPet(anotherPetWithTheSameName);
			petService.savePet(anotherPetWithTheSameName);
		});
	}

	@Test
	@Transactional
	public void shouldUpdatePetName() throws Exception {
		Pet pet7 = this.petService.findPetById(7);
		String oldName = pet7.getName();

		String newName = oldName + "X";
		pet7.setName(newName);
		this.petService.savePet(pet7);

		pet7 = this.petService.findPetById(7);
		assertThat(pet7.getName()).isEqualTo(newName);
	}

	@Test
	@Transactional
	public void shouldThrowExceptionUpdatingPetsWithTheSameName() {
		Owner owner6 = this.ownerService.findOwnerById(6);
		Pet pet = new Pet();
		pet.setName("wario");
		Collection<PetType> types = this.petService.findPetTypes();
		pet.setType(EntityUtils.getById(types, PetType.class, 2));
		pet.setBirthDate(LocalDate.now());
		owner6.addPet(pet);

		Pet anotherPet = new Pet();
		anotherPet.setName("waluigi");
		anotherPet.setType(EntityUtils.getById(types, PetType.class, 1));
		anotherPet.setBirthDate(LocalDate.now().minusWeeks(2));
		owner6.addPet(anotherPet);

		try {
			petService.savePet(pet);
			petService.savePet(anotherPet);
		} catch (DuplicatedPetNameException e) {
			// The pets already exists!
			e.printStackTrace();
		}

		Assertions.assertThrows(DuplicatedPetNameException.class, () -> {
			anotherPet.setName("wario");
			petService.savePet(anotherPet);
		});
	}

	@Test
	@Transactional
	public void shouldAddNewVisitForPet() {
		Pet pet7 = this.petService.findPetById(7);
		int found = pet7.getVisits().size();
		Visit visit = new Visit();
		pet7.addVisit(visit);
		visit.setDescription("test");
		this.petService.saveVisit(visit);
		try {
			this.petService.savePet(pet7);
		} catch (DuplicatedPetNameException ex) {
			Logger.getLogger(PetServiceTests.class.getName()).log(Level.SEVERE, null, ex);
		}

		pet7 = this.petService.findPetById(7);
		assertThat(pet7.getVisits().size()).isEqualTo(found + 1);
		assertThat(visit.getId()).isNotNull();
	}

	@Test
	@Transactional
	void shouldFindVisitsByPetId() throws Exception {
		Collection<Visit> visits = this.petService.findVisitsByPetId(7);
		assertThat(visits.size()).isEqualTo(2);
		Visit[] visitArr = visits.toArray(new Visit[visits.size()]);
		assertThat(visitArr[0].getPet()).isNotNull();
		assertThat(visitArr[0].getDate()).isNotNull();
		assertThat(visitArr[0].getPet().getId()).isEqualTo(7);
	}

	// ADD STAY

	@Test
	@Transactional
	public void shouldAddNewStayForPet() {
		Pet pet7 = this.petService.findPetById(7);
		int found = pet7.getStays().size();
		Stay stay = new Stay();
		stay.setFinishdate(LocalDate.now().plusDays(2));
		stay.setStartdate(LocalDate.now());
		stay.setPrice(15.0);
		stay.setSpecialCares("test special cares");
		this.petService.saveStay(stay);
		pet7.addStay(stay);
		pet7 = this.petService.findPetById(7);
		assertThat(pet7.getStays().size()).isEqualTo(found + 1);
		assertThat(stay.getId()).isNotNull();
	}

	@Test
	@Transactional
	public void shouldThrowExceptionInsertingStay() {
		Stay stay = new Stay();
		stay.setFinishdate(LocalDate.now().plusDays(2));
		stay.setStartdate(LocalDate.now());
		stay.setPrice(null);
		stay.setSpecialCares("special cares");
		assertThrows(ConstraintViolationException.class, () -> {
			this.petService.findPetById(7).addStay(stay);
			this.petService.saveStay(stay);
		});
	}

	@Test
	@Transactional
	void shouldFindStaysByPetIdCorrectNumberStays() throws Exception {
		Collection<Stay> stays = this.petService.findStaysByPetId(1);
		assertThat(stays.size()).isEqualTo(1); // Tiene que haber 1 porque es lo que esta populado en data.sql
	}

	@Test
	@Transactional
	void shouldFindStaysByPetIdPetNotNull() throws Exception {
		Collection<Stay> stays = this.petService.findStaysByPetId(1);
		Stay[] stayArr = stays.toArray(new Stay[stays.size()]);
		assertThat(stayArr[0].getPet()).isNotNull();
	}

	@Test
	@Transactional
	void shouldFindStaysByPetIdFinishDateNotNull() throws Exception {
		Collection<Stay> stays = this.petService.findStaysByPetId(1);
		Stay[] stayArr = stays.toArray(new Stay[stays.size()]);
		assertThat(stayArr[0].getFinishdate()).isNotNull();
	}

	@Test
	@Transactional
	void shouldFindStaysByPetIdStartDateNotNull() throws Exception {
		Collection<Stay> stays = this.petService.findStaysByPetId(1);
		Stay[] stayArr = stays.toArray(new Stay[stays.size()]);
		assertThat(stayArr[0].getStartdate()).isNotNull();
	}

	@Test
	@Transactional
	void shouldFindStaysByPetIdPriceNotNull() throws Exception {
		Collection<Stay> stays = this.petService.findStaysByPetId(1);
		Stay[] stayArr = stays.toArray(new Stay[stays.size()]);
		assertThat(stayArr[0].getPrice()).isNotNull();
	}

	@Test
	@Transactional
	void shouldFindStaysByPetIdSpecialCaresNotBlank() throws Exception {
		Collection<Stay> stays = this.petService.findStaysByPetId(1);
		Stay[] stayArr = stays.toArray(new Stay[stays.size()]);
		assertThat(stayArr[0].getSpecialCares()).isNotBlank();
	}

	@Test
	@Transactional
	void shouldFindStaysByPetIdEqualPetId() throws Exception {
		Collection<Stay> stays = this.petService.findStaysByPetId(1);
		Stay[] stayArr = stays.toArray(new Stay[stays.size()]);
		assertThat(stayArr[0].getPet().getId()).isEqualTo(1);
	}

	// EDIT STAY

	@Test
	@Transactional
	public void shouldUpdateStay() throws Exception {
		Pet pet7 = this.petService.findPetById(7);
		int found = pet7.getStays().size();
		Stay stay = petService.findStayById(1);
		stay.setStartdate(LocalDate.now());
		this.petService.saveStay(stay);
		assertThat(pet7.getStays().size()).isEqualTo(found);
		assertThat(stay.getStartdate()).isEqualTo(LocalDate.now());
	}

	@Test
	@Transactional
	public void shouldThrowExceptionUpdatingStay() throws Exception {
		Stay stay = petService.findStayById(1);
		assertThrows(Exception.class, () -> {
			stay.setPrice(null);
			entityManager.flush();
			this.petService.saveStay(stay);
		});
	}

	// ADD HOSPITALISATION

	@Test
	@Transactional
	public void shouldAddNewHospitalisationForPet() throws Exception {
		Pet pet7 = this.petService.findPetById(7);
		int found = pet7.getHospitalisations().size();
		Hospitalisation hospitalisation = new Hospitalisation();
		hospitalisation.setTotalPrice(15.0);
		hospitalisation.setTreatment("test treatment");
		hospitalisation.setDiagnosis("test diagnosis");
		this.petService.saveHospitalisation(hospitalisation);
		pet7.addHospitalisation(hospitalisation);
		pet7 = this.petService.findPetById(7);
		assertThat(pet7.getHospitalisations().size()).isEqualTo(found + 1);
		assertThat(hospitalisation.getId()).isNotNull();
	}

	@Test
	@Transactional
	public void shouldThrowExceptionInsertingHospitalisation() throws Exception {
		Hospitalisation hospitalisation = new Hospitalisation();
		hospitalisation.setTotalPrice(0.0);
		hospitalisation.setTreatment("test treatment");
		hospitalisation.setDiagnosis("test diagnosis");
		assertThrows(ConstraintViolationException.class, () -> {
			this.petService.findPetById(7).addHospitalisation(hospitalisation);
			this.petService.saveHospitalisation(hospitalisation);
		});
	}

	@Test
	@Transactional
	void shouldFindStaysByPetIdCorrectNumberHospitalisations() throws Exception {
		Collection<Hospitalisation> hospitalisations = this.petService.findHospitalisationsByPetId(7);
		assertThat(hospitalisations.size()).isEqualTo(2);
	}

	@Test
	@Transactional
	void shouldFindHospitalisationsByPetIdPetNotNull() throws Exception {
		Collection<Hospitalisation> hospitalisations = this.petService.findHospitalisationsByPetId(7);
		Hospitalisation[] hospitArr = hospitalisations.toArray(new Hospitalisation[hospitalisations.size()]);
		assertThat(hospitArr[0].getPet()).isNotNull();
	}

	@Test
	@Transactional
	void shouldFindHospitalisationsDischargedByPetIdFinishDateNotNull() throws Exception {
		Collection<Hospitalisation> hospitalisations = this.petService.findHospitalisationsByPetId(7);
		Hospitalisation[] hospitArr = hospitalisations.toArray(new Hospitalisation[hospitalisations.size()]);
		assertThat(hospitArr[0].getHospitalisationStatus().getName().equals("DISCHARGED"));
		assertThat(hospitArr[0].getFinishDate()).isNotNull();
	}
	
	@Test
	@Transactional
	void shouldFindHospitalisationsHospitalisedByPetIdFinishDateNull() throws Exception {
		Collection<Hospitalisation> hospitalisations = this.petService.findHospitalisationsByPetId(7);
		Hospitalisation[] hospitArr = hospitalisations.toArray(new Hospitalisation[hospitalisations.size()]);
		assertThat(hospitArr[1].getHospitalisationStatus().getName().equals("HOSPITALISED"));
		assertThat(hospitArr[1].getFinishDate()).isNull();
	}

	@Test
	@Transactional
	void shouldFindHospitalisationsByPetIdStartDateNotNull() throws Exception {
		Collection<Hospitalisation> hospitalisations = this.petService.findHospitalisationsByPetId(7);
		Hospitalisation[] hospitArr = hospitalisations.toArray(new Hospitalisation[hospitalisations.size()]);
		assertThat(hospitArr[0].getStartDate()).isNotNull();
	}

	@Test
	@Transactional
	void shouldFindHospitalisationsByPetIdTotalPriceNotNull() throws Exception {
		Collection<Hospitalisation> hospitalisations = this.petService.findHospitalisationsByPetId(7);
		Hospitalisation[] hospitArr = hospitalisations.toArray(new Hospitalisation[hospitalisations.size()]);
		assertThat(hospitArr[0].getTotalPrice()).isNotNull();
	}

	@Test
	@Transactional
	void shouldFindHospitalisationsByPetIdDiagnosisNotBlank() throws Exception {
		Collection<Hospitalisation> hospitalisations = this.petService.findHospitalisationsByPetId(7);
		Hospitalisation[] hospitArr = hospitalisations.toArray(new Hospitalisation[hospitalisations.size()]);
		assertThat(hospitArr[0].getDiagnosis()).isNotBlank();
	}

	@Test
	@Transactional
	void shouldFindHospitalisationsByPetIdTreatmentNotBlank() throws Exception {
		Collection<Hospitalisation> hospitalisations = this.petService.findHospitalisationsByPetId(7);
		Hospitalisation[] hospitArr = hospitalisations.toArray(new Hospitalisation[hospitalisations.size()]);
		assertThat(hospitArr[0].getTreatment()).isNotBlank();
	}

	@Test
	@Transactional
	void shouldFindHospitalisationsByPetIdEqualPetId() throws Exception {
		Collection<Hospitalisation> hospitalisations = this.petService.findHospitalisationsByPetId(7);
		Hospitalisation[] hospitArr = hospitalisations.toArray(new Hospitalisation[hospitalisations.size()]);
		assertThat(hospitArr[0].getPet().getId()).isEqualTo(7);
	}
	
	@Test
	@Transactional
	void shouldDeleteHospitalisation() throws Exception {
		Hospitalisation hospitalisation = this.petService.findHospitalisationById(1);
		Pet pet = hospitalisation.getPet();
		Integer numHosp = pet.getHospitalisations().size();
		this.petService.deleteHospitalisation(hospitalisation);
		assertThat(pet.getHospitalisations().size() == numHosp -1);
	}
}

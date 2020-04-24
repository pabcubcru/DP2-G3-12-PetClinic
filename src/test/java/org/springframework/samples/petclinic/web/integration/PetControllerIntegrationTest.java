package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.web.PetController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PetControllerIntegrationTest {
	
	@Autowired
	private PetController petController;
	
	@Autowired
	private OwnerService ownerService;
	
	@Autowired
	private PetService petService;
	
	@Test
	void testInitCreationForm() throws Exception {
		ModelMap model = new ModelMap();
		Owner owner = this.ownerService.findOwnerById(1);
		model.put("pet", new Pet());
		String view = petController.initCreationForm(owner, model);
		
		assertEquals(view, "pets/createOrUpdatePetForm");
		assertNotNull(model.get("pet"));
	}
	
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		ModelMap model = new ModelMap();
		Pet pet = new Pet();
		pet.setBirthDate(LocalDate.of(2017, 04, 07));
		pet.setName("Travis");
		PetType pt = new PetType();
		pt.setId(1);
		pet.setType(pt);
		Owner owner = this.ownerService.findOwnerById(1);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		String view = petController.processCreationForm(owner, pet, result, model);
		
		assertEquals(view, "redirect:/owners/{ownerId}");
	}
	
	@Test
	void testProcessCreationFormHasErrorsDuplicatedName() throws Exception {
		ModelMap model = new ModelMap();
		Pet pet = new Pet();
		pet.setBirthDate(LocalDate.of(2017, 04, 07));
		pet.setName("Leo");
		PetType pt = new PetType();
		pt.setId(1);
		pet.setType(pt);
		Owner owner = this.ownerService.findOwnerById(1);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		String view = petController.processCreationForm(owner, pet, result, model);
		
		assertEquals(view, "pets/createOrUpdatePetForm");
		assertEquals(result.getFieldError("name").getCode(), "duplicate"); // Name is duplicated
	}
	
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		ModelMap model = new ModelMap();
		Pet pet = new Pet();
		pet.setName("Travis");
		PetType pt = new PetType();
		pt.setId(1);
		pet.setType(pt);
		Owner owner = this.ownerService.findOwnerById(1);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		result.rejectValue("birth_date", "nullBirthDate");
		String view = petController.processCreationForm(owner, pet, result, model);
		
		assertEquals(view, "pets/createOrUpdatePetForm");
		assertEquals(result.getFieldErrorCount("birth_date"), 1); // Birth date is null
	}
	
	@Test
	void testProcessCreationFormHasErrorsBirthDateFuture() throws Exception {
		ModelMap model = new ModelMap();
		Pet pet = new Pet();
		pet.setBirthDate(LocalDate.of(2021, 02, 02));
		pet.setName("Travis");
		PetType pt = new PetType();
		pt.setId(1);
		pet.setType(pt);
		Owner owner = this.ownerService.findOwnerById(1);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		String view = petController.processCreationForm(owner, pet, result, model);
		
		assertEquals(view, "pets/createOrUpdatePetForm");
		assertEquals(result.getFieldError("birth_date").getCode(), "Incorrect birthdate"); // Birth date is future
	}
	
	@Test
	void testInitUpdateForm() throws Exception {
		ModelMap model = new ModelMap();
		String view = petController.initUpdateForm(1, model);
		
		assertEquals(view, "pets/createOrUpdatePetForm");
		assertNotNull(model.get("pet"));
	}
	
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		ModelMap model = new ModelMap();
		Pet pet = this.petService.findPetById(1);
		pet.setName("Divock");
		Owner owner = this.ownerService.findOwnerById(1);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		String view = petController.processUpdateForm(pet, result, owner, pet.getId(), model);
		
		assertEquals(view, "redirect:/owners/{ownerId}");
	}
	
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		ModelMap model = new ModelMap();
		Pet pet = new Pet();
		pet.setName("Divock");
		Owner owner = this.ownerService.findOwnerById(1);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		result.rejectValue("birth_date", "nullBirthDate");
		String view = petController.processUpdateForm(pet, result, owner, 1, model);
		
		assertEquals(view, "pets/createOrUpdatePetForm");
		assertEquals(result.getFieldErrorCount("birth_date"), 1); // Birth date is null
	}
	
	@Test
	void testProcessUpdateFormHasErrorsDuplicatedName() throws Exception {
		ModelMap model = new ModelMap();
		Pet pet = this.petService.findPetById(1);
		pet.setName("Basil");
		Owner owner = this.ownerService.findOwnerById(1);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		String view = petController.processUpdateForm(pet, result, owner, pet.getId(), model);
		
		assertEquals(view, "pets/createOrUpdatePetForm");
		assertEquals(result.getFieldError("name").getCode(), "duplicate"); // Name is duplicated
	}
	
	@Test
	void testProcessUpdateFormHasErrorsBirthDateFuture() throws Exception {
		ModelMap model = new ModelMap();
		Pet pet = this.petService.findPetById(1);
		pet.setBirthDate(LocalDate.of(2021, 02, 02));
		Owner owner = this.ownerService.findOwnerById(1);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		String view = petController.processUpdateForm(pet, result, owner, 1, model);
		
		assertEquals(view, "pets/createOrUpdatePetForm");
		assertEquals(result.getFieldError("birth_date").getCode(), "Incorrect birthdate"); // Birth date is future
	}
	
	@Test
	void testInitDeleteFormSuccess() throws Exception {
		Owner owner = this.ownerService.findOwnerById(2);
		String view = petController.initDeleteForm(2, owner);
		
		assertEquals(view, "redirect:/owners/{ownerId}");
	}
	
	@Test
	void testInitDeleteFormHasErrors() throws Exception {
		Owner owner = this.ownerService.findOwnerById(1);
		String view = petController.initDeleteForm(1, owner);
		
		assertEquals(view, "/exception");
	}

}

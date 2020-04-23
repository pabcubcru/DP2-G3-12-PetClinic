package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Hospitalisation;
import org.springframework.samples.petclinic.model.HospitalisationStatus;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.web.HospitalisationController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class HospitalisationControllerIntegrationTest {
	
	@Autowired
	private HospitalisationController hospitalisationController;
	
	@Autowired
	private PetService petService;

	@Test
	void testInitNewHospitalisationForm() throws Exception {
		ModelMap model = new ModelMap();
		Pet pet = this.petService.findPetById(1);
		model.put("hospitalisation", new Hospitalisation());
		String view = hospitalisationController.initNewHospitalisationForm(pet, model);
		
		assertEquals(view, "pets/createOrUpdateHospitalisationForm");
		assertNotNull(model.get("hospitalisation"));
	}
	
	@Test
	void testProcessNewHospitalisationFormSuccess() throws Exception {
		ModelMap model = new ModelMap();
		Hospitalisation hospitalisation = new Hospitalisation();
		hospitalisation.setDiagnosis("NONE");
		hospitalisation.setTotalPrice(100.);
		hospitalisation.setTreatment("NONE");
		Pet pet = this.petService.findPetById(1);
		Owner owner = pet.getOwner();
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		model.put("hospitalisation", hospitalisation);
		String view = hospitalisationController.processNewHospitalisationForm(hospitalisation, result, pet, model);
		
		assertEquals(view, "redirect:/owners/" + owner.getId());
	}
	
	@Test
	void testProcessNewHospitalisationFormHasErrors() throws Exception {
		ModelMap model = new ModelMap();
		Hospitalisation hospitalisation = new Hospitalisation();
		Pet pet = this.petService.findPetById(1);
		hospitalisation.setDiagnosis("NONE");
		hospitalisation.setTotalPrice(null);
		hospitalisation.setTreatment("NONE");
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		model.put("pet", pet);
		result.rejectValue("price", "nullPrice");
		String view = hospitalisationController.processNewHospitalisationForm(hospitalisation, result, pet, model);
		
		assertEquals(view, "pets/createOrUpdateHospitalisationForm");
		assertEquals(result.getFieldErrorCount("price"), 1); // Price is null
	}
	
	@Test
	void testInitEditHospitalisationForm() throws Exception {
		ModelMap model = new ModelMap();
		Pet pet = petService.findPetById(1);
		Hospitalisation hospitalisation = petService.findHospitalisationById(1);
		model.put("hospitalisation", hospitalisation);
		String view = hospitalisationController.initEditHospitalisationForm(pet, model, 1);
		
		assertEquals(view, "pets/createOrUpdateHospitalisationForm");
		assertNotNull(model.get("hospitalisation"));
	}
	
	@Test
	void testProcessEditHospitalisationFormSuccess() throws Exception {
		ModelMap model = new ModelMap();
		Hospitalisation hospitalisation = new Hospitalisation();
		Pet pet = petService.findPetById(1);
		Owner owner = pet.getOwner();
		hospitalisation.setDiagnosis("NONE");
		hospitalisation.setTotalPrice(150.);
		hospitalisation.setTreatment("NONE");
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		int hospitalisationId = 1;
		model.put("hospitalisation", hospitalisation);
		String view = hospitalisationController.processEditHospitalisationForm(hospitalisation, result, pet, model, hospitalisationId);
		
		assertEquals(view, "redirect:/owners/" + owner.getId());
	}
	
	@Test
	void testProcessEditHospitalisationFormHasErrors() throws Exception {
		ModelMap model = new ModelMap();
		Hospitalisation hospitalisation = new Hospitalisation();
		Pet pet = petService.findPetById(1);
		HospitalisationStatus hs = new HospitalisationStatus();
		hs.setName("HOSPITALISED");
		hospitalisation.setHospitalisationStatus(hs);
		hospitalisation.setTotalPrice(150.);
		hospitalisation.setTreatment("NONE");
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		result.rejectValue("diagnosis", "nullDiagnosis");
		int hospitalisationId = 1;
		String view = hospitalisationController.processEditHospitalisationForm(hospitalisation, result, pet, model, hospitalisationId);
		
		assertEquals(view, "pets/createOrUpdateHospitalisationForm");
		assertEquals(result.getFieldErrorCount("diagnosis"), 1); // Diagnosis is null
	}
	
	@Test
	void testShowHospitalisations() throws Exception {
		ModelMap model = new ModelMap();
		Pet pet = this.petService.findPetById(1);
		String view = hospitalisationController.showHospitalisations(pet, model);
		
		assertEquals(view, "hospitalisationList");
	}
	
	@Test
	void testInitDeleteHospitalisationFormSuccess() throws Exception {
		Pet pet = this.petService.findPetById(1);
		Owner owner = pet.getOwner();
		Hospitalisation hospitalisation = this.petService.findHospitalisationById(1);
		String view = hospitalisationController.initDeleteForm(pet.getId(), hospitalisation.getId());
		
		assertEquals(view, "redirect:/owners/" + owner.getId());
	}
	
	@Test
	void testInitDeleteHospitalisationFormHasErrors() throws Exception {
		Pet pet = this.petService.findPetById(7);
		Hospitalisation hospitalisation = this.petService.findHospitalisationById(3);
		String view = hospitalisationController.initDeleteForm(pet.getId(), hospitalisation.getId());
		
		assertEquals(view, "/exception");
		
	}
	
}

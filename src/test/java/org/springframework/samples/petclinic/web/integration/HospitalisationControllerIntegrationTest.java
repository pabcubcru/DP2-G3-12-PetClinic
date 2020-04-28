package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Hospitalisation;
import org.springframework.samples.petclinic.model.HospitalisationStatus;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.web.HospitalisationController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HospitalisationControllerIntegrationTest {
	
	@Autowired
	private HospitalisationController hospitalisationController;
	
	@Autowired
	private PetService petService;
	
	@Test
	void testInitNewHospitalisationForm() throws Exception {
		ModelMap model = new ModelMap();
		Pet pet = this.petService.findPetById(3);
		model.put("hospitalisation", new Hospitalisation());
		String view = hospitalisationController.initNewHospitalisationForm(pet, model);
		
		assertEquals(view, "pets/createOrUpdateHospitalisationForm");
		assertNotNull(model.get("hospitalisation"));
	}
	
	@Test
	void testProcessNewHospitalisationFormSuccess() throws Exception {
		Hospitalisation hospitalisation = new Hospitalisation();
		hospitalisation.setDiagnosis("NONE");
		hospitalisation.setTotalPrice(100.);
		hospitalisation.setTreatment("NONE");
		Pet pet = this.petService.findPetById(4);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		String view = hospitalisationController.processNewHospitalisationForm(hospitalisation, result, pet);
		
		assertEquals(view, "redirect:/owners/{ownerId}");
	}
	
	@Test
	void testProcessNewHospitalisationFormHasErrors() throws Exception {
		Hospitalisation hospitalisation = new Hospitalisation();
		Pet pet = this.petService.findPetById(1);
		hospitalisation.setDiagnosis("NONE");
		hospitalisation.setTotalPrice(null);
		hospitalisation.setTreatment("NONE");
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		result.rejectValue("price", "nullPrice");
		String view = hospitalisationController.processNewHospitalisationForm(hospitalisation, result, pet);
		
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
		hospitalisation.setDiagnosis("NONE");
		hospitalisation.setTotalPrice(150.);
		hospitalisation.setTreatment("NONE");
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		int hospitalisationId = 1;
		model.put("hospitalisation", hospitalisation);
		String view = hospitalisationController.processEditHospitalisationForm(hospitalisation, result, pet, model, hospitalisationId);
		
		assertEquals(view, "redirect:/owners/{ownerId}");
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
	void testInitDeleteHospitalisationFormSuccess() throws Exception {
		Pet pet = this.petService.findPetById(1);
		Hospitalisation hospitalisation = this.petService.findHospitalisationById(1);
		String view = hospitalisationController.initDeleteForm(pet, hospitalisation.getId());
		
		assertEquals(view, "redirect:/owners/{ownerId}");
	}
	
	@Test
	void testInitDeleteHospitalisationFormHasErrors() throws Exception {
		Pet pet = this.petService.findPetById(7);
		Hospitalisation hospitalisation = this.petService.findHospitalisationById(3);
		String view = hospitalisationController.initDeleteForm(pet, hospitalisation.getId());
		
		assertEquals(view, "/exception");
		
	}
	
}

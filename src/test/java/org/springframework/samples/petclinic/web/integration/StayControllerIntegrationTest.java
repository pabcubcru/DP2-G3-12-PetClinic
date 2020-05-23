package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.web.StayController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class StayControllerIntegrationTest {

	@Autowired
	private StayController stayController;

	@Autowired
	private PetService petService;

	// INSERTAR ESTANCIA

	@Test
	void testInitNewStayForm() throws Exception {
		ModelMap model = new ModelMap();
		String view = stayController.initNewStayForm(model);

		assertEquals(view, "pets/createOrUpdateStayForm");
		assertNotNull(model.get("stay"));
	}

	@Test
	void testProcessNewStayFormSuccess() throws Exception {
		Stay stay1 = new Stay();
		stay1.setStartdate(LocalDate.of(2020, 10, 01));
		stay1.setFinishdate(LocalDate.of(2020, 10, 10));
		stay1.setSpecialCares("Special Cares1");
		stay1.setPrice(20.0);
		Pet pet = this.petService.findPetById(1);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		String view = stayController.processNewStayForm(stay1, result, pet);

		assertEquals(view, "redirect:/owners/{ownerId}");
	}

	@Test
	void testProcessNewStayFormHasErrorsFinishDateBeforeStartDateAndPriceNull() throws Exception {
		Stay stay1 = new Stay();
		stay1.setStartdate(LocalDate.of(2020, 10, 05));
		stay1.setFinishdate(LocalDate.of(2020, 10, 01));
		stay1.setSpecialCares("Special Cares1");
		stay1.setPrice(null);
		Pet pet = this.petService.findPetById(1);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		result.rejectValue("price", "nullPrice");
		result.rejectValue("finishdate", "dateStartDateAfterDateFinishDate");
		String view = stayController.processNewStayForm(stay1, result, pet);

		assertEquals(view, "pets/createOrUpdateStayForm");
		assertEquals(result.getFieldErrorCount("price"), 1); // Price is null
		assertEquals(result.getFieldErrorCount("finishdate"), 2);
	}


	@Test
	void testProcessNewStayFormHasErrorsExistAnotherWithSamePeriod() throws Exception {
		Stay stay1 = new Stay();
		stay1.setStartdate(LocalDate.of(2020, 10, 05));
		stay1.setFinishdate(LocalDate.of(2020, 10, 20));
		stay1.setSpecialCares("Special Cares1");
		stay1.setPrice(null);
		Pet pet = this.petService.findPetById(1);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		result.rejectValue("finishdate", "dateStartDateAfterDateFinishDate");
		String view = stayController.processNewStayForm(stay1, result, pet);

		assertEquals(view, "pets/createOrUpdateStayForm");
		assertEquals(result.getFieldErrorCount("finishdate"), 1);
	}
	
	@Test
	void testProcessNewStayFormHasErrorsStartDateInPast() throws Exception {
		Stay stay1 = new Stay();
		stay1.setStartdate(LocalDate.of(2020, 01, 05));
		stay1.setFinishdate(LocalDate.of(2020, 10, 10));
		stay1.setSpecialCares("Special Cares1");
		stay1.setPrice(30.0);
		Pet pet = this.petService.findPetById(1);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		result.rejectValue("startdate", "dateStartDateIsPast");
		String view = stayController.processNewStayForm(stay1, result, pet);

		assertEquals(view, "pets/createOrUpdateStayForm");
		assertEquals(result.getFieldErrorCount("startdate"), 2);
	}

	// EDITAR ESTANCIA

	@Test
	void testInitEditStayForm() throws Exception {
		ModelMap model = new ModelMap();
		Pet pet = petService.findPetById(1);
		Stay stay = petService.findStayById(1);
		model.put("stay", stay);
		String view = stayController.initEditStayForm(pet, model, 1);

		assertEquals(view, "pets/createOrUpdateStayForm");
		assertNotNull(model.get("stay"));
	}

	@Test
	void testProcessEditStayFormSuccess() throws Exception {
		ModelMap model = new ModelMap();
		Stay stay2 = new Stay();
		stay2.setStartdate(LocalDate.of(2020, 11, 01));
		stay2.setFinishdate(LocalDate.of(2020, 11, 20));
		stay2.setSpecialCares("Special Cares2");
		stay2.setPrice(50.0);
		Pet pet = this.petService.findPetById(1);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		int stayId = 1;
		String view = stayController.processEditStayForm(stay2, result, pet, model, stayId);

		assertEquals(view, "redirect:/owners/{ownerId}");
	}

	@Test
	void testProcessEditStayFormHasErrors() throws Exception {
		ModelMap model = new ModelMap();
		Stay stay2 = new Stay();
		stay2.setStartdate(LocalDate.of(2020, 11, 20));
		stay2.setFinishdate(LocalDate.of(2020, 11, 01));
		stay2.setSpecialCares("Special Cares2");
		stay2.setPrice(50.0);
		Pet pet = this.petService.findPetById(1);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		int stayId = 1;
		result.rejectValue("finishdate", "dateStartDateAfterDateFinishDate");
		String view = stayController.processEditStayForm(stay2, result, pet, model, stayId);

		assertEquals(view, "pets/createOrUpdateStayForm");
		assertEquals(result.getFieldErrorCount("finishdate"), 1);
	}

	@Test
	void testProcessEditStayFormHasErrorsExistAnotherWithSamePeriod() throws Exception {
		ModelMap model = new ModelMap();
		Stay stay2 = new Stay();
		stay2.setStartdate(LocalDate.of(2020, 10, 5));
		stay2.setFinishdate(LocalDate.of(2020, 11, 01));
		stay2.setSpecialCares("Special Cares2");
		stay2.setPrice(50.0);
		Pet pet = this.petService.findPetById(1);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		int stayId = 1;
		result.rejectValue("finishdate", "duplicatedStay");
		String view = stayController.processEditStayForm(stay2, result, pet, model, stayId);

		assertEquals(view, "pets/createOrUpdateStayForm");
		assertEquals(result.getFieldErrorCount("finishdate"), 1);
	}

	@Test
	void testProcessEditStayFormHasErrorsDatesNull() throws Exception {
		ModelMap model = new ModelMap();
		Stay stay2 = new Stay();
		stay2.setStartdate(LocalDate.of(2020, 12, 01));
		stay2.setFinishdate(null);
		stay2.setSpecialCares("Special Cares2");
		stay2.setPrice(50.0);
		Pet pet = this.petService.findPetById(1);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		int stayId = 1;
		result.rejectValue("finishdate", "dateNull");
		String view = stayController.processEditStayForm(stay2, result, pet, model, stayId);

		assertEquals(view, "pets/createOrUpdateStayForm");
		assertEquals(result.getFieldErrorCount("finishdate"), 1);
	}

	// DAR POR FINALIZADA ESTANCIA

	@Test
	void testProcessEndStaySuccess() throws Exception {
		Stay stay3 = petService.findStayById(3);

		String view = stayController.initEndStayForm(stay3.getId());
		assertEquals(view, "redirect:/owners/{ownerId}");
	}

	@Test
	void testProcessEndStayError() throws Exception {
		Stay stay7 = petService.findStayById(7);

		String view = stayController.initEndStayForm(stay7.getId());
		assertEquals(view, "/exception");

	}
	
	// DELETE STAY
	
	@Test
	void testProcessDeleteStaySuccess() throws Exception {
		Stay stay2 = petService.findStayById(2);

		String view = stayController.initDeleteStayForm(stay2.getPet(), stay2.getId());
		assertEquals(view, "redirect:/owners/{ownerId}");
	}
	
	@Test
	void testProcessDeteleStayError() throws Exception {
		Stay stay8 = petService.findStayById(8);

		String view = stayController.initDeleteStayForm(stay8.getPet(), stay8.getId());
		assertEquals(view, "/exception");
	}


	void testShowStays() throws Exception {
		ModelMap model = new ModelMap();
		Pet pet = this.petService.findPetById(1);
		String view = stayController.showStays(pet, model);

		assertEquals(view, "stayList");
	}
}

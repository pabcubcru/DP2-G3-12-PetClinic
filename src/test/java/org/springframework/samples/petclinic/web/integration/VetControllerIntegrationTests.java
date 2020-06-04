package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.web.VetController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VetControllerIntegrationTests {
	
	@Autowired
	private VetController vetController;
	
	@Test
	void testInitNewOrderForm() throws Exception {
		ModelMap model = new ModelMap();
		String view = vetController.initCreationForm(model);

		assertEquals(view, "vets/createOrUpdateVetForm");
		assertNotNull(model.get("vet"));

	}

	@Test
	void testProcessNewVetFormSuccess() throws Exception {
		ModelMap model = new ModelMap();
		Vet vet = new Vet();
		vet.setFirstName("John");
		vet.setLastName("Snow");
		

		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		String view = vetController.processCreationForm(vet, result);

		assertEquals(view, "redirect:/vets/");
	}

	@Test
	void testProcessNewVetFormHasErrors() throws Exception {
		ModelMap model = new ModelMap();
		Vet vet = new Vet();
		vet.setFirstName("John");
		vet.setLastName(null);

		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		result.rejectValue("lastName", "nullLastName");
		String view = vetController.processCreationForm(vet, result);

		assertEquals(view, "vets/createOrUpdateVetForm");
		assertEquals(result.getFieldErrorCount("lastName"), 1);

	}


}

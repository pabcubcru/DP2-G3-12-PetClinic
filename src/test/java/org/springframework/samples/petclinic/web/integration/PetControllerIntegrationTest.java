
package org.springframework.samples.petclinic.web.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.web.PetController;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PetControllerIntegrationTest {

	@Autowired
	private PetController	petController;

	@Autowired
	private OwnerService	ownerService;


	@Test
	void testInitDeleteFormSuccess() throws Exception {
		Owner owner = this.ownerService.findOwnerById(2);
		String view = this.petController.initDeleteForm(2, owner);

		Assertions.assertEquals(view, "redirect:/owners/{ownerId}");
	}

	@Test
	void testInitDeleteFormHasErrors() throws Exception {
		Owner owner = this.ownerService.findOwnerById(1);
		String view = this.petController.initDeleteForm(1, owner);

		Assertions.assertEquals(view, "/exception");
	}

}

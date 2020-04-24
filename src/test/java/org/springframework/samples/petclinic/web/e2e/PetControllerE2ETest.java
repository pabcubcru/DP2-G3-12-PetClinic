package org.springframework.samples.petclinic.web.e2e;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc

public class PetControllerE2ETest {

	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_OWNER_ID_2 = 2;

	private static final int TEST_PET_ID = 1;
	private static final int TEST_PET_ID_2 = 2;

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testInitCreationForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/new", TEST_OWNER_ID)).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdatePetForm")).andExpect(model().attributeExists("pet"))
				.andExpect(model().attributeExists("owner"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/new", TEST_OWNER_ID).with(csrf()).param("name", "pet2")
				.param("type.name", "cat").param("birthDate", "2015/02/12"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID).with(csrf())
				.param("name", "Betty").param("birthDate", "2015/02/12"))
				.andExpect(model().attributeHasNoErrors("owner")).andExpect(model().attributeHasErrors("pet"))
				.andExpect(status().isOk()).andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID))
				.andExpect(status().isOk()).andExpect(model().attributeExists("pet"))
				.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID_2, TEST_PET_ID_2).with(csrf())
				.param("name", "Hamstersito").param("type.name", "hamster").param("birthDate", "2015/02/12")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", TEST_OWNER_ID, TEST_PET_ID).with(csrf())
				.param("name", "Betty").param("birthDate", "2015/02/12"))
				.andExpect(model().attributeHasNoErrors("owner")).andExpect(model().attributeHasErrors("pet"))
				.andExpect(status().isOk()).andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testInitDeleteFormSuccess() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/delete", TEST_OWNER_ID, 2))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testInitDeleteFormHasErrors() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/delete", TEST_OWNER_ID, TEST_PET_ID))
				.andExpect(status().isOk()).andExpect(view().name("/exception"));
	}

}
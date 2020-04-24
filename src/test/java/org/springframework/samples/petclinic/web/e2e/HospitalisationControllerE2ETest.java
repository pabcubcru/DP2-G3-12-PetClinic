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
public class HospitalisationControllerE2ETest {
	
	private static final int TEST_PET_ID = 1;
	private static final int TEST_PET_ID_2 = 2;
	private static final int TEST_HOSP_ID = 1;
	
	@Autowired
	private MockMvc mockMvc;
	
	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testInitNewHospitalisationForm() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/hospitalisations/new", TEST_PET_ID_2)).andExpect(status().isOk())
				.andExpect(model().attributeExists("hospitalisation"))
				.andExpect(view().name("pets/createOrUpdateHospitalisationForm"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testInitThrowExceptionNewHospitalisationForm() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/hospitalisations/new", TEST_PET_ID)).andExpect(status().isOk())
				.andExpect(model().attributeDoesNotExist("hospitalisation")).andExpect(view().name("/exception"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessNewHospitalisationFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/hospitalisations/new", 1, TEST_PET_ID).with(csrf())
				.param("totalPrice", "15.0").param("diagnosis", "test diagnosis").param("treatment", "test treatment"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessNewHospitalisationFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/1/pets/{petId}/hospitalisations/new", TEST_PET_ID_2).with(csrf())
				.param("diagnosis", "test diagnosis").param("treatment", "test treatment").param("totalPrice", "-25.0"))
				.andExpect(model().attributeHasErrors("hospitalisation"))
				.andExpect(model().attributeHasFieldErrors("hospitalisation", "totalPrice")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateHospitalisationForm"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testInitEditHospitalisationForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/hospitalisations/{hospitalisationId}/edit", 6, 7,
				3)).andExpect(status().isOk()).andExpect(model().attributeExists("hospitalisation"))
				.andExpect(view().name("pets/createOrUpdateHospitalisationForm"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessEditHospitalisationFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/hospitalisations/{hospitalisationId}/edit", 1, TEST_PET_ID_2,
				TEST_HOSP_ID).with(csrf()).param("totalPrice", "15.0").param("diagnosis", "test diagnosis")
						.param("treatment", "test treatment").param("hospitalisationStatus.name", "DISCHARGED"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessEditHospitalisationFormHasErrors() throws Exception {
		mockMvc.perform(
				post("/owners/1/pets/{petId}/hospitalisations/{hospitalisationId}/edit", TEST_PET_ID, TEST_HOSP_ID)
						.with(csrf()).param("diagnosis", "test diagnosis").param("totalPrice", "15")
						.param("hospitalisationStatus.name", "DISCHARGED"))
				.andExpect(model().attributeHasErrors("hospitalisation"))
				.andExpect(model().attributeHasFieldErrors("hospitalisation", "treatment")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateHospitalisationForm"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testInitDeleteFormSuccess() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/hospitalisations/{hospitalisationId}/delete", 2,
				TEST_PET_ID, TEST_HOSP_ID)).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testInitThrowExceptionDeleteForm() throws Exception {
		mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/hospitalisations/{hospitalisationId}/delete", 1, 7, 3))
				.andExpect(status().isOk()).andExpect(view().name("/exception"));
	}

}

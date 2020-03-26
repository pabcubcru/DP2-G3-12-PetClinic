package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = HospitalisationController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class HospitalisationControllerTest {


	private static final int TEST_PET_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PetService clinicService;
	
	@BeforeEach
	void setup() {
		given(this.clinicService.findPetById(TEST_PET_ID)).willReturn(new Pet());
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitNewStayForm() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/hospitalisations/new", TEST_PET_ID)).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateHospitalisationForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewStayFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/*/pets/{petId}/hospitalisations/new", TEST_PET_ID).with(csrf())
				.param("startDate", "2020/06/06")
				.param("finishDate", "2020/06/08").param("totalPrice", "15.0")
				.param("diagnosis", "test diagnosis").param("treatment", "test treatment")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewHospitalisationFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/*/pets/{petId}/hospitalisations/new", TEST_PET_ID).with(csrf()).param("startDate", "2020/06/06").param("finishDate", "2020/06/04")
				.param("diagnosis", "test diagnosis").param("treatment", "test treatment"))
				.andExpect(model().attributeHasErrors("hospitalisation")).andExpect(model().attributeHasFieldErrors("hospitalisation", "totalPrice"))
				.andExpect(model().attributeHasFieldErrorCode("hospitalisation", "finishDate", "dateStartDateAfterDateFinishDate")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateHospitalisationForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowStays() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/hospitalisations", TEST_PET_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("hospitalisations")).andExpect(view().name("hospitalisationList"));
	}
}
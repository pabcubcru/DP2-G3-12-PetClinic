package org.springframework.samples.petclinic.web;

import static org.hamcrest.Matchers.hasProperty;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.regex.Matcher;

import org.hamcrest.beans.HasProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = StayController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class StayControllerTest {

	private static final int TEST_PET_ID = 1;
	private static final int TEST_STAY_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PetService clinicService;
	
	private Stay testStay;

	@BeforeEach
	void setup() {
		testStay = new Stay();
		testStay.setFinishdate(LocalDate.now().plusDays(2));
		testStay.setStartdate(LocalDate.now());
		testStay.setPet(clinicService.findPetById(TEST_PET_ID));
		testStay.setId(TEST_STAY_ID);
		testStay.setPrice(30.0);
		testStay.setSpecialCares("test special cares");
		given(this.clinicService.findStayById(TEST_STAY_ID)).willReturn(this.testStay);
		given(this.clinicService.findPetById(TEST_PET_ID)).willReturn(new Pet());
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitNewStayForm() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/stays/new", TEST_PET_ID)).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewStayFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/*/pets/{petId}/stays/new", TEST_PET_ID).with(csrf())
				.param("startdate", "2020/06/06")
				.param("finishdate", "2020/06/08").param("price", "15.0")
				.param("specialCares", "A lot of special cares")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewStayFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/*/pets/{petId}/stays/new", TEST_PET_ID).with(csrf()).param("startdate", "2020/06/06").param("finishdate", "2020/06/04")
				.param("specialCares", "A lot of special cares"))
				.andExpect(model().attributeHasErrors("stay")).andExpect(model().attributeHasFieldErrors("stay", "price"))
				.andExpect(model().attributeHasFieldErrorCode("stay", "finishdate", "dateStartDateAfterDateFinishDate")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowStays() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/stays", TEST_PET_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("stays")).andExpect(view().name("stayList"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitEditStayForm() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/stays/{stayId}/edit", TEST_PET_ID, TEST_STAY_ID)).andExpect(status().isOk())
				.andExpect(model().attributeExists("stay"))
				.andExpect(model().attributeExists("pet"))
				.andExpect(model().attribute("pet", this.clinicService.findPetById(TEST_PET_ID)))
				.andExpect(model().attribute("stay", hasProperty("startdate", is(LocalDate.now()))))
				.andExpect(model().attribute("stay", hasProperty("finishdate", is(LocalDate.now().plusDays(2)))))
				.andExpect(model().attribute("stay", hasProperty("specialCares", is("test special cares"))))
				.andExpect(model().attribute("stay", hasProperty("price", is(30.0))))
				.andExpect(model().attribute("stay", hasProperty("id", is(TEST_STAY_ID))))
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessEditStayFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/*/pets/{petId}/stays/{stayId}/edit", TEST_PET_ID, TEST_STAY_ID).with(csrf()).param("startdate", "2020/06/06")
				.param("finishdate", "2020/06/08").param("price", "15.0")
				.param("specialCares", "A lot of special cares"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessEditStayFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/*/pets/{petId}/stays/{stayId}/edit", TEST_PET_ID, TEST_STAY_ID).with(csrf()).param("startdate", "2020/06/06")
				.param("finishdate", "2020/06/04")
				.param("specialCares", "A lot of special cares"))
				.andExpect(model().attributeHasErrors("stay")).andExpect(model().attributeHasFieldErrors("stay", "price"))
				.andExpect(model().attributeHasFieldErrorCode("stay", "finishdate", "dateStartDateAfterDateFinishDate")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}
}

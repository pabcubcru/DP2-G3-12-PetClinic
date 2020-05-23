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
import java.util.Map;

import org.assertj.core.util.Lists;
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
import org.springframework.web.bind.annotation.GetMapping;

@WebMvcTest(controllers = StayController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class StayControllerTest {

	private static final int TEST_PET_ID_1 = 1;
	private static final int TEST_PET_ID_2 = 2;
	private static final int TEST_STAY_ID_1 = 1;
	private static final int TEST_STAY_ID_2 = 2;
	private static final int TEST_STAY_ID_3 = 3;
	private static final int TEST_STAY_ID_4 = 4;

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PetService clinicService;

	private Stay testStay;

	@BeforeEach
	void setup() {
		testStay = new Stay();
		testStay.setStartdate(LocalDate.of(2020, 07, 15));
		testStay.setFinishdate(LocalDate.of(2020, 07, 19));
		testStay.setPet(clinicService.findPetById(TEST_PET_ID_1));
		testStay.setId(TEST_STAY_ID_1);
		testStay.setPrice(30.0);
		testStay.setSpecialCares("test special cares");

		Stay stay2 = new Stay();
		stay2.setId(TEST_STAY_ID_2);
		stay2.setStartdate(LocalDate.of(2020, 07, 20));
		stay2.setFinishdate(LocalDate.of(2020, 07, 25));
		
		Stay stay3 = new Stay();
		stay3.setId(TEST_STAY_ID_3);
		stay3.setStartdate(LocalDate.of(2020, 01, 01));
		stay3.setFinishdate(LocalDate.of(2020, 01, 10));
		
		Stay stay4 = new Stay();
		stay4.setId(TEST_STAY_ID_4);
		stay4.setStartdate(LocalDate.of(2020, 04, 01));
		stay4.setFinishdate(LocalDate.of(2021, 10, 10));

		Pet pet1 = new Pet();
		pet1.setId(TEST_PET_ID_1);
		Pet pet2 = new Pet();
		pet2.setId(TEST_PET_ID_2);
		
		given(this.clinicService.findStayById(TEST_STAY_ID_1)).willReturn(this.testStay);
		given(this.clinicService.findStayById(TEST_STAY_ID_2)).willReturn(stay2);
		given(this.clinicService.findPetById(TEST_PET_ID_1)).willReturn(pet1);
		given(this.clinicService.findPetById(TEST_PET_ID_2)).willReturn(pet2);
		given(this.clinicService.findStaysByPetId(TEST_PET_ID_1)).willReturn(Lists.emptyList());
		given(this.clinicService.findStaysByPetId(TEST_PET_ID_2)).willReturn(Lists.list(testStay, stay2));
		given(this.clinicService.findStayById(TEST_STAY_ID_3)).willReturn(stay3);
		given(this.clinicService.findStayById(TEST_STAY_ID_4)).willReturn(stay4);
	}

	
	// INSERTAR ESTANCIA
	
	@WithMockUser(value = "spring")
	@Test
	void testInitNewStayForm() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/stays/new", TEST_PET_ID_1)).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewStayFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/1/pets/{petId}/stays/new", TEST_PET_ID_1).with(csrf())
				.param("startdate", "2020/06/06").param("finishdate", "2020/06/08").param("price", "15.0")
				.param("specialCares", "A lot of special cares")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessNewStayFormHasErrorsFinishDateBeforeStartDateAndPriceNull() throws Exception {
		mockMvc.perform(
				post("/owners/1/pets/{petId}/stays/new", TEST_PET_ID_1).with(csrf()).param("startdate", "2020/06/06")
						.param("finishdate", "2020/06/04").param("specialCares", "A lot of special cares"))
				.andExpect(model().attributeHasErrors("stay"))
				.andExpect(model().attributeHasFieldErrors("stay", "price"))
				.andExpect(model().attributeHasFieldErrorCode("stay", "finishdate", "dateStartDateAfterDateFinishDate"))
				.andExpect(status().isOk()).andExpect(view().name("pets/createOrUpdateStayForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessNewStayFormHasErrorsExistAnotherWithSamePeriod() throws Exception {
		mockMvc.perform(post("/owners/1/pets/{petId}/stays/new", TEST_PET_ID_2).with(csrf())
				.param("startdate", "2020/07/21").param("finishdate", "2020/07/24")
				.param("price", "50.0").param("specialCares", "A lot of special cares"))
				.andExpect(model().attributeHasErrors("stay"))
				.andExpect(model().attributeHasFieldErrorCode("stay", "finishdate", "duplicatedStay"))
				.andExpect(status().isOk()).andExpect(view().name("pets/createOrUpdateStayForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessNewStayFormHasErrorsStartDateInPast() throws Exception {
		mockMvc.perform(
				post("/owners/1/pets/{petId}/stays/new", TEST_PET_ID_1).with(csrf()).param("startdate", "2020/03/06").param("price", "15.0")
						.param("finishdate", "2020/06/04").param("specialCares", "A lot of special cares"))
				.andExpect(model().attributeHasErrors("stay"))
				.andExpect(model().attributeHasFieldErrorCode("stay", "startdate", "dateStartDateIsPast"))
				.andExpect(status().isOk()).andExpect(view().name("pets/createOrUpdateStayForm"));
	}

	// EDITAR ESTANCIA
	
	@WithMockUser(value = "spring")
	@Test
	void testInitEditStayForm() throws Exception {
		mockMvc.perform(get("/owners/1/pets/{petId}/stays/{stayId}/edit", TEST_PET_ID_1, TEST_STAY_ID_1))
				.andExpect(status().isOk()).andExpect(model().attributeExists("stay"))
				.andExpect(model().attributeExists("pet"))
				.andExpect(model().attribute("pet", this.clinicService.findPetById(TEST_PET_ID_1)))
				.andExpect(model().attribute("stay", hasProperty("startdate", is(LocalDate.of(2020, 07, 15)))))
				.andExpect(model().attribute("stay", hasProperty("finishdate", is(LocalDate.of(2020, 07, 19)))))
				.andExpect(model().attribute("stay", hasProperty("specialCares", is("test special cares"))))
				.andExpect(model().attribute("stay", hasProperty("price", is(30.0))))
				.andExpect(model().attribute("stay", hasProperty("id", is(TEST_STAY_ID_1))))
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessEditStayFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/1/pets/{petId}/stays/{stayId}/edit", TEST_PET_ID_2, TEST_STAY_ID_1).with(csrf())
				.param("startdate", "2020/06/06").param("finishdate", "2020/06/08").param("price", "15.0")
				.param("id", "1").param("specialCares", "A lot of special cares"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessEditStayFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/1/pets/{petId}/stays/{stayId}/edit", TEST_PET_ID_2, TEST_STAY_ID_1).with(csrf())
				.param("startdate", "2020/06/06").param("finishdate", "2020/06/04").param("id", "1")
				.param("specialCares", "A lot of special cares")).andExpect(model().attributeHasErrors("stay"))
				.andExpect(model().attributeHasFieldErrors("stay", "price"))
				.andExpect(model().attributeHasFieldErrorCode("stay", "finishdate", "dateStartDateAfterDateFinishDate"))
				.andExpect(status().isOk()).andExpect(view().name("pets/createOrUpdateStayForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessEditStayFormHasErrorsExistAnotherWithSamePeriod() throws Exception {
		mockMvc.perform(post("/owners/1/pets/{petId}/stays/{stayId}/edit", TEST_PET_ID_2, TEST_STAY_ID_2).with(csrf())
				.param("startdate", "2020/07/18").param("finishdate", "2020/07/24").param("id", "2")
				.param("price", "50.0").param("specialCares", "A lot of special cares"))
				.andExpect(model().attributeHasErrors("stay"))
				.andExpect(model().attributeHasFieldErrorCode("stay", "finishdate", "duplicatedStay"))
				.andExpect(status().isOk()).andExpect(view().name("pets/createOrUpdateStayForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessEditStayFormHasErrorsDatesNull() throws Exception {
		mockMvc.perform(post("/owners/1/pets/{petId}/stays/{stayId}/edit", TEST_PET_ID_2, TEST_STAY_ID_2).with(csrf())
				.param("id", "2")
				.param("price", "50.0").param("specialCares", "A lot of special cares"))
				.andExpect(model().attributeHasErrors("stay"))
				.andExpect(status().isOk()).andExpect(view().name("pets/createOrUpdateStayForm"));
	}
	
	// DAR POR FINALIZADA ESTANCIA
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessEndStaySuccess() throws Exception {
		mockMvc.perform(get("/owners/1/pets/1/stays/{stayId}/end", TEST_STAY_ID_4)).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessEndStayError() throws Exception {
		mockMvc.perform(get("/owners/1/pets/1/stays/{stayId}/end", TEST_STAY_ID_3)).andExpect(status().isOk())
				.andExpect(view().name("/exception"));
	}
	
	
	// SHOW
	
	@WithMockUser(value = "spring")
	@Test
	void testShowStays() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/stays", TEST_PET_ID_1)).andExpect(status().isOk())
				.andExpect(model().attributeExists("stays")).andExpect(view().name("stayList"));
	}
	
	// DELETE STAY
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteStaySuccess() throws Exception {
		mockMvc.perform(get("/owners/1/pets/1/stays/{stayId}/delete", TEST_STAY_ID_1)).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testProcessDeleteStayError() throws Exception {
		mockMvc.perform(get("/owners/1/pets/1/stays/{stayId}/delete", TEST_STAY_ID_4)).andExpect(status().isOk())
				.andExpect(view().name("/exception"));
	}
}

package org.springframework.samples.petclinic.web.e2e;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

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
public class StayControllerE2ETest {

	private static final int TEST_PET_ID_1 = 1;
	private static final int TEST_PET_ID_2 = 2;
	private static final int TEST_STAY_ID_1 = 1;
	private static final int TEST_STAY_ID_2 = 2;
	private static final int TEST_STAY_ID_3 = 3;
	private static final int TEST_STAY_ID_4 = 4;
	private static final int TEST_STAY_ID_5 = 5;
	private static final int TEST_STAY_ID_7 = 7;

	@Autowired
	private MockMvc mockMvc;

	// INSERTAR ESTANCIA

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testInitNewStayForm() throws Exception {
		mockMvc.perform(get("/owners/*/pets/{petId}/stays/new", TEST_PET_ID_1)).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessNewStayFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/1/pets/{petId}/stays/new", TEST_PET_ID_1).with(csrf())
				.param("startdate", "2020/06/06").param("finishdate", "2020/06/08").param("price", "15.0")
				.param("specialCares", "A lot of special cares")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
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

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessNewStayFormHasErrorsStartDateInPast() throws Exception {
		mockMvc.perform(post("/owners/1/pets/{petId}/stays/new", TEST_PET_ID_1).with(csrf())
				.param("startdate", "2020/03/06").param("price", "15.0").param("finishdate", "2020/06/04")
				.param("specialCares", "A lot of special cares")).andExpect(model().attributeHasErrors("stay"))
				.andExpect(model().attributeHasFieldErrorCode("stay", "startdate", "dateStartDateIsPast"))
				.andExpect(status().isOk()).andExpect(view().name("pets/createOrUpdateStayForm"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessNewStayFormHasErrorsExistAnotherWithSamePeriod() throws Exception {
		mockMvc.perform(post("/owners/6/pets/{petId}/stays/new", 7).with(csrf())
				.param("startdate", "2020/05/10").param("finishdate", "2020/05/24").param("price", "50.0")
				.param("specialCares", "A lot of special cares")).andExpect(model().attributeHasErrors("stay"))
				.andExpect(model().attributeHasFieldErrorCode("stay", "finishdate", "duplicatedStay"))
				.andExpect(status().isOk()).andExpect(view().name("pets/createOrUpdateStayForm"));
	}

	// EDITAR ESTANCIA

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testInitEditStayForm() throws Exception {
		mockMvc.perform(get("/owners/1/pets/{petId}/stays/{stayId}/edit", TEST_PET_ID_1, TEST_STAY_ID_1))
				.andExpect(status().isOk()).andExpect(model().attributeExists("stay"))
				.andExpect(model().attributeExists("pet"))
				.andExpect(model().attribute("stay", hasProperty("startdate", is(LocalDate.of(2020, 07, 15)))))
				.andExpect(model().attribute("stay", hasProperty("finishdate", is(LocalDate.of(2020, 07, 19)))))
				.andExpect(model().attribute("stay", hasProperty("specialCares", is("test special cares"))))
				.andExpect(model().attribute("stay", hasProperty("price", is(30.0))))
				.andExpect(model().attribute("stay", hasProperty("id", is(TEST_STAY_ID_1))))
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessEditStayFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/6/pets/{petId}/stays/{stayId}/edit", 7, 4).with(csrf())
				.param("startdate", "2020/06/20").param("finishdate", "2020/06/25").param("price", "15.0")
				.param("specialCares", "A lot of special cares"))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessEditStayFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/1/pets/{petId}/stays/{stayId}/edit", 1, 1).with(csrf())
				.param("startdate", "2020/06/06").param("finishdate", "2020/05/04")
				.param("specialCares", "A lot of special cares")).andExpect(model().attributeHasErrors("stay"))
				.andExpect(model().attributeHasFieldErrors("stay", "price"))
				.andExpect(model().attributeHasFieldErrorCode("stay", "finishdate", "dateStartDateAfterDateFinishDate"))
				.andExpect(status().isOk()).andExpect(view().name("pets/createOrUpdateStayForm"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessEditStayFormHasErrorsExistAnotherWithSamePeriod() throws Exception {
		mockMvc.perform(post("/owners/7/pets/{petId}/stays/{stayId}/edit", 7, 4).with(csrf())
				.param("startdate", "2020/07/22").param("finishdate", "2020/08/27")
				.param("price", "50.0").param("specialCares", "A lot of special cares"))
				.andExpect(model().attributeHasErrors("stay"))
				.andExpect(model().attributeHasFieldErrorCode("stay", "finishdate", "duplicatedStay"))
				.andExpect(status().isOk()).andExpect(view().name("pets/createOrUpdateStayForm"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessEditStayFormHasErrorsDatesNull() throws Exception {
		mockMvc.perform(post("/owners/1/pets/{petId}/stays/{stayId}/edit", 1, 1).with(csrf())
				.param("price", "50.0").param("specialCares", "A lot of special cares"))
				.andExpect(model().attributeHasErrors("stay")).andExpect(status().isOk())
				.andExpect(view().name("pets/createOrUpdateStayForm"));
	}

	// DAR POR FINALIZADA ESTANCIA

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessEndStaySuccess() throws Exception {
		mockMvc.perform(get("/owners/6/pets/7/stays/{stayId}/end", TEST_STAY_ID_3))
				.andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessEndStayError() throws Exception {
		mockMvc.perform(get("/owners/10/pets/13/stays/{stayId}/end", TEST_STAY_ID_7)).andExpect(status().isOk())
				.andExpect(view().name("/exception"));
	}

}

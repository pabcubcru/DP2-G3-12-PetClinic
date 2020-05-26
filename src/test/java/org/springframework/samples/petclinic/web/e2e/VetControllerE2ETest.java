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
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class VetControllerE2ETest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testInitNewOrderForm() throws Exception {
		mockMvc.perform(get("/vets/new")).andExpect(status().isOk())
				.andExpect(view().name("vets/createOrUpdateVetForm"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessNewOrderFormSuccess() throws Exception {
		mockMvc.perform(post("/vets/new").with(csrf())
					.param("firstName", "John")
					.param("lastName", "Snow"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/vets/"));
	}

	@WithMockUser(username = "admin1", authorities = "admin")
	@Test
	void testProcessNewOrderFormHasErrors() throws Exception {
		mockMvc.perform(post("/vets/new").with(csrf())
				.param("firstName", "John"))
				.andExpect(status().isOk())
				.andExpect(model().attributeHasErrors("vet"))
				.andExpect(model().attributeHasFieldErrors("vet", "lastName"))
				.andExpect(view().name("vets/createOrUpdateVetForm"));
	}

}

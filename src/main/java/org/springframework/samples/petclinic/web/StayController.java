/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;


import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.util.Validaciones;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class StayController {

	private final PetService petService;

	@Autowired
	public StayController(PetService petService) {
		this.petService = petService;
	}
	
	@ModelAttribute("pet")
	public Pet loadPetWithVisit(@PathVariable("petId") int petId) {
		Pet pet = this.petService.findPetById(petId);
		return pet;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	// Spring MVC calls method loadPetWithStay(...) before initNewStayForm is called
	@GetMapping(value = "/owners/*/pets/{petId}/stays/new")
	public String initNewStayForm(Map<String, Object> model) {
		model.put("stay", new Stay());
		return "pets/createOrUpdateStayForm";
	}

	// Spring MVC calls method loadPetWithStay(...) before processNewStayForm is
	// called
	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/stays/new")
	public String processNewStayForm(@Valid Stay stay, BindingResult result, Pet pet) {
		Collection<Stay> stays = this.petService.findStaysByPetId(pet.getId());
		if (stay.getStartdate() != null && stay.getFinishdate() != null) {
			if(stay.getStartdate().isBefore(LocalDate.now())) {
				result.rejectValue("startdate", "dateStartDateIsPast",
						"The start date must be present or future");
			}
			if (stay.getFinishdate().isBefore(stay.getStartdate())) {
				result.rejectValue("finishdate", "dateStartDateAfterDateFinishDate", "The finish date must be after than start date");
			}
			
			if (Validaciones.validacionReserva(stay, stays)) {
				result.rejectValue("finishdate", "duplicatedStay", "There is already a current booking for this pet");
			}
		}
		if (result.hasErrors()) {
			return "pets/createOrUpdateStayForm";
		} else {
			pet.addStay(stay);
			this.petService.saveStay(stay);
			return "redirect:/owners/{ownerId}";
		}
	}
	
	@GetMapping(value = "/owners/*/pets/{petId}/stays/{stayId}/edit")
	public String initEditStayForm(Pet pet, Map<String, Object> model, @PathVariable("stayId") int stayId) {
		Stay stay = petService.findStayById(stayId);
		model.put("stay", stay);
		return "pets/createOrUpdateStayForm";
	}

	
	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/stays/{stayId}/edit")
	public String processEditStayForm(@Valid Stay stay, BindingResult result, Pet pet, Map<String, Object> model, @PathVariable("stayId") int stayId) {
		Collection<Stay> stays = this.petService.findStaysByPetId(pet.getId());
		if (stay.getStartdate() != null && stay.getFinishdate() != null) {
			if (stay.getFinishdate().isBefore(stay.getStartdate())) {
				result.rejectValue("finishdate", "dateStartDateAfterDateFinishDate", "The finish date must be after than start date");
			}
			Stay s = this.petService.findStayById(stayId);
			if(!s.getStartdate().equals(stay.getStartdate()) || !s.getFinishdate().equals(stay.getFinishdate())) {
				if (Validaciones.validacionReserva(stay, stays)) {
				result.rejectValue("finishdate", "duplicatedStay", "There is already a current booking for this pet");
				}
			}
		}
		if (result.hasErrors()) {
			return "pets/createOrUpdateStayForm";
		} else {
			stay.setId(stayId);
			stay.setPet(pet);
			this.petService.saveStay(stay);
			return "redirect:/owners/{ownerId}";
		}
	}
	
	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/stays/{stayId}/end")
	public String initEndStayForm(@PathVariable("stayId") int stayId) {
		Stay stay = petService.findStayById(stayId);
		stay.setFinishdate(LocalDate.now());
		this.petService.saveStay(stay);
		return "redirect:/owners/{ownerId}";
	}

	@GetMapping(value = "/owners/*/pets/{petId}/stays")
	public String showStays(Pet pet, Map<String, Object> model) {
		model.put("stays", pet.getStays());
		return "stayList";
	}

}

package org.springframework.samples.petclinic.web;


import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Hospitalisation;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HospitalisationController {
	
	private PetService petService;
	
	@Autowired
	public HospitalisationController(PetService petService) {
		this.petService = petService;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@GetMapping(value = "/owners/*/pets/{petId}/hospitalisations/new")
	public String initNewHospitalisationForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		Pet pet = this.petService.findPetById(petId);
		model.put("pet", pet);
		model.put("hospitalisation", new Hospitalisation());
		return "pets/createOrUpdateHospitalisationForm";
	}
	
	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/hospitalisations/new")
	public String processNewHospitalisationForm(@Valid Hospitalisation hospitalisation, BindingResult result, @PathVariable("petId") int petId, Map<String, Object> model) {
		if(hospitalisation.getFinishDate() != null && hospitalisation.getStartDate() != null) {
			if(hospitalisation.getFinishDate().isBefore(hospitalisation.getStartDate())) {
				result.rejectValue("finishDate", "dateStartDateAfterDateFinishDate",
						"The finish date can not be before than start date");
			}
		}
		Pet pet = this.petService.findPetById(petId);
		if (result.hasErrors()) {
			model.put("pet", pet);
			return "pets/createOrUpdateHospitalisationForm";
		}
		else {
			hospitalisation.setPet(pet);
			this.petService.saveHospitalisation(hospitalisation);
			pet.addHospitalisation(hospitalisation);
			return "redirect:/owners/{ownerId}";
		}
	}
	
	@GetMapping(value = "/owners/*/pets/{petId}/hospitalisations")
	public String showHospitalisations(@PathVariable int petId, Map<String, Object> model) {
		model.put("hospitalisations", this.petService.findPetById(petId).getHospitalisations());
		return "hospitalisationList";
	}
	
}

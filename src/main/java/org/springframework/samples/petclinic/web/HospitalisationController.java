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
	
	@ModelAttribute("hospitalisation")
	public Hospitalisation loadPetWithHospitalisation(@PathVariable("petId") int petId) {
		Pet pet = this.petService.findPetById(petId);
		Hospitalisation hospitalisation = new Hospitalisation();
		pet.addHospitalisation(hospitalisation);
		return hospitalisation;
	}
	
	@GetMapping(value = "/owners/*/pets/{petId}/hospitalisations/new")
	public String initNewHospitalisationForm(@PathVariable("petId") int petId, Map<String, Object> model) {
		return "pets/createOrUpdateHospitalisationForm";
	}
	
	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/hospitalisations/new")
	public String processNewHospitalisationForm(@Valid Hospitalisation hospitalisation, BindingResult result) {
		if(hospitalisation.getFinishDate().isBefore(hospitalisation.getStartDate())) {
			result.rejectValue("finishDate", "dateStartDateAfterDateFinishDate",
					"The finish date can not be before than start date");
		}
		if (result.hasErrors()) {
			return "pets/createOrUpdateHospitalisationForm";
		}
		else {
			this.petService.saveHospitalisation(hospitalisation);
			return "redirect:/owners/{ownerId}";
		}
	}
	
	@GetMapping(value = "/owners/*/pets/{petId}/hospitalisations")
	public String showHospitalisations(@PathVariable int petId, Map<String, Object> model) {
		model.put("hospitalisations", this.petService.findPetById(petId).getHospitalisations());
		return "hospitalisationList";
	}
	
}

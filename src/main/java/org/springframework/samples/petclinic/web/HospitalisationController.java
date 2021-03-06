package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Hospitalisation;
import org.springframework.samples.petclinic.model.HospitalisationStatus;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
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

	@ModelAttribute("hospitalisation_status")
	public Collection<HospitalisationStatus> populateHospitalisationStatus() {
		return this.petService.findHospitalisationStatus();
	}

	@ModelAttribute("pet")
	public Pet loadPetWithVisit(@PathVariable("petId") int petId) {
		Pet pet = this.petService.findPetById(petId);
		return pet;
	}

	@GetMapping(value = "/owners/*/pets/{petId}/hospitalisations/new")
	public String initNewHospitalisationForm(Pet pet, Map<String, Object> model) {
		if (pet.getStatus().getName().equals("HEALTHY")) {
			model.put("hospitalisation", new Hospitalisation());
			return "pets/createOrUpdateHospitalisationForm";
		} else {
			return "/exception";
		}
	}

	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/hospitalisations/new")
	public String processNewHospitalisationForm(@Valid Hospitalisation hospitalisation, BindingResult result, Pet pet) {
		if (result.hasErrors()) {
			return "pets/createOrUpdateHospitalisationForm";
		} else {
			pet.setStatus(petService.findPetStatus().stream().filter(s -> s.getName().equals("SICK")).findFirst().get());
			pet.addHospitalisation(hospitalisation);
			this.petService.saveHospitalisation(hospitalisation);
			return "redirect:/owners/{ownerId}";
		}
	}

	@GetMapping(value = "/owners/*/pets/{petId}/hospitalisations/{hospitalisationId}/edit")
	public String initEditHospitalisationForm(Pet pet, Map<String, Object> model,
			@PathVariable("hospitalisationId") int hospitalisationId) {
		Hospitalisation hospitalisation = petService.findHospitalisationById(hospitalisationId);
		model.put("hospitalisation", hospitalisation);
		return "pets/createOrUpdateHospitalisationForm";
	}

	@PostMapping(value = "/owners/{ownerId}/pets/{petId}/hospitalisations/{hospitalisationId}/edit")
	public String processEditHospitalisationForm(@Valid Hospitalisation hospitalisation, BindingResult result, Pet pet,
			Map<String, Object> model, @PathVariable("hospitalisationId") int hospitalisationId)
			throws DataAccessException, DuplicatedPetNameException {
		if (result.hasErrors()) {
			return "pets/createOrUpdateHospitalisationForm";
		} else {
			hospitalisation.setId(hospitalisationId);
			if (hospitalisation.getHospitalisationStatus().getName().equals("DISCHARGED")) {
				hospitalisation.getHospitalisationStatus().setId(2);
				hospitalisation.setFinishDate(LocalDate.now());
				pet.setStatus(this.petService.findPetStatus().stream().filter(s -> s.getName().equals("HEALTHY"))
						.findFirst().get());
			}
			hospitalisation.setPet(pet);
			this.petService.saveHospitalisation(hospitalisation);
			return "redirect:/owners/{ownerId}";
		}
	}

	@GetMapping(value = "/owners/{ownerId}/pets/{petId}/hospitalisations/{hospitalisationId}/delete")
	public String initDeleteForm(Pet pet, @PathVariable("hospitalisationId") int hospitalisationId) {
		Hospitalisation hosp = this.petService.findHospitalisationById(hospitalisationId);

		if (hosp.getHospitalisationStatus().getName().equals("DISCHARGED")) {
			pet.deleteHospitalisation(hosp);
			this.petService.deleteHospitalisation(hosp);
			return "redirect:/owners/{ownerId}";
		} else {
			return "/exception";
		}
	}

}

package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class VetController {

	private final VetService	vetService;

	private static final String	VIEWS_VETS_CREATE_OR_UPDATE_FORM	= "vets/createOrUpdateVetForm";
	private static final String	REDIRECT_VETS						= "redirect:/vets";


	@Autowired
	public VetController(final VetService vetService) {
		this.vetService = vetService;
	}

	@ModelAttribute("listSpecialties")
	public Collection<Specialty> populateSpecialties() {
		return this.vetService.findSpecialties();
	}

	@GetMapping(value = {
		"/vets"
	})
	public String showVetList(final Map<String, Object> model) {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for Object-Xml mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		model.put("vets", vets);
		return "vets/vetList";
	}

	@GetMapping(value = {
		"/vets.xml"
	})
	public @ResponseBody Vets showResourcesVetList() {
		// Here we are returning an object of type 'Vets' rather than a collection of Vet
		// objects
		// so it is simpler for JSon/Object mapping
		Vets vets = new Vets();
		vets.getVetList().addAll(this.vetService.findVets());
		return vets;
	}

	@GetMapping(value = "/vets/new")
	public String initCreationForm(final ModelMap model) {
		Vet vet = new Vet();
		model.put("vet", vet);
		model.addAttribute("specialties", new HashSet<>());
		return VetController.VIEWS_VETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/vets/new")
	public String processCreationForm(@Valid final Vet vet, @RequestParam(name = "listaSpe", required = false) final String specialties, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("vet", vet);
			return VetController.VIEWS_VETS_CREATE_OR_UPDATE_FORM;
		} else {
			if (specialties != null) {
				String[] speNames = specialties.split(",");
				Set<Specialty> selectedSpe = new HashSet<>();
				for (String s : speNames) {
					selectedSpe.add((Specialty) this.populateSpecialties().stream().filter(x -> x.getName().equals(s)).toArray()[0]);
				}
				for (Specialty spe : selectedSpe) {
					vet.addSpecialty(spe);
				}
			}
			this.vetService.saveVet(vet);
			return VetController.REDIRECT_VETS;
		}
	}

	@GetMapping(value = "/vets/{vetId}/edit")
	public String initUpdateForm(@PathVariable("vetId") final int vetId, final ModelMap model) {
		Vet vet = this.vetService.findVetById(vetId);
		model.put("vet", vet);
		return VetController.VIEWS_VETS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/vets/{vetId}/edit")
	public String processUpdateForm(@Valid final Vet vet, @RequestParam(name = "listaSpe", required = false) final String specialties, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("vet", vet);
			return VetController.VIEWS_VETS_CREATE_OR_UPDATE_FORM;
		} else {
			if (specialties != null) {
				String[] speNames = specialties.split(",");
				Set<Specialty> selectedSpe = new HashSet<>();
				for (String s : speNames) {
					selectedSpe.add((Specialty) this.populateSpecialties().stream().filter(x -> x.getName().equals(s)).toArray()[0]);
				}
				for (Specialty spe : selectedSpe) {
					vet.addSpecialty(spe);
				}
			}
			this.vetService.saveVet(vet);
			return VetController.REDIRECT_VETS;
		}
	}

}
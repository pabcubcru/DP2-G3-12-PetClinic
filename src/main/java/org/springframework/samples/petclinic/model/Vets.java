package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Simple domain object representing a list of veterinarians. Mostly here to be used for
 * the 'vets' {@link org.springframework.web.servlet.view.xml.MarshallingView}.
 *
 * @author Arjen Poutsma
 */
@XmlRootElement
public class Vets {

	private List<Vet> vet;

	@XmlElement
	public List<Vet> getVetList() {
		if (vet == null) {
			vet = new ArrayList<>();
		}
		return vet;
	}

}
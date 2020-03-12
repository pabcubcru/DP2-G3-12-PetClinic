package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "hospitalisations")
public class Hospitalisation extends NamedEntity{
	
	@Column(name = "start_date")   
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate startDate;
	
	@Column(name = "finish_date")   
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate finishDate;
	
	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet pet;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Vet> vets;
	
	@NotNull
	@Column(name = "hospitalisation_status")
	private HospitalisationStatus hospitalisationStatus;
	
	@NotBlank
	@Column(name = "treatment")
	private String treatment;
	
	@NotBlank
	@Column(name = "diagnosis")
	private String diagnosis;
	
	@NotNull
	@Column(name = "total_price")
	private Double totalPrice;
	
	public Pet getPet() {
		return this.pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}
	
	public Hospitalisation() {
		this.startDate = LocalDate.now();
		this.hospitalisationStatus = HospitalisationStatus.HOSPITALISED;
	}
	
}

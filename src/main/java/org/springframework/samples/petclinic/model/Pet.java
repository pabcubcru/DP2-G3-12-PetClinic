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
package org.springframework.samples.petclinic.model;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Simple business object representing a pet.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
@Entity
@Table(name = "pets")
public class Pet extends NamedEntity {

	@Column(name = "birth_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate birthDate;

	@ManyToOne
	@JoinColumn(name = "type_id")
	private PetType type;

	@ManyToOne
	@JoinColumn(name = "pet_status_id")
	private PetStatus status;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private Owner owner;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.LAZY)
	private Set<Visit> visits;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.LAZY)
	private Set<Hospitalisation> hospitalisations;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "pet", fetch = FetchType.LAZY)
	private Set<Stay> stays;
	
	public Pet() {
		PetStatus status = new PetStatus();
		status.setName("HEALTHY");
		status.setId(1);
		this.status = status;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public LocalDate getBirthDate() {
		return this.birthDate;
	}

	public PetType getType() {
		return this.type;
	}

	public void setType(PetType type) {
		this.type = type;
	}

	public Owner getOwner() {
		return this.owner;
	}

	protected void setOwner(Owner owner) {
		this.owner = owner;
	}

	public PetStatus getStatus() {
		return status;
	}

	public void setStatus(PetStatus status) {
		this.status = status;
	}

	protected Set<Visit> getVisitsInternal() {
		if (this.visits == null) {
			this.visits = new HashSet<>();
		}
		return this.visits;
	}

	protected void setVisitsInternal(Set<Visit> visits) {
		this.visits = visits;
	}

	public List<Visit> getVisits() {
		List<Visit> sortedVisits = new ArrayList<>(getVisitsInternal());
		PropertyComparator.sort(sortedVisits, new MutableSortDefinition("date", false, false));
		return Collections.unmodifiableList(sortedVisits);
	}

	public void addVisit(Visit visit) {
		getVisitsInternal().add(visit);
		visit.setPet(this);
	}
	
	public void deleteVisit(Visit visit) {
		getVisitsInternal().remove(visit);
	}

	protected Set<Hospitalisation> getHospitalisationsInternal() {
		if (this.hospitalisations == null) {
			this.hospitalisations = new HashSet<>();
		}
		return this.hospitalisations;
	}

	protected void setHospitalisationsInternal(Set<Hospitalisation> hospitalisations) {
		this.hospitalisations = hospitalisations;
	}

	public List<Hospitalisation> getHospitalisations() {
		List<Hospitalisation> sortedHospitalisations = new ArrayList<>(getHospitalisationsInternal());
		return Collections.unmodifiableList(sortedHospitalisations);
	}

	public void addHospitalisation(Hospitalisation hospitalisation) {
		getHospitalisationsInternal().add(hospitalisation);
		hospitalisation.setPet(this);
	}

	public void deleteHospitalisation(Hospitalisation hospitalisation) {
		getHospitalisationsInternal().remove(hospitalisation);
	}

	protected Set<Stay> getStaysInternal() {
		if (this.stays == null) {
			this.stays = new HashSet<>();
		}
		return this.stays;
	}

	protected void setStaysInternal(Set<Stay> stays) {
		this.stays = stays;
	}

	public List<Stay> getStays() {
		List<Stay> sortedStays = new ArrayList<>(getStaysInternal());
		return Collections.unmodifiableList(sortedStays);
	}

	public void addStay(Stay stay) {
		getStaysInternal().add(stay);
		stay.setPet(this);
	}
	
	@Transactional
	public void deleteStay(Stay stay) {
		getStaysInternal().remove(stay);
	}

}


package org.springframework.samples.petclinic.model;

import java.beans.Transient;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "stays")
public class Stay extends BaseEntity {

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "pet_id")
	private Pet			pet;

	@Column(name = "start_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	private LocalDate	startdate;

	@Column(name = "finish_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	@FutureOrPresent
	private LocalDate	finishdate;

	@NotNull
	@Range(min = 0)
	@Column(name = "price")
	private Double		price;

	@NotBlank
	@Column(name = "special_cares")
	private String		specialCares;


	public Stay() {
		this.startdate = LocalDate.now();
	}
	
	public Pet getPet() {
		return this.pet;
	}

	public void setPet(final Pet pet) {
		this.pet = pet;
	}

	public LocalDate getStartdate() {
		return this.startdate;
	}

	public void setStartdate(final LocalDate startdate) {
		this.startdate = startdate;
	}

	public LocalDate getFinishdate() {
		return this.finishdate;
	}

	public void setFinishdate(final LocalDate finishdate) {
		this.finishdate = finishdate;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(final Double price) {
		this.price = price;
	}

	public String getSpecialCares() {
		return this.specialCares;
	}

	public void setSpecialCares(final String specialCares) {
		this.specialCares = specialCares;
	}
	
	@Transient
	public Boolean activeStay() {
		return !this.startdate.isAfter(LocalDate.now()) && this.finishdate.isAfter(LocalDate.now());
	}
	
	@Transient
	public Boolean pastStay() {
		return !this.finishdate.isAfter(LocalDate.now());
	}

}

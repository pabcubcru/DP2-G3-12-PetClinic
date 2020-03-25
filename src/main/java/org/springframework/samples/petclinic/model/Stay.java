package org.springframework.samples.petclinic.model;

import java.time.LocalDate;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "stays")
public class Stay extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet pet;

	@Column(name = "start_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	@FutureOrPresent
	private LocalDate startdate;

	@Column(name = "finish_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	@FutureOrPresent
	private LocalDate finishdate;

	@NotNull
	@Column(name = "price")
	private Double price;

	@NotBlank
	@Column(name = "special_cares")
	private String specialCares;

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

	public LocalDate getStartdate() {
		return startdate;
	}

	public void setStartdate(LocalDate startdate) {
		this.startdate = startdate;
	}

	public LocalDate getFinishdate() {
		return finishdate;
	}

	public void setFinishdate(LocalDate finishdate) {
		this.finishdate = finishdate;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getSpecialCares() {
		return specialCares;
	}

	public void setSpecialCares(String specialCares) {
		this.specialCares = specialCares;
	}

}

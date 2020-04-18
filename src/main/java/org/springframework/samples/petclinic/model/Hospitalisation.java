
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "hospitalisations")
public class Hospitalisation extends BaseEntity {

	@Column(name = "start_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	@FutureOrPresent
	private LocalDate	startDate;

	@Column(name = "finish_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	@FutureOrPresent
	private LocalDate	finishDate;

	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet			pet;

	@ManyToOne
	@JoinColumn(name = "hospitalisation_status")
	private HospitalisationStatus hospitalisationStatus;

	@NotEmpty
	@Column(name = "treatment")
	private String		treatment;

	@NotEmpty
	@Column(name = "diagnosis")
	private String		diagnosis;

	@NotNull
	@Range(min = 0)
	@Column(name = "total_price")
	private Double		totalPrice;


	public Pet getPet() {
		return this.pet;
	}

	public void setPet(final Pet pet) {
		this.pet = pet;
	}

	public LocalDate getStartDate() {
		return this.startDate;
	}

	public void setStartDate(final LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getFinishDate() {
		return this.finishDate;
	}

	public void setFinishDate(final LocalDate finishDate) {
		this.finishDate = finishDate;
	}

	public HospitalisationStatus getHospitalisationStatus() {
		return hospitalisationStatus;
	}

	public void setHospitalisationStatus(HospitalisationStatus hospitalisationStatus) {
		this.hospitalisationStatus = hospitalisationStatus;
	}

	public String getTreatment() {
		return this.treatment;
	}

	public void setTreatment(final String treatment) {
		this.treatment = treatment;
	}

	public String getDiagnosis() {
		return this.diagnosis;
	}

	public void setDiagnosis(final String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public Double getTotalPrice() {
		return this.totalPrice;
	}

	public void setTotalPrice(final Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Hospitalisation() {
		this.startDate = LocalDate.now();
	}

}

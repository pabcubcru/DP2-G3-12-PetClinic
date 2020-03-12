package org.springframework.samples.petclinic.model;

import java.time.LocalDate;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "discounts")
public class Discount extends NamedEntity{

	@NotNull
	@Column(name = "percentage")
	private Double percentage;
	
	@Column(name = "start_date")     
	@FutureOrPresent
	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate startDate;
	
	@Column(name = "finish_date")    
	@FutureOrPresent
	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate finishDate;
	
	
}

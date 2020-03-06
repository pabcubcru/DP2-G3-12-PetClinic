package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@Table(name = "products")
public class Product extends NamedEntity {

	@NotEmpty
	@Column(name = "price")
	private Double price;
	
	@NotEmpty
	@Column(name = "stock")
	private int stock;
	
	@OneToOne(optional = true)
	@JoinColumn(name = "discount_id")
	private Discount discount;
}

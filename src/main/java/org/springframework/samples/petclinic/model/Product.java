
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@Table(name = "products")
public class Product extends BaseEntity {
	
	@NotBlank
	@Column(name = "name", unique = true)
	private String name;
	
	@NotNull
	@Column(name = "price")
	private Double		price;

	@NotNull
	@Column(name = "stock")
	private int			stock;

	@OneToOne(optional = true)
	@Fetch(value = FetchMode.SELECT)
	@JoinColumn(name = "discount_id")
	private Discount	discount;

	@ManyToOne(optional = false)
	@JoinColumn(name = "shop_id")
	private Shop		shop;


	public double getPriceWithDiscount() {
		double res = this.getPrice();
		if (this.discount != null) {
			res -= this.price * this.discount.getPercentage() / 100;
		}
		return res;
	}
	
}

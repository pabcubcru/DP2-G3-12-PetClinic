
package org.springframework.samples.petclinic.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product extends BaseEntity {

	@NotBlank
	@Column(name = "name", unique = true)
	private String		name;

	@NotNull
	@Range(min = 0)
	@Column(name = "price")
	private Double		price;

	@NotNull
	@Range(min = 0)
	@Column(name = "stock")
	private int			stock;

	@OneToOne(optional = true)
	@JoinColumn(name = "discount_id")
	private Discount	discount;

	@ManyToOne(optional = false, fetch = FetchType.EAGER)
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

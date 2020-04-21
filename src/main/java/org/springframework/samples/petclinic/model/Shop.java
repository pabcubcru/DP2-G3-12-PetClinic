
package org.springframework.samples.petclinic.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@NotNull
@Table(name = "shops")
public class Shop extends NamedEntity {

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "shop", fetch = FetchType.EAGER)
	private Set<Product>	products;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "shop", fetch = FetchType.EAGER)
	private Set<Order>		orders;


	public Set<Product> getProductsInternal() {
		if (this.products == null) {
			this.products = new HashSet<>();
		}
		return this.products;
	}

	protected void setProductsInternal(final Set<Product> products) {
		this.products = products;
	}

	public int getNumberOfProducts() {
		return this.getProductsInternal().size();
	}

	public void addProduct(final Product product) {
		this.getProductsInternal().add(product);
		product.setShop(this);
	}

	public void deleteProduct(final Product product) {
		this.getProductsInternal().remove(product);
	}

	public Set<Order> getOrdersInternal() {
		if (this.orders == null) {
			this.orders = new HashSet<>();
		}
		return this.orders;
	}

	protected void setOrdersInternal(final Set<Order> orders) {
		this.orders = orders;
	}

	public int getNumberOfOrders() {
		return this.getOrdersInternal().size();
	}

	public void addOrder(final Order order) {
		this.getOrdersInternal().add(order);
		order.setShop(this);
	}
}

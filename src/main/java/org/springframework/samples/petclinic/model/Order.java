
package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "orders")
public class Order extends NamedEntity {

	@Column(name = "supplier")
	@NotBlank
	private String			supplier;

	@Column(name = "product_number")
	@NotNull
	@Range(min = 1)
	private int				productNumber;

	@Column(name = "order_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:SS")
	private LocalDateTime	orderDate;

	@NotNull
	@Column(name = "order_status")
	private OrderStatus		orderStatus;

	@ManyToOne(optional = false)
	@JoinColumn(name = "shop_id")
	private Shop			shop;

	@ManyToOne(optional = false, fetch=FetchType.EAGER)
	@JoinColumn(name = "product_id")
	private Product			product;


	public Order() {
		this.orderDate = LocalDateTime.now();
		this.orderStatus = OrderStatus.INPROCESS;
	}

	public void orderReceived() {
		this.setOrderStatus(OrderStatus.RECEIVED);
	}

	public void orderCanceled() {
		this.setOrderStatus(OrderStatus.CANCELED);
	}
}

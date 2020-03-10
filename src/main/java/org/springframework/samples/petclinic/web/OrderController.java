package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.model.OrderStatus;
import org.springframework.samples.petclinic.model.Shop;
import org.springframework.samples.petclinic.service.OrderService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ShopService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/shops/{shopId}")
public class OrderController {

	private final OrderService orderService;
	
	private final ProductService productService;
	private final ShopService shopService;

	@Autowired
	public OrderController(OrderService orderService, ProductService productService, ShopService shopService) {
		this.orderService = orderService;
		this.productService = productService;
		this.shopService = shopService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/orders/new")
	public String initNewOrderForm(Map<String, Object> model) {
		model.put("products", productService.findProducts());
		model.put("shops", shopService.findShops());
		model.put("productsSize", productService.findProducts().spliterator().estimateSize());
		model.put("shopsSize", shopService.findShops().spliterator().estimateSize());
		Order order = new Order();
		model.put("order", order);
		return "orders/createOrUpdateOrderForm";
	}

	@PostMapping(value = "/orders/new")
	public String processNewOrderForm(@Valid Order order, BindingResult result, @PathVariable("shopId") int shopId) {
		if (result.hasErrors()) {
			return "orders/createOrUpdateOrderForm";
		}
		else {
			Shop shop = this.shopService.findShops().iterator().next();
			order.setShop(shop);
			this.orderService.saveOrder(order);
			shop.addOrder(order);
			return "redirect:/shops/" + shopId + "/orders/" + order.getId();
		}
	}
	
	@GetMapping(value = "/orders/{orderId}/received")
	public String processOrderReceived(@PathVariable("orderId") int orderId, @PathVariable("shopId") int shopId) {
			Order order = this.orderService.findOrderById(orderId);
			if(order.getOrderStatus().equals(OrderStatus.INPROCESS)) {
				order.orderReceived();
				this.orderService.saveOrder(order);
				return "redirect:/shops/" + shopId + "/orders/" + order.getId();
			} else {
				return "/exception";
			}
	}

	@GetMapping("/orders/{orderId}")
	public ModelAndView showOrder(@PathVariable("orderId") int orderId) {
		ModelAndView mav = new ModelAndView("orders/orderDetails");
		Order order = this.orderService.findOrderById(orderId);
		mav.addObject(order);
		return mav;
	}
}
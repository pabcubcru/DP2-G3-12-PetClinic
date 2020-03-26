package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Order;
import org.springframework.samples.petclinic.model.OrderStatus;
import org.springframework.samples.petclinic.model.Product;
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
		model.put("products", productService.findProductsNames());
		model.put("productsSize", productService.findProductsNames().size());
		model.put("productName", "");
		Order order = new Order();
		model.put("order", order);
		return "orders/createOrUpdateOrderForm";
	}

	@PostMapping(value = "/orders/new")
	public String processNewOrderForm(@Valid Order order, BindingResult result, @PathVariable("shopId") int shopId, Map<String, Object> model) {
		if (result.hasErrors()) {
			model.put("products", productService.findProductsNames());
			model.put("productsSize", productService.findProductsNames().size());
			model.put("productName", "");
			model.put("order", order);
			return "orders/createOrUpdateOrderForm";
		}
		else {
			Shop shop = this.shopService.findShops().iterator().next();
			Product product = productService.findByName(order.getProduct().getName());
			order.setProduct(product);
			shop.addOrder(order);
			this.orderService.saveOrder(order);
			return "redirect:/shops/" + shopId;
		}
	}
	
	@GetMapping(value = "/orders/{orderId}/received")
	public String processOrderReceived(@PathVariable("orderId") int orderId, @PathVariable("shopId") int shopId) {
			Order order = this.orderService.findOrderById(orderId);
			if(order.getOrderStatus().equals(OrderStatus.INPROCESS)) {
				order.orderReceived();
				this.orderService.saveOrder(order);
				return "redirect:/shops/" + shopId + "/orders/" + orderId;
			} else {
				return "/exception";
			}
	}
	
	@GetMapping(value = "/orders/{orderId}/canceled")
	public String processOrderCanceled(@PathVariable("orderId") int orderId, @PathVariable("shopId") int shopId) {
			Order order = this.orderService.findOrderById(orderId);
			if(order.getOrderStatus().equals(OrderStatus.INPROCESS) && order.getOrderDate().isAfter(LocalDateTime.now().minusDays(2))) {
				order.orderCanceled();
				this.orderService.saveOrder(order);
				return "redirect:/shops/" + shopId + "/orders/" + orderId;
			} else {
				return "/exception";
			}
	}

	@GetMapping("/orders/{orderId}")
	public ModelAndView showOrder(@PathVariable("orderId") int orderId) {
		ModelAndView mav = new ModelAndView("orders/orderDetails");
		Order order = this.orderService.findOrderById(orderId);
		mav.addObject(order);
		mav.addObject("canBeCanceled", order.getOrderDate().isAfter(LocalDateTime.now().minusDays(2)));
		return mav;
	}
}

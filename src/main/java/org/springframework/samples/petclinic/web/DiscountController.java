package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Discount;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.service.DiscountService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shops/{shopId}/products/{productId}")
public class DiscountController {

	private DiscountService discountService;
	private ProductService productService;

	@Autowired
	public DiscountController(DiscountService discountService, ProductService productService) {
		this.discountService = discountService;
		this.productService = productService;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/discounts/new")
	public String initNewDiscountForm(Map<String, Object> model, @PathVariable("productId") int productId) {
		Discount discount = new Discount();
		Product product = productService.findProductById(productId);
		model.put("discount", discount);
		model.put("product", product);
		return "discounts/createOrUpdateDiscountForm";
	}

	@PostMapping(value = "/discounts/new")
	public String processNewDiscountForm(@Valid Discount discount, BindingResult result,
			@PathVariable("productId") int productId, @PathVariable("shopId") int shopId) {
		if (discount.getFinishDate().isBefore(discount.getStartDate())) {
			result.rejectValue("finishDate", "wrongDate", "Finish date must be after than start date");
		}
		if (result.hasErrors()) {
			return "discounts/createOrUpdateDiscountForm";
		} else {
			Product product = productService.findProductById(productId);
			product.setDiscount(discount);
			discountService.saveDiscount(discount);
			return "redirect:/shops/" + shopId + "/products/" + productId;
		}
	}

	@GetMapping(value = "/discounts/{discountId}/edit")
	public String initEditDiscountForm(Map<String, Object> model, @PathVariable("productId") int productId,
			@PathVariable("discountId") int discountId) {
		Discount discount = discountService.findDiscountById(discountId).get();
		Product product = productService.findProductById(productId);
		model.put("discount", discount);
		model.put("product", product);
		return "discounts/createOrUpdateDiscountForm";
	}

	@PostMapping(value = "/discounts/{discountId}/edit")
	public String processEditDiscountForm(@Valid Discount discount, BindingResult result,
			@PathVariable("productId") int productId, @PathVariable("shopId") int shopId,
			@PathVariable("discountId") int discountId, Map<String, Object> model) {
		if (discount.getFinishDate() != null && discount.getStartDate() != null) {
			if (discount.getFinishDate().isBefore(discount.getStartDate())) {
				result.rejectValue("finishDate", "wrongDate", "Finish date must be after than start date");
			}
		}
		Product product = productService.findProductById(productId);
		if (result.hasErrors()) {
			model.put("product", product);
			return "discounts/createOrUpdateDiscountForm";
		} else {
			discount.setId(discountId);
			product.setDiscount(discount);
			discountService.saveDiscount(discount);
			return "redirect:/shops/" + shopId + "/products/" + productId;
		}
	}
}

package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Discount;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.DiscountService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shops/{shopId}/products/{productId}")
public class DiscountController {

	private DiscountService discountService;
	private ProductService productService;

	@ModelAttribute("product")
	public Product loadProduct(@PathVariable("productId") int productId) {
		Product product = this.productService.findProductById(productId);
		return product;
	}

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
	public String initNewDiscountForm(Map<String, Object> model) {
		Discount discount = new Discount();
		model.put("discount", discount);
		return "discounts/createOrUpdateDiscountForm";
	}

	@PostMapping(value = "/discounts/new")
	public String processNewDiscountForm(@Valid Discount discount, BindingResult result, Product product,
			@PathVariable("shopId") int shopId) {
		if(discount.getFinishDate() != null && discount.getStartDate() != null) {
			if (discount.getFinishDate().isBefore(discount.getStartDate())) {
				result.rejectValue("finishDate", "wrongDate", "Finish date must be after than start date");
			}
		}
		if (result.hasErrors()) {
			return "discounts/createOrUpdateDiscountForm";
		} else {
			product.setDiscount(discount);
			discountService.saveDiscount(discount);
			return "redirect:/shops/" + shopId + "/products/{productId}";
		}
	}

	@GetMapping(value = "/discounts/{discountId}/edit")
	public String initEditDiscountForm(Map<String, Object> model, Product product,
			@PathVariable("discountId") int discountId) {
		Discount discount = discountService.findDiscountById(discountId);
		model.put("discount", discount);
		return "discounts/createOrUpdateDiscountForm";
	}

	@PostMapping(value = "/discounts/{discountId}/edit")
	public String processEditDiscountForm(@Valid Discount discount, BindingResult result, Product product,
			@PathVariable("shopId") int shopId, @PathVariable("discountId") int discountId) {
		if (discount.getFinishDate() != null && discount.getStartDate() != null) {
			if (discount.getFinishDate().isBefore(discount.getStartDate())) {
				result.rejectValue("finishDate", "wrongDate", "Finish date must be after than start date");
			}
		}
		if (result.hasErrors()) {
			return "discounts/createOrUpdateDiscountForm";
		} else {
			discount.setId(discountId);
			product.setDiscount(discount);
			discountService.saveDiscount(discount);
			return "redirect:/shops/" + shopId + "/products/{productId}";
		}
	}
	
	@GetMapping("/discounts/{discountId}/delete")
	public String processDeleteDiscount(@PathVariable("discountId") int discountId, Product product, @PathVariable("shopId") int shopId) {
		Discount discount = discountService.findDiscountById(discountId);
		if(product.getDiscount().equals(discount)) {
			product.setDiscount(null);
			productService.saveProduct(product);
			discountService.deleteDiscount(discount);
			return "redirect:/shops/" + shopId + "/products/{productId}";
		} else {
			return "/exception";
		}
	}
}

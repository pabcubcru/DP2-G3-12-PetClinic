
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import java.util.Map;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.model.Shop;
import org.springframework.samples.petclinic.service.DiscountService;
import org.springframework.samples.petclinic.service.OrderService;
import org.springframework.samples.petclinic.service.ProductService;
import org.springframework.samples.petclinic.service.ShopService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/shops/1")
public class ProductController {

	private ProductService productService;
	private ShopService shopService;
	private OrderService orderService;
	private DiscountService discountService;

	@Autowired
	public ProductController(final ProductService productService, final ShopService shopService,
			final OrderService orderService, final DiscountService discountService) {
		this.productService = productService;
		this.shopService = shopService;
		this.orderService = orderService;
		this.discountService = discountService;
	}

	@ModelAttribute("shop")
	public Shop loadShop() {
		return this.shopService.findShops().iterator().next();
	}

	@InitBinder("shop")
	public void initOwnerBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/products/new")
	public String initNewProductForm(final Map<String, Object> model) {
		Product product = new Product();
		model.put("product", product);
		return "products/createOrUpdateProductForm";
	}

	@PostMapping(value = "/products/new")
	public String processNewProductForm(@Valid Product product, final BindingResult result, final Shop shop) {
		if (productService.findProductsNames().contains(product.getName())) {
			result.rejectValue("name", "duplicatedName", "This name already exist");
		}
		if (result.hasErrors()) {
			return "products/createOrUpdateProductForm";
		} else {
			shop.addProduct(product);
			this.productService.saveProduct(product);
			return "redirect:/shops/" + shop.getId();
		}
	}

	@GetMapping(value = "/products/{productId}/delete")
	public String deleteProduct(final Map<String, Object> model, @PathVariable("productId") final int productId,
			final Shop shop) {
		Product product = this.productService.findProductById(productId);
		if (this.orderService.countOrdersByProductId(productId) == 0) {
			shop.deleteProduct(product);
			this.productService.deleteProduct(product);
			if (product.getDiscount() != null) {
				this.discountService.deleteDiscount(product.getDiscount().getId());
			}
			return "redirect:/shops/" + shop.getId();
		} else {
			return "/exception";
		}
	}

	@GetMapping("/products/{productId}")
	public ModelAndView showProduct(@PathVariable("productId") final int productId) {
		ModelAndView mav = new ModelAndView("products/productDetails");
		Product product = this.productService.findProductById(productId);
		if (product.getDiscount() != null) {
			if (product.getDiscount().getFinishDate().isAfter(LocalDate.now())
					&& product.getDiscount().getStartDate().isBefore(LocalDate.now())
					|| product.getDiscount().getStartDate().isEqual(LocalDate.now())
					|| product.getDiscount().getFinishDate().isEqual(LocalDate.now())) {
				mav.addObject("activeDiscount", true);
				mav.addObject("priceWithDiscount", product.getPriceWithDiscount());
			}
		}
		boolean noHasOrders = this.orderService.countOrdersByProductId(productId) == 0;
		mav.addObject("canDeleteIt", noHasOrders);
		mav.addObject(product);
		return mav;
	}

	@GetMapping(value = "/products/{productId}/edit")
	public String initUpdateProductForm(@PathVariable("productId") final int productId, final Map<String, Object> model) {
		Product product = this.productService.findProductById(productId);
		model.put("product", product);
		return "products/createOrUpdateProductForm";
	}

	@PostMapping(value = "/products/{productId}/edit")
	public String processUpdateProductForm(@Valid final Product product, final BindingResult result,
			@PathVariable("productId") final int productId, Shop shop) {
		Product productWithoutUpdate = this.productService.findProductById(productId);
		if (this.productService.findProductsNames().contains(product.getName())
				&& !productWithoutUpdate.getName().equals(product.getName())) {
			result.rejectValue("name", "duplicatedName", "This name already exist");
		}
		if (result.hasErrors()) {
			return "products/createOrUpdateProductForm";
		} else {
			product.setDiscount(productWithoutUpdate.getDiscount());
			product.setId(productId);
			product.setShop(shop);
			this.productService.saveProduct(product);
			return "redirect:/shops/" + shop.getId() + "/products/" + productId;
		}
	}
}

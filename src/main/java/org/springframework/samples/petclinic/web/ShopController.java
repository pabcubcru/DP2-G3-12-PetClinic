package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Shop;
import org.springframework.samples.petclinic.service.ShopService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/shops")
public class ShopController {
	
	private ShopService shopService;
	
	@Autowired
	public ShopController(ShopService shopService) {
		this.shopService = shopService;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@GetMapping(value = "/new")
	public String initNewProductForm(Map<String, Object> model) {
		Shop shop = new Shop();
		model.put("shop", shop);
		return "shops/createOrUpdateShopForm";
	}
	
	@PostMapping(value = "/new")
	public String processNewProductForm(@Valid Shop shop, BindingResult result) {
		if (result.hasErrors()) {
			return "shops/createOrUpdateShopForm";
		}
		else {
			this.shopService.saveShop(shop);
			return "redirect:/shops/1";
		}
	}
	
	@GetMapping("/*")
	public ModelAndView showShop() {
		ModelAndView mav = new ModelAndView("shops/shopDetails");
		Shop shop = shopService.findShops().iterator().next();
		mav.addObject("shop", shop);
		mav.addObject("products", shop.getProductsInternal());
		mav.addObject("orders", shop.getOrdersInternal());
		return mav;
	}

}

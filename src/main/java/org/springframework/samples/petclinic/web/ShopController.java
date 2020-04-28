
package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Shop;
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
@RequestMapping("/shops")
public class ShopController {

	private ShopService shopService;


	@Autowired
	public ShopController(final ShopService shopService) {
		this.shopService = shopService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping("/1")
	public ModelAndView showShop() {
		ModelAndView mav = new ModelAndView("shops/shopDetails");
		Shop shop = this.shopService.findShopById(1);
		mav.addObject("shop", shop);
		mav.addObject("products", shop.getProductsInternal());
		mav.addObject("orders", shop.getOrdersInternal());
		return mav;
	}

	@GetMapping(value = "/{shopId}/edit")
	public String initUpdateShopForm(@PathVariable("shopId") final int shopId, final Map<String, Object> model) {
		Shop shop = this.shopService.findShopById(shopId);
		model.put("shop", shop);
		return "shops/createOrUpdateShopForm";
	}

	@PostMapping(value = "/{shopId}/edit")
	public String processUpdateShopForm(@Valid final Shop shop, final BindingResult result) {
		if (result.hasErrors()) {
			return "shops/createOrUpdateShopForm";
		} else {
			Shop shopToUpdate = shopService.findShopById(1);
			BeanUtils.copyProperties(shop, shopToUpdate, "id", "products", "orders");
			this.shopService.saveShop(shop);
			return "redirect:/shops/1";
		}
	}

}

package org.springframework.samples.petclinic.web.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.Shop;
import org.springframework.samples.petclinic.service.ShopService;
import org.springframework.samples.petclinic.web.ShopController;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.servlet.ModelAndView;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShopControllerIntegrationTest {
	
	@Autowired
	private ShopController shopController;
	
	@Autowired
	private ShopService shopService;
	

	@Test
	void testInitCreationShopForm() throws Exception {
		ModelMap model = new ModelMap();
		
		String view = shopController.initNewShopForm(model);
		assertEquals(view, "shops/createOrUpdateShopForm");
	}
	
	
	@Test
	void testProcessCreationShopFormSuccess() throws Exception {
		Shop shop = shopService.findShopById(1);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		
		String view = shopController.processNewShopForm(shop, result);
		
		assertEquals(view, "redirect:/shops/1");
	}
	
	@Test
	void testProcessCreationShopFormHasErrors() throws Exception {
		Shop shop = new Shop();
		shop.setName(null);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		result.rejectValue("name", "nullName");
		String view = shopController.processNewShopForm(shop, result);
		
		assertEquals(view, "shops/createOrUpdateShopForm");
	}
	
	@Test
	void testInitUpdateShopForm() throws Exception {
		ModelMap model = new ModelMap();
		Shop shop = shopService.findShopById(1);
		model.put("shop", shop);
		int shopId = shop.getId();
		
		String view = shopController.initUpdateShopForm(shopId, model);
		assertEquals(view, "shops/createOrUpdateShopForm");
	}
	
	@Test
	void testProcessUpdateShopFormSuccess() throws Exception {
		Shop shop = shopService.findShopById(1);
		shop.setName("shop1");
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		
		String view = shopController.processUpdateShopForm(shop, result, shop.getId());
		
		assertEquals(view, "redirect:/shops/1");
	}
	
	@Test
   	void testProcessUpdateShopFormHasErrors() throws Exception {
		Shop shop = shopService.findShopById(1);
		shop.setName(null);
		BindingResult result = new MapBindingResult(Collections.emptyMap(), "");
		result.rejectValue("name", "nullName");
		String view = shopController.processUpdateShopForm(shop, result, shop.getId());
		
		assertEquals(view, "shops/createOrUpdateShopForm");
		
	}
	
	@Test
   	void testShowShopDetails() throws Exception {
		ModelAndView view = shopController.showShop();
		
		assertEquals(view.getViewName(), "shops/shopDetails");
	}
	

}

package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Shop;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ShopServiceTests {

	@Autowired
	protected ShopService shopService;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Test
	void shouldFindShopWithCorrectId() throws Exception {
		Shop shop1 = this.shopService.findShopById(1);
		assertThat(shop1.getName()).isEqualTo("shop1");
	}
	
	@Test
	@Transactional
	public void shouldSaveShop() {
		Iterable<Shop> shops = this.shopService.findShops();
		long found = shops.spliterator().estimateSize();

		Shop shop1 = new Shop();
		shop1.setId(1);
		shop1.setName("shop1");
		
		this.shopService.saveShop(shop1);
		assertThat(shops.spliterator().estimateSize()).isEqualTo(found);
	}
	
	@Test
	void shouldReturnCorrectNumberOfProducts() throws Exception {
		Shop shop1 = this.shopService.findShopById(1);
		assertThat(shop1.getNumberOfProducts()).isEqualTo(6);
	}
	
	@Test
	void shouldReturnCorrectNumberOfOrders() throws Exception {
		Shop shop1 = this.shopService.findShopById(1);
		assertThat(shop1.getNumberOfOrders()).isEqualTo(5);
	}


}

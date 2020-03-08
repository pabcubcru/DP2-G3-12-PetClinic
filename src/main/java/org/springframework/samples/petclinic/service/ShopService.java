package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Shop;
import org.springframework.samples.petclinic.repository.springdatajpa.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShopService {

	private ShopRepository shopRepository;
	
	@Autowired
	public ShopService(ShopRepository shopRepository) {
		this.shopRepository = shopRepository;
	}	
	
	@Transactional(readOnly = true)	
	public Iterable<Shop> findShops() throws DataAccessException {
		return shopRepository.findAll();
	}
	
	@Transactional
	public void saveShop(Shop shop) throws DataAccessException {
		this.shopRepository.save(shop);
	}
}

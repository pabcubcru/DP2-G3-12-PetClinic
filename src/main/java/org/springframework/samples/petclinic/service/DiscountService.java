package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Discount;
import org.springframework.samples.petclinic.repository.springdatajpa.DiscountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiscountService {

	private DiscountRepository discountRepository;
	
	@Autowired
	public DiscountService(DiscountRepository discountRepository) {
		this.discountRepository = discountRepository;
	}	
	
	@Transactional
	public void saveDiscount(Discount discount) throws DataAccessException {
		this.discountRepository.save(discount);
	}
}

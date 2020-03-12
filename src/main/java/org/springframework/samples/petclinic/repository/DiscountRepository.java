package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Discount;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends CrudRepository<Discount, Integer>{

}

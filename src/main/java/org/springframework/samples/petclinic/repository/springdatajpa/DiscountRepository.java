package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Discount;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepository extends CrudRepository<Discount, Integer>{

	@Query("delete from Discount d where d.id=?1")
	public void deleteDisc(int id) throws DataAccessException;
}

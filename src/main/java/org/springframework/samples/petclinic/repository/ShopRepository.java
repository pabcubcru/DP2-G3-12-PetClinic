
package org.springframework.samples.petclinic.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Shop;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends CrudRepository<Shop, Integer> {

	Shop findById(int id) throws DataAccessException;
}

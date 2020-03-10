package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Hospitalisation;

public interface HospitalisationRepository {
	
	void save(Hospitalisation hospitalisation) throws DataAccessException;

	List<Hospitalisation> findByPetId(Integer petId);

}

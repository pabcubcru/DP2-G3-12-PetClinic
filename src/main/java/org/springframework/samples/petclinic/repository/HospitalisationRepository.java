package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Hospitalisation;
import org.springframework.samples.petclinic.model.HospitalisationStatus;

public interface HospitalisationRepository extends CrudRepository<Hospitalisation, Integer>{
	
	@Query("select h from Hospitalisation h where h.pet.id = ?1")
	List<Hospitalisation> findByPetId(int petId);
	
	@Query("SELECT hstatus FROM HospitalisationStatus hstatus ORDER BY hstatus.name")
	List<HospitalisationStatus> findHospitalisationStatus() throws DataAccessException;

}

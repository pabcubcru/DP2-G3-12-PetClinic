package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Hospitalisation;
import org.springframework.samples.petclinic.repository.springdatajpa.SpringDataHospitalisationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HospitalisationService {

	private SpringDataHospitalisationRepository hospitalisationRepository;
	
	@Autowired
	public HospitalisationService(SpringDataHospitalisationRepository hospitalisationRepository) {
		this.hospitalisationRepository = hospitalisationRepository;
	}	
	
	@Transactional(readOnly = true)	
	public Iterable<Hospitalisation> findHospitalisations() throws DataAccessException {
		return hospitalisationRepository.findAll();
	}
	
	@Transactional
	public void saveHospitalisation(Hospitalisation hospitalisation) throws DataAccessException {
		this.hospitalisationRepository.save(hospitalisation);
	}

}

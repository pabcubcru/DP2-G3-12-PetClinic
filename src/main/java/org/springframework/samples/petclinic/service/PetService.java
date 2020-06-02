/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Hospitalisation;
import org.springframework.samples.petclinic.model.HospitalisationStatus;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetStatus;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Stay;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.HospitalisationRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.StayRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
@Service
public class PetService {

	private PetRepository petRepository;
	
	private VisitRepository visitRepository;
	
	private StayRepository stayRepository;
	
	private HospitalisationRepository hospitalisationRepository;
	

	@Autowired
	public PetService(PetRepository petRepository,
			VisitRepository visitRepository, StayRepository stayRepository, HospitalisationRepository hospitalisationRepository) {
		this.petRepository = petRepository;
		this.visitRepository = visitRepository;
		this.stayRepository = stayRepository;
		this.hospitalisationRepository = hospitalisationRepository;
	}

	@Transactional(readOnly = true)
	public Collection<PetType> findPetTypes() throws DataAccessException {
		return petRepository.findPetTypes();
	}
	
	@Transactional(readOnly = true)
	public Collection<PetStatus> findPetStatus() throws DataAccessException {
		return petRepository.findPetStatus();
	}
	
	@Transactional
	@CacheEvict(cacheNames = "ownerById", allEntries = true)
	public void saveVisit(Visit visit) throws DataAccessException {
		visitRepository.save(visit);
	}
	
	@Transactional
	@CacheEvict(cacheNames = "ownerById", allEntries = true)
	public void deleteVisit(Visit visit) throws DataAccessException {
		visitRepository.delete(visit);
	}
	
	@Transactional
	@CacheEvict(cacheNames = "ownerById", allEntries = true)
	public void saveStay(Stay stay) throws DataAccessException {
		stayRepository.save(stay);
	}
	
	@Transactional
	@CacheEvict(cacheNames = "ownerById", allEntries = true)
	public void deleteStay(Stay stay) throws DataAccessException {
		stayRepository.delete(stay);
	}

	@Transactional  
	@CacheEvict(cacheNames = "ownerById", allEntries = true)
	public void saveHospitalisation(Hospitalisation hospitalisation) throws DataAccessException {
		hospitalisationRepository.save(hospitalisation);
	}
	
	@Transactional
	@CacheEvict(cacheNames = "ownerById", allEntries = true)
	public void deleteHospitalisation(Hospitalisation hospitalisation) throws DataAccessException {
		hospitalisationRepository.delete(hospitalisation);
	}

	public Pet findPetById(int id) throws DataAccessException {
		return petRepository.findById(id);
	}

	@Transactional(rollbackFor = DuplicatedPetNameException.class)
	@CacheEvict(cacheNames = "ownerById", allEntries = true)
	public void savePet(Pet pet) throws DataAccessException, DuplicatedPetNameException {
			Pet otherPet=pet.getOwner().getPetwithIdDifferent(pet.getName(), pet.getId());
            if (StringUtils.hasLength(pet.getName()) &&  (otherPet!= null && otherPet.getId()!=pet.getId())) {            	
            	throw new DuplicatedPetNameException();
            }else
                petRepository.save(pet);                
	}
	
	@Transactional
	@CacheEvict(cacheNames = "ownerById", allEntries = true)
	public void deletePet(Pet pet) throws DataAccessException {
		petRepository.delete(pet);
	}


	public Collection<Visit> findVisitsByPetId(int petId) throws DataAccessException {
		return visitRepository.findByPetId(petId);
	}
	
	public Collection<Stay> findStaysByPetId(int petId) throws DataAccessException {
		return stayRepository.findByPetId(petId);
		
	}
	
	public Stay findStayById(int id) throws DataAccessException {
		return stayRepository.findById(id).orElse(null);
	}
    
	public Collection<Hospitalisation> findHospitalisationsByPetId(int petId) throws DataAccessException{
		return hospitalisationRepository.findByPetId(petId);
	}
	
	public Hospitalisation findHospitalisationById(int id) throws DataAccessException {
		return hospitalisationRepository.findById(id).get();
	}
  
	@Transactional(readOnly = true)
	public Collection<HospitalisationStatus> findHospitalisationStatus() throws DataAccessException {
		return hospitalisationRepository.findHospitalisationStatus();
	}

}

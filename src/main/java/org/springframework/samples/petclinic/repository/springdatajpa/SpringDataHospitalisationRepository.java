package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Hospitalisation;

public interface SpringDataHospitalisationRepository extends CrudRepository<Hospitalisation, Integer> {

}

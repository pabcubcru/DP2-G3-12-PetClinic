package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.samples.petclinic.model.Hospitalisation;
import org.springframework.samples.petclinic.repository.HospitalisationRepository;

public interface SpringDataHospitalisationRepository extends HospitalisationRepository, org.springframework.data.repository.Repository<Hospitalisation, Integer> {

}

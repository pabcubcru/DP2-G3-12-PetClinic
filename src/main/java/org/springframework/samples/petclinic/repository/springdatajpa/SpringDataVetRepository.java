package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.VetRepository;

/**
 * Spring Data JPA specialization of the {@link VetRepository} interface
 *
 * @author Michael Isvy
 * @since 15.1.2013
 */
public interface SpringDataVetRepository extends VetRepository, Repository<Vet, Integer> {

	@Override
	@Query("SELECT vet FROM Vet vet")
	List<Vet> findAll();

	@Override
	@Query("SELECT vet FROM Vet vet WHERE vet.id = ?1")
	Vet findVetById(int id);

	@Override
	@Query("SELECT specialty FROM Specialty specialty")
	Set<Specialty> findSpecialties();

}
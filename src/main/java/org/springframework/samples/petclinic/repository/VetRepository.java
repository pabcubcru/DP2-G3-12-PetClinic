package org.springframework.samples.petclinic.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.samples.petclinic.model.Specialty;
import org.springframework.samples.petclinic.model.Vet;

/**
 * Repository class for <code>Vet</code> domain objects All method names are compliant
 * with Spring Data naming conventions so this interface can easily be extended for Spring
 * Data See here:
 * http://static.springsource.org/spring-data/jpa/docs/current/reference/html/jpa.repositories.html#jpa.query-methods.query-creation
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
public interface VetRepository {
	/**
	 * Retrieve all <code>Vet</code>s from the data store.
	 * @return a <code>Collection</code> of <code>Vet</code>s
	 */
    Vet findVetById(int id);
	void save(Vet vet);
	Set<Specialty> findSpecialties();
	List<Vet> findAll();
}
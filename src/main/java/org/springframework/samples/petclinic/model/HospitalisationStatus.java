package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "hospitalisation_status")
public class HospitalisationStatus extends NamedEntity{

}

package org.springframework.samples.petclinic.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.samples.petclinic.model.Hospitalisation;

public class HospitalValidation {
	
	private HospitalValidation() {
		new HospitalValidation();
	}
	
	public static boolean validationHospital(Hospitalisation hospitalisation, Collection<Hospitalisation> hospitalisations) {
		boolean ocupado = false;
		List<Hospitalisation> hospitalisationsOfAPet = new ArrayList<>(hospitalisations);

		for (int i = 0; i < hospitalisations.size(); i++) {
			if (ocupado) {
				break;
			} else if(!hospitalisationsOfAPet.get(i).equals(hospitalisation)){
				LocalDate actualStartDate = hospitalisationsOfAPet.get(i).getStartDate();
				LocalDate actualFinishDate = hospitalisationsOfAPet.get(i).getFinishDate();
				LocalDate newStartDate = hospitalisation.getStartDate();
				LocalDate newFinishDate = hospitalisation.getFinishDate();

				boolean b1 = newStartDate.isBefore(actualStartDate);
				boolean b2 = newFinishDate.isBefore(actualStartDate);
				boolean b3 = newStartDate.isAfter(actualFinishDate);
				boolean b4 = newFinishDate.isAfter(actualFinishDate);

				if (!((b1 && b2) || (b3 && b4))) {
					ocupado = true;
				}
			}
		}
		return ocupado;
	}

}

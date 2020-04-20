package org.springframework.samples.petclinic.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.samples.petclinic.model.Stay;

public class Validaciones {

	private Validaciones() {
		new Validaciones();
	}

	public static boolean validacionReserva(Stay stay, Collection<Stay> stays) {
		boolean ocupado = false;
		List<Stay> staysOfAPet = new ArrayList<>(stays);

		for (int i = 0; i < stays.size(); i++) {
			if (ocupado) {
				break;
			} else {
				LocalDate actualStartDate = staysOfAPet.get(i).getStartdate();
				LocalDate actualFinishDate = staysOfAPet.get(i).getFinishdate();
				LocalDate newStartDate = stay.getStartdate();
				LocalDate newFinishDate = stay.getFinishdate();

				boolean b1 = newStartDate.isBefore(actualStartDate);
				boolean b2 = newFinishDate.isBefore(actualStartDate);
				boolean b3 = newStartDate.isAfter(actualFinishDate);
				boolean b4 = newFinishDate.isAfter(actualFinishDate);

				if (!((b1 && b2) || b3 && b4)) {
					ocupado = true;
				}
			}
		}
		return ocupado;
	}
}

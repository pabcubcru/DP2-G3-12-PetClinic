package org.springframework.samples.petclinic.util;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.samples.petclinic.model.Stay;

public class Validaciones {

	private Validaciones() {
		new Validaciones();
	}

	public static boolean validacionReserva(Stay newStay, Collection<Stay> stays) {
		boolean res = false;

		for (Stay oldStay : stays) {
			LocalDate oldStayStartDate = oldStay.getStartdate();
			LocalDate oldStayFinishDate = oldStay.getFinishdate();
			LocalDate newStayStartDate = newStay.getStartdate();
			LocalDate newStayFinishDate = newStay.getFinishdate();

			boolean b1 = newStayStartDate.isBefore(oldStayStartDate);
			boolean b2 = newStayFinishDate.isBefore(oldStayStartDate);
			boolean b3 = newStayStartDate.isAfter(oldStayFinishDate);
			boolean b4 = newStayFinishDate.isAfter(oldStayFinishDate);

			boolean newStayStartDateAndFinishDateIsBeforeOldStayStartDate = b1 && b2;
			boolean newStayStartDateAndFinishDateIsAfterOldStayFinishDate = b3 && b4;

			boolean isBusy = !(newStayStartDateAndFinishDateIsBeforeOldStayStartDate
					|| newStayStartDateAndFinishDateIsAfterOldStayFinishDate);

			if (isBusy) {
				res = true;
				break;
			}
		}
		return res;
	}
}

package library.entities.helpers;

import library.entities.IPatron;
import library.entities.Patron;

public class PatronHelper implements IPatronHelper {

	private static final long serialVersionUID = 1L;

	@Override
	public IPatron makePatron(String lastName, String firstName, String email, long phoneNo, int id) {
		return new Patron(lastName, firstName, email, phoneNo, id);
	}

}

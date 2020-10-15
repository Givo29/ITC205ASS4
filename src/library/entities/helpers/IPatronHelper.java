package library.entities.helpers;

import java.io.Serializable;

import library.entities.IPatron;

public interface IPatronHelper extends Serializable {

	public IPatron makePatron(String lastName, String firstName, String email, long phoneNo, int id);

}

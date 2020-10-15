package library.entities.helpers;

import java.io.Serializable;

import library.entities.IBook;
import library.entities.ILoan;
import library.entities.IPatron;

public interface ILoanHelper extends Serializable {

	ILoan makeLoan(IBook book, IPatron patron);

}
package library.entities.helpers;

import library.entities.IBook;
import library.entities.ILoan;
import library.entities.IPatron;
import library.entities.Loan;

public class LoanHelper implements ILoanHelper {

	private static final long serialVersionUID = 1L;

	@Override
	public ILoan makeLoan(IBook book, IPatron patron) {
		return new Loan(book, patron);
	}
}

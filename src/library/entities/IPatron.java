package library.entities;

import java.util.List;

public interface IPatron {

	enum PatronState {
		CAN_BORROW, RESTRICTED
	};

	int getId();

	String getLastName();

	String getFirstName();

	List<ILoan> getLoans();

	double getFinesPayable();

	boolean hasOverDueLoans();

	void restrictBorrowing();

	void allowBorrowing();
	
	boolean canBorrow();
	
	boolean isRestricted();

	int getNumberOfCurrentLoans();

	void takeOutLoan(ILoan loan);

	void dischargeLoan(ILoan loan);

	void incurFine(double fine);

	double payFine(double paymentAmount);

}
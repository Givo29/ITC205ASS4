package library.entities;

import java.util.Date;

public interface ILoan {

	enum LoanState {
		PENDING, CURRENT, OVER_DUE, DISCHARGED
	};

	int getId();


	IPatron getPatron();

	IBook getBook();

	Date getDueDate();

	void commit(int loanId, Date dueDate);

	void discharge(boolean isDamaged);

	void updateOverDueStatus(Date currentDate);

	boolean isPending();

	boolean isCurrent();

	boolean isOverDue();

	boolean isDischarged();

}
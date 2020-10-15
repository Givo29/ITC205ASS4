package library.entities;

import java.util.List;

public interface ILibrary {

	static final int LOAN_LIMIT = 2;
	static final int LOAN_PERIOD = 2;
	static final double FINE_PER_DAY = 1.0;
	static final double MAX_FINES_OWED = 1.0;
	static final double DAMAGE_FEE = 2.0;

	IPatron addPatron(String lastName, String firstName, String email, long phoneNo);

	IBook addBook(String author, String title, String callNumber);

	List<IPatron> getPatronList();

	List<IBook> getBookList();

	List<ILoan> getCurrentLoansList();

	IPatron getPatronById(int patronId);

	IBook getBookById(int bookId);

	ILoan getCurrentLoanByBookId(int bookId);

	boolean patronCanBorrow(IPatron patron);

	boolean patronWillReachLoanMax(IPatron patron, int numberOfPendingLoans);

	ILoan issueLoan(IBook book, IPatron patron);

	void commitLoan(ILoan loan);

	void dischargeLoan(ILoan currentLoan, boolean isDamaged);

	void checkCurrentLoansOverDue();

	double calculateOverDueFine(ILoan loan);

	double payFine(IPatron patron, double amount);

	void repairBook(IBook currentBook);

}
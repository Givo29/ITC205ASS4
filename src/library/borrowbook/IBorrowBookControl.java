package library.borrowbook;

public interface IBorrowBookControl {

	enum BorrowControlState {
		INITIALISED, SWIPING, RESTRICTED, SCANNING, IDENTIFIED, FINALISING, COMPLETED, CANCELLED
	};

	void setUI(IBorrowBookUI ui);

	void cardSwiped(int patronId);

	void bookScanned(int bookId);

	void borrowingCompleted();

	void commitLoans();

	void cancel();

}
package library.entities;

public interface IBook {

	enum BookState {
		AVAILABLE, ON_LOAN, DAMAGED
	};

	int getId();

	String getTitle();

	boolean isAvailable();

	boolean isOnLoan();

	boolean isDamaged();

	void borrowFromLibrary();

	void returnToLibrary(boolean isDamaged);
	
	void repair();


}
package library.borrowbook;

public interface IBorrowBookUI {

	enum BorrowUIState {
		INITIALISED, SWIPING, SCANNING, RESTRICTED, FINALISING, COMPLETED, CANCELLED
	};

	void run();

	void display(Object object);

	void setSwiping();

	void setScanning();

	void setRestricted();

	void setFinalising();

	void setCompleted();

	void setCancelled();

}
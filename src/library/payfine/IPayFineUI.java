package library.payfine;

public interface IPayFineUI {

	enum UIStateConstants {
		INITIALISED, READY, PAYING, COMPLETED, CANCELLED
	};

	void setState(UIStateConstants state);

	void run();

	void display(Object object);

}
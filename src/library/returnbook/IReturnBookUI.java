package library.returnbook;

public interface IReturnBookUI {

	enum UIStateConstants {
		INITIALISED, READY, INSPECTING, COMPLETED
	};

	void run();

	void display(Object object);

	void setState(UIStateConstants state);

}
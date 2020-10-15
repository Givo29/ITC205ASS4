package library.fixbook;

public interface IFixBookUI {

	enum UIStateConstants {
		INITIALISED, READY, FIXING, COMPLETED
	};

	void setUiState(UIStateConstants uiState);

	void run();

	void display(Object object);

}
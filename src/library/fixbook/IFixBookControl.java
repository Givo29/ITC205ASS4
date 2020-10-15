package library.fixbook;

public interface IFixBookControl {

	void setUi(IFixBookUI ui);

	void bookScanned(int bookId);

	void fixBook(boolean mustFix);

	void scanningCompleted();

}
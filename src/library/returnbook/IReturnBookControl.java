package library.returnbook;

public interface IReturnBookControl {

	void setUI(IReturnBookUI ui);

	void bookScanned(int bookId);

	void scanningComplete();

	void dischargeLoan(boolean isDamaged);

}
package library.payfine;

public interface IPayFineControl {

	void setUi(IPayFineUI payFineUi);

	void cardSwiped(int patronId);

	void cancel();

	double payFine(double amount);

}
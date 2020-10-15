package library.payfine;

import java.util.Scanner;

public class PayFineUI implements IPayFineUI {

	private IPayFineControl control;
	private Scanner input;
	private UIStateConstants uiState;

	public PayFineUI(IPayFineControl control) {
		this.control = control;
		input = new Scanner(System.in);
		uiState = UIStateConstants.INITIALISED;
		control.setUi(this);
	}

	@Override
	public void setState(UIStateConstants state) {
		this.uiState = state;
	}

	@Override
	public void run() {
		output("Pay Fine Use Case UI\n");

		while (true) {

			switch (uiState) {

			case READY:
				String patronIdString = input("Swipe patron card (press <enter> to cancel): ");
				if (patronIdString.length() == 0) {
					control.cancel();
					break;
				}
				try {
					int patronId = Integer.valueOf(patronIdString).intValue();
					control.cardSwiped(patronId);
				} catch (NumberFormatException e) {
					output("Invalid patronId");
				}
				break;

			case PAYING:
				double paymentAmount = 0;
				String paymentAmountString = input("Enter amount (<Enter> cancels) : ");
				if (paymentAmountString.length() == 0) {
					control.cancel();
					break;
				}
				try {
					paymentAmount = Double.valueOf(paymentAmountString).doubleValue();
				} catch (NumberFormatException e) {
				}
				if (paymentAmount <= 0) {
					output("Amount must be positive");
					break;
				}
				control.payFine(paymentAmount);
				break;

			case CANCELLED:
				output("Pay Fine process cancelled");
				return;

			case COMPLETED:
				output("Pay Fine process complete");
				return;

			default:
				output("Unhandled state");
				throw new RuntimeException("FixBookUI : unhandled state :" + uiState);

			}
		}
	}

	private String input(String prompt) {
		System.out.print(prompt);
		return input.nextLine();
	}

	private void output(Object object) {
		System.out.println(object);
	}

	@Override
	public void display(Object object) {
		output(object);
	}

}

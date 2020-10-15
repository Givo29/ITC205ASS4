package library.fixbook;

import java.util.Scanner;

public class FixBookUI implements IFixBookUI {

	private IFixBookControl control;
	private Scanner scanner;
	private UIStateConstants uiState;

	public FixBookUI(IFixBookControl control) {
		this.control = control;
		scanner = new Scanner(System.in);
		uiState = UIStateConstants.INITIALISED;
		control.setUi(this);
	}

	@Override
	public void setUiState(UIStateConstants uiState) {
		this.uiState = uiState;
	}

	@Override
	public void run() {
		output("Fix Book Use Case UI\n");

		while (true) {

			switch (uiState) {

			case READY:
				String bookBarcodeString = input("Scan Book (<enter> completes): ");
				if (bookBarcodeString.length() == 0) {
					control.scanningCompleted();
				} else {
					try {
						int bookBarcode = Integer.valueOf(bookBarcodeString).intValue();
						control.bookScanned(bookBarcode);
					} catch (NumberFormatException e) {
						output("Invalid bookId");
					}
				}
				break;

			case FIXING:
				String userResponse = input("Fix Book? (Y/N) : ");
				boolean mustFix = false;
				if (userResponse.toUpperCase().equals("Y")) {
					mustFix = true;
				}
				control.fixBook(mustFix);
				break;

			case COMPLETED:
				output("Fixing process complete");
				return;

			default:
				output("Unhandled state");
				throw new RuntimeException("FixBookUI : unhandled state :" + uiState);

			}
		}

	}

	private String input(String prompt) {
		System.out.print(prompt);
		return scanner.nextLine();
	}

	private void output(Object object) {
		System.out.println(object);
	}

	@Override
	public void display(Object object) {
		output(object);
	}

}

package library.borrowbook;

import java.util.Scanner;

public class BorrowBookUI implements IBorrowBookUI {

	private IBorrowBookControl borrowBookControl;
	private Scanner input;
	private BorrowUIState uiState;

	public BorrowBookUI(IBorrowBookControl control) {
		this.borrowBookControl = control;
		input = new Scanner(System.in);
		uiState = BorrowUIState.INITIALISED;
		control.setUI(this);
	}

	private String getUserInput(String prompt) {
		System.out.print(prompt);
		return input.nextLine();
	}

	private void showOutput(Object object) {
		System.out.println(object);
	}

	@Override
	public void display(Object object) {
		showOutput(object);
	}

	@Override
	public void run() {
		showOutput("Borrow Book Use Case UI\n");

		boolean finished = false;

		while (!finished) {

			switch (uiState) {

			case SWIPING:
				String patronIdString = getUserInput("Swipe patron card (press <enter> to cancel): ");
				if (patronIdString.length() == 0) {
					borrowBookControl.cancel();
					break;
				}
				try {
					int patronId = Integer.valueOf(patronIdString).intValue();
					borrowBookControl.cardSwiped(patronId);
				} catch (NumberFormatException e) {
					showOutput("Invalid Patron Id");
				}
				break;

			case RESTRICTED:
				getUserInput("Press <any key> to cancel");
				borrowBookControl.cancel();
				break;

			case SCANNING:
				String bookIdString = getUserInput("Scan Book (<enter> completes): ");
				if (bookIdString.length() == 0) {
					borrowBookControl.borrowingCompleted();
					break;
				}
				try {
					int bookId = Integer.valueOf(bookIdString).intValue();
					borrowBookControl.bookScanned(bookId);
				} catch (NumberFormatException e) {
					showOutput("Invalid Book Id");
				}
				break;

			case FINALISING:
				String answer = getUserInput("Commit loans? (Y/N): ");
				if (answer.toUpperCase().equals("N")) {
					borrowBookControl.cancel();
				} else {
					borrowBookControl.commitLoans();
				}
				break;

			case COMPLETED:
				showOutput("Borrowing Completed");
				getUserInput("Press <any key> to exit");
				finished = true;
				break;

			case CANCELLED:
				showOutput("Borrowing Cancelled");
				finished = true;
				break;

			default:
				showOutput("Unhandled state");
				throw new RuntimeException("BorrowBookUI : unhandled state :" + uiState);
			}
		}
	}

	@Override
	public void setSwiping() {
		if (!uiState.equals(BorrowUIState.INITIALISED)) {
			throw new RuntimeException("BorrowBookUI: cannot call setSwiping except in INITIALISED state");
		}
		this.uiState = BorrowUIState.SWIPING;
	}

	@Override
	public void setScanning() {
		if (!uiState.equals(BorrowUIState.SWIPING)) {
			throw new RuntimeException("BorrowBookUI: cannot call setScanning except in SWIPING state");
		}
		this.uiState = BorrowUIState.SCANNING;
	}

	@Override
	public void setRestricted() {
		if (!uiState.equals(BorrowUIState.SWIPING)) {
			throw new RuntimeException("BorrowBookUI: cannot call setRestricted except in SWIPING state");
		}
		this.uiState = BorrowUIState.RESTRICTED;
	}

	@Override
	public void setFinalising() {
		if (!uiState.equals(BorrowUIState.SCANNING)) {
			throw new RuntimeException("BorrowBookUI: cannot call setFinalising except in SCANNING state");
		}
		this.uiState = BorrowUIState.FINALISING;
	}

	@Override
	public void setCompleted() {
		if (!uiState.equals(BorrowUIState.FINALISING)) {
			throw new RuntimeException("BorrowBookUI: cannot call setCompleted except in FINALISING state");
		}
		this.uiState = BorrowUIState.COMPLETED;
	}

	@Override
	public void setCancelled() {
		if (uiState.equals(BorrowUIState.COMPLETED)) {
			throw new RuntimeException("BorrowBookUI: cannot call setCancelled in COMPLETED state");
		}
		this.uiState = BorrowUIState.CANCELLED;
	}

}

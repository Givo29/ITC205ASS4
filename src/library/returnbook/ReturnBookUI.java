package library.returnbook;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import library.entities.Calendar;

public class ReturnBookUI implements IReturnBookUI {

	private IReturnBookControl returnBookControl;
	private Scanner input;
	private UIStateConstants uiState;

	public ReturnBookUI(IReturnBookControl control) {
		this.returnBookControl = control;
		input = new Scanner(System.in);
		uiState = UIStateConstants.INITIALISED;
		control.setUI(this);
	}

	@Override
	public void run() {
		showOutput("Return Book Use Case UI");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date currentDate = Calendar.getInstance().getDate();
		String dateString = dateFormat.format(currentDate);
		showOutput(dateString + "\n");

		while (true) {

			switch (uiState) {

			case INITIALISED:
				break;

			case READY:
				String bookStr = getUserInput("Scan Book (<enter> completes): ");
				if (bookStr.length() == 0) {
					returnBookControl.scanningComplete();
				} else {
					try {
						int bookId = Integer.valueOf(bookStr).intValue();
						returnBookControl.bookScanned(bookId);
					} catch (NumberFormatException e) {
						showOutput("Invalid bookId");
					}
				}
				break;

			case INSPECTING:
				String answer = getUserInput("Is book damaged? (Y/N): ");
				boolean isDamaged = false;
				if (answer.toUpperCase().equals("Y")) {
					isDamaged = true;
				}
				returnBookControl.dischargeLoan(isDamaged);

			case COMPLETED:
				showOutput("Return processing complete");
				return;

			default:
				showOutput("Unhandled state");
				throw new RuntimeException("ReturnBookUI : unhandled state :" + uiState);
			}
		}
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
	public void setState(UIStateConstants state) {
		this.uiState = state;
	}

}

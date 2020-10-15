package library.returnbook;

import library.entities.IBook;
import library.entities.ILibrary;
import library.entities.ILoan;

public class ReturnBookControl implements IReturnBookControl {

	private IReturnBookUI returnBookUI;

	private enum ControlStateConstants {
		INITIALISED, READY, INSPECTING
	};

	private ControlStateConstants controlState;

	private ILibrary library;
	private ILoan currentLoan;

	public ReturnBookControl(ILibrary library) {
		this.library = library;
		controlState = ControlStateConstants.INITIALISED;
	}

	@Override
	public void setUI(IReturnBookUI ui) {
		if (!controlState.equals(ControlStateConstants.INITIALISED)) {
			throw new RuntimeException("ReturnBookControl: cannot call setUI except in INITIALISED state");
		}
		this.returnBookUI = ui;
		ui.setState(IReturnBookUI.UIStateConstants.READY);
		controlState = ControlStateConstants.READY;
	}

	@Override
	public void bookScanned(int bookId) {
		if (!controlState.equals(ControlStateConstants.READY)) {
			throw new RuntimeException("ReturnBookControl: cannot call bookScanned except in READY state");
		}
		IBook currentBook = library.getBookById(bookId);
		if (currentBook == null) {
			returnBookUI.display("Invalid Book Id");
			return;
		}
		if (!currentBook.isOnLoan()) {
			returnBookUI.display("Book has not been borrowed");
			return;
		}
		currentLoan = library.getCurrentLoanByBookId(bookId);
		double overDueFine = 0.0;
		if (currentLoan.isOverDue()) {
			overDueFine = library.calculateOverDueFine(currentLoan);
			currentLoan.getPatron().incurFine(overDueFine);
		}
		returnBookUI.display("Inspecting");
		returnBookUI.display(currentBook);
		returnBookUI.display(currentLoan);

		if (currentLoan.isOverDue()) {
			returnBookUI.display(String.format("\nOverdue fine : $%.2f", overDueFine));
		}
		returnBookUI.setState(IReturnBookUI.UIStateConstants.INSPECTING);
		controlState = ControlStateConstants.INSPECTING;
	}

	@Override
	public void scanningComplete() {
		if (!controlState.equals(ControlStateConstants.READY)) {
			throw new RuntimeException("ReturnBookControl: cannot call scanningComplete except in READY state");
		}
		returnBookUI.setState(IReturnBookUI.UIStateConstants.COMPLETED);
	}

	@Override
	public void dischargeLoan(boolean isDamaged) {
		if (!controlState.equals(ControlStateConstants.INSPECTING)) {
			throw new RuntimeException("ReturnBookControl: cannot call dischargeLoan except in INSPECTING state");
		}
		library.dischargeLoan(currentLoan, isDamaged);
		currentLoan = null;
		returnBookUI.setState(IReturnBookUI.UIStateConstants.READY);
		controlState = ControlStateConstants.READY;
	}

}

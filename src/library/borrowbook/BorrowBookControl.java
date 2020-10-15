package library.borrowbook;

import java.util.ArrayList;
import java.util.List;

import library.entities.IBook;
import library.entities.ILibrary;
import library.entities.ILoan;
import library.entities.IPatron;

public class BorrowBookControl implements IBorrowBookControl {

	IBorrowBookUI borrowBookUI;

	ILibrary library;
	IPatron currentPatron;
	BorrowControlState controlState;

	List<ILoan> pendingLoans;
	IBook currentBook;

	public BorrowBookControl(ILibrary library) {
		this.library = library;
		controlState = BorrowControlState.INITIALISED;
	}

	@Override
	public void setUI(IBorrowBookUI ui) {
		if (!controlState.equals(BorrowControlState.INITIALISED)) {
			throw new RuntimeException("BorrowBookControl: cannot call setUI except in INITIALISED state");
		}

		this.borrowBookUI = ui;
		ui.setSwiping();
		controlState = BorrowControlState.SWIPING;
	}

	@Override
	public void cardSwiped(int patronId) {
		if (!controlState.equals(BorrowControlState.SWIPING)) {
			throw new RuntimeException("BorrowBookControl: cannot call cardSwiped except in READY state");
		}

		currentPatron = library.getPatronById(patronId);
		if (currentPatron == null) {
			borrowBookUI.display("Invalid patronId");
			return;
		}
		if (library.patronCanBorrow(currentPatron)) {
			pendingLoans = new ArrayList<>();
			borrowBookUI.setScanning();
			controlState = BorrowControlState.SCANNING;
		} else {
			borrowBookUI.display("Patron cannot borrow at this time");
			borrowBookUI.setRestricted();
			controlState = BorrowControlState.RESTRICTED;
		}
	}

	@Override
	public void bookScanned(int bookId) {
		currentBook = null;
		if (!controlState.equals(BorrowControlState.SCANNING)) {
			throw new RuntimeException("BorrowBookControl: cannot call bookScanned except in SCANNING state");
		}
		currentBook = library.getBookById(bookId);
		if (currentBook == null) {
			borrowBookUI.display("Invalid bookId");
			return;
		}
		if (!currentBook.isAvailable()) {
			borrowBookUI.display("Book cannot be borrowed");
			return;
		}
		ILoan pendingLoan = library.issueLoan(currentBook, currentPatron);

		pendingLoans.add(pendingLoan);
		for (ILoan loan : pendingLoans) {
			IBook book = loan.getBook();
			borrowBookUI.display(book);
		}
		int numberOfPendingLoans = pendingLoans.size();
		if (library.patronWillReachLoanMax(currentPatron, numberOfPendingLoans)) {
			borrowBookUI.display("Loan limit reached");
			borrowingCompleted();
		}
	}

	@Override
	public void borrowingCompleted() {
		if (!controlState.equals(BorrowControlState.SCANNING)) {
			throw new RuntimeException("BorrowBookControl: cannot call bookScanned except in SCANNING state");
		}
		if (pendingLoans.size() == 0) {
			cancel();
		} else {
			borrowBookUI.display("\nFinal Borrowing List");
			for (ILoan loan : pendingLoans) {
				IBook book = loan.getBook();
				borrowBookUI.display(book);
			}
			borrowBookUI.setFinalising();
			controlState = BorrowControlState.FINALISING;
		}
	}

	@Override
	public void commitLoans() {
		if (!controlState.equals(BorrowControlState.FINALISING)) {
			throw new RuntimeException("BorrowBookControl: cannot call commitLoans except in FINALISING state");
		}
		for (ILoan loan : pendingLoans) {
			library.commitLoan(loan);
		}
		borrowBookUI.display("Completed Loan Slip");
		for (ILoan loan : pendingLoans) {
			borrowBookUI.display(loan);
		}
		borrowBookUI.setCompleted();
		controlState = BorrowControlState.COMPLETED;
	}

	@Override
	public void cancel() {
		borrowBookUI.setCancelled();
		controlState = BorrowControlState.CANCELLED;
	}

}

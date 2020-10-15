package library.fixbook;

import library.entities.IBook;
import library.entities.ILibrary;

public class FixBookControl implements IFixBookControl {

	private IFixBookUI fixBookUi;

	private enum ControlState {
		INITIALISED, READY, FIXING
	};

	private ControlState state;

	private ILibrary library;
	private IBook currentBook;

	public FixBookControl(ILibrary library) {
		this.library = library;
		state = ControlState.INITIALISED;
	}

	@Override
	public void setUi(IFixBookUI ui) {
		if (!state.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("FixBookControl: cannot call setUI except in INITIALISED state");
		}
		this.fixBookUi = ui;
		ui.setUiState(IFixBookUI.UIStateConstants.READY);
		state = ControlState.READY;
	}

	@Override
	public void bookScanned(int bookId) {
		if (!state.equals(ControlState.READY)) {
			throw new RuntimeException("FixBookControl: cannot call bookScanned except in READY state");
		}
		currentBook = library.getBookById(bookId);

		if (currentBook == null) {
			fixBookUi.display("Invalid bookId");
			return;
		}
		if (!currentBook.isDamaged()) {
			fixBookUi.display("Book has not been damaged");
			return;
		}
		fixBookUi.display(currentBook.toString());
		fixBookUi.setUiState(IFixBookUI.UIStateConstants.FIXING);
		state = ControlState.FIXING;
	}

	@Override
	public void fixBook(boolean mustFix) {
		if (!state.equals(ControlState.FIXING)) {
			throw new RuntimeException("FixBookControl: cannot call fixBook except in FIXING state");
		}
		if (mustFix) {
			library.repairBook(currentBook);
		}
		currentBook = null;
		fixBookUi.setUiState(IFixBookUI.UIStateConstants.READY);
		state = ControlState.READY;
	}

	@Override
	public void scanningCompleted() {
		if (!state.equals(ControlState.READY)) {
			throw new RuntimeException("FixBookControl: cannot call scanningComplete except in READY state");
		}
		fixBookUi.setUiState(IFixBookUI.UIStateConstants.COMPLETED);
	}

}

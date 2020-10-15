package library.payfine;

import library.entities.ILibrary;
import library.entities.IPatron;

public class PayFineControl implements IPayFineControl {

	private IPayFineUI payFineUi;

	private enum ControlState {
		INITIALISED, READY, PAYING, COMPLETED, CANCELLED
	};

	private ControlState state;

	private ILibrary library;
	private IPatron patron;

	public PayFineControl(ILibrary library) {
		this.library = library;
		state = ControlState.INITIALISED;
	}

	@Override
	public void setUi(IPayFineUI payFineUi) {
		if (!state.equals(ControlState.INITIALISED)) {
			throw new RuntimeException("PayFineControl: cannot call setUI except in INITIALISED state");
		}
		this.payFineUi = payFineUi;
		payFineUi.setState(IPayFineUI.UIStateConstants.READY);
		state = ControlState.READY;
	}

	@Override
	public void cardSwiped(int patronId) {
		if (!state.equals(ControlState.READY)) {
			throw new RuntimeException("PayFineControl: cannot call cardSwiped except in READY state");
		}
		patron = library.getPatronById(patronId);

		if (patron == null) {
			payFineUi.display("Invalid Patron Id");
			return;
		}
		payFineUi.display(patron);
		payFineUi.setState(IPayFineUI.UIStateConstants.PAYING);
		state = ControlState.PAYING;
	}

	@Override
	public void cancel() {
		payFineUi.setState(IPayFineUI.UIStateConstants.CANCELLED);
		state = ControlState.CANCELLED;
	}

	@Override
	public double payFine(double amount) {
		if (!state.equals(ControlState.PAYING)) {
			throw new RuntimeException("PayFineControl: cannot call payFine except in PAYING state");
		}
		double change = library.payFine(patron, amount);
		if (change > 0) {
			String changeDisplayString = String.format("Change: $%.2f", change);
			payFineUi.display(changeDisplayString);
		}
		payFineUi.display(patron);
		payFineUi.setState(IPayFineUI.UIStateConstants.COMPLETED);
		state = ControlState.COMPLETED;
		return change;
	}

}

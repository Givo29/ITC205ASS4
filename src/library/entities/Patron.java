package library.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class Patron implements Serializable, IPatron {

	String lastName;
	String firstName;
	String emailAddress;
	long phoneNumber;
	int id;
	double finesPayable;

	PatronState state;

	Map<Integer, ILoan> loans;


	 public Patron(String lastName, String firstName, String email, long phoneNo, int id, 
			 double finesPayable, PatronState state, Map<Integer, ILoan> loans) { 
		 
		 this.lastName = lastName; 
		 this.firstName = firstName; 
		 this.emailAddress = email;
		 this.phoneNumber = phoneNo; 
		 this.id = id;
		 this.finesPayable = finesPayable;
		 this.state = state;
		 this.loans = loans;
	}


	public Patron(String lastName, String firstName, String email, long phoneNo, int id) {
		this(lastName, firstName, email, phoneNo, id, 0.0, PatronState.CAN_BORROW, new HashMap<Integer, ILoan>());
	}

	
	@Override
	public int getId() {
		return id;
	}

	
	@Override
	public String getLastName() {
		return lastName;
	}

	
	@Override
	public String getFirstName() {
		return firstName;
	}

	
	@Override
	public List<ILoan> getLoans() {
		return new ArrayList<ILoan>(loans.values());
	}

	
	@Override
	public int getNumberOfCurrentLoans() {
		return loans.size();
	}

	
	@Override
	public double getFinesPayable() {
		return finesPayable;
	}

	
	@Override
	public void restrictBorrowing() {
		this.state = PatronState.RESTRICTED;
	}

	
	@Override
	public void allowBorrowing() {
		this.state = PatronState.CAN_BORROW;
	}

	
	@Override
	public boolean canBorrow() {
		return state == PatronState.CAN_BORROW;
	}


	@Override
	public boolean isRestricted() {
		return state == PatronState.RESTRICTED;
	}

	
	@Override
	public boolean hasOverDueLoans() {
		for (ILoan loan : loans.values()) {
			if (loan.isOverDue()) {
				return true;
			}
		}
		return false;
	}

	
	@Override
	public void takeOutLoan(ILoan loan) {
		if (this.state == PatronState.RESTRICTED) {
			throw new RuntimeException("Patron cannot borrow in RESTRICTED state");
		}
		int loanId = loan.getId();
		if (loans.containsKey(loanId)) {
			throw new RuntimeException("Attempted to add duplicate loan to patron");
		} 
		loans.put(loanId, loan);
	}

	
	@Override
	public void dischargeLoan(ILoan loan) {
		int loanId = loan.getId();
		
		if (!loans.containsKey(loanId)) {
			throw new RuntimeException("No such loan held by patron");
		} 
		loans.remove(loanId);
	}

	
	@Override
	public void incurFine(double fine) {
		finesPayable += fine;
	}

	
	@Override
	public double payFine(double paymentAmount) {
		if (paymentAmount < 0) {
			throw new RuntimeException("Patron.payFine: amount must be positive");
		}
		double change = 0;
		if (paymentAmount > finesPayable) {
			change = paymentAmount - finesPayable;
			finesPayable = 0;
		} 
		else {
			finesPayable -= paymentAmount;
		}
		return change;
	}

	
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		String finesPayableString = String.format("$%.2f", finesPayable);

		stringBuilder.append("Patron:  ").append(id).append("\n").append("  Name:  ").append(lastName).append(", ")
				.append(firstName).append("\n").append("  Email: ").append(emailAddress).append("\n")
				.append("  Phone: ").append(phoneNumber).append("\n").append("  Fines Owed :  ")
				.append(finesPayableString).append("\n").append("  State: ").append(state).append("\n");

		for (ILoan loan : loans.values()) {
			stringBuilder.append(loan).append("\n");
		}
		return stringBuilder.toString();
	}


}

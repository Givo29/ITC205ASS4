package library.entities;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("serial")
public class Loan implements Serializable, ILoan {

	int loanId;
	IBook book;
	IPatron patron;
	Date dueDate;
	LoanState state;


	public Loan(IBook book, IPatron patron, Date dueDate, LoanState state, int loanId) { 
		if (book == null || patron == null || state == null) {
			throw new RuntimeException("Loan : constructor : book, patron, and state cannot be null");
		}
		this.book = book;
	 	this.patron = patron;
	 	this.dueDate = dueDate;
	 	this.state = state; 
	 	this.loanId = loanId;
	 }
	
	
	public Loan(IBook book, IPatron patron) {
		this(book, patron, null, LoanState.PENDING, 0);
	}

	
	@Override
	public int getId() {
		return loanId;
	}

	
	@Override
	public IPatron getPatron() {
		return patron;
	}

	
	@Override
	public IBook getBook() {
		return book;
	}

	
	@Override
	public Date getDueDate() {
		return dueDate;
	}

	
	@Override
	public void commit(int loanId, Date dueDate) {
		if (state != LoanState.PENDING) {
			throw new RuntimeException("Loan : commit : Cannot commit a non PENDING loan");
		}
		if (loanId <= 0) {
			throw new RuntimeException("Loan : commit : loanId must be positive int");
		}
		if (dueDate == null) {
			throw new RuntimeException("Loan : commit : dueDate cannot be null");
		}
		if (!book.isAvailable()) {
			throw new RuntimeException("Loan : commit : book must be AVAILABLE");
		}		
		if (!patron.canBorrow()) {
			throw new RuntimeException("Loan : commit : patron must be CAN_BORROW");
		}
		
		this.state = LoanState.CURRENT;
		this.loanId = loanId;
		this.dueDate = dueDate;
				
		patron.takeOutLoan(this);
		book.borrowFromLibrary();		
		
	}

	
	@Override
	public void discharge(boolean isDamaged) {
		if (!(state == LoanState.CURRENT || state == LoanState.OVER_DUE)) {
			throw new RuntimeException("Cannot discharge a loand that is not CURRENT or OVERDUE");
		}
		state = LoanState.DISCHARGED;

		patron.dischargeLoan(this);
		book.returnToLibrary(isDamaged);
	}

	
	@Override
	public void updateOverDueStatus(Date currentDate) {
		if (state == LoanState.CURRENT && currentDate.after(dueDate)) {
			this.state = LoanState.OVER_DUE;
		}
	}


	@Override
	public boolean isPending() {
		return state == LoanState.PENDING;
	}

	
	@Override
	public boolean isCurrent() {
		return state == LoanState.CURRENT;
	}

	
	@Override
	public boolean isOverDue() {
		return state == LoanState.OVER_DUE;
	}

	
	@Override
	public boolean isDischarged() {
		return state == LoanState.DISCHARGED;
	}

	
	public String toString() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String dueDateString = sdf.format(dueDate);
		int bookId = book.getId();
		String bookTitle = book.getTitle();
		int patronId = patron.getId();
		String patronlastName = patron.getLastName();
		String patronFirstName = patron.getFirstName();

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Loan:  ").append(loanId).append("\n").append("  Borrower ").append(patronId).append(" : ")
				.append(patronlastName).append(", ").append(patronFirstName).append("\n").append("  Book ")
				.append(bookId).append(" : ").append(bookTitle).append("\n").append("  DueDate: ").append(dueDateString)
				.append("\n").append("  State: ").append(state);

		return stringBuilder.toString();
	}

}

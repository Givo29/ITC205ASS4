package library.entities;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Book implements Serializable, IBook {

	String title;
	String author;
	String callNumber;
	int id;

	BookState state;


	public Book(String author, String title, String callNo, int id, BookState state) { 
		if (author == null || title == null || callNo == null || state == null || id <= 0) {
			throw new RuntimeException("Book: invalid parameters for contructor");
		}
		this.author = author; 
		this.title = title; 
		this.callNumber = callNo;
		this.id = id; 
		this.state = state; 
		}


	public Book(String author, String title, String callNo, int id) {
		this(author, title, callNo, id, BookState.AVAILABLE);
	}

	
	@Override
	public int getId() {
		return id;
	}

	
	@Override
	public String getTitle() {
		return title;
	}

	
	@Override
	public boolean isAvailable() {
		return state == BookState.AVAILABLE;
	}

	
	@Override
	public boolean isOnLoan() {
		return state == BookState.ON_LOAN;
	}

	
	@Override
	public boolean isDamaged() {
		return state == BookState.DAMAGED;
	}

	
	@Override
	public void borrowFromLibrary() {
		if (!state.equals(BookState.AVAILABLE)) {
			throw new RuntimeException(String.format("Book: cannot borrow while book is in state: %s", state));
		} 
		state = BookState.ON_LOAN;
	}

	
	@Override
	public void returnToLibrary(boolean isDamaged) {
		if (!state.equals(BookState.ON_LOAN)) {
			throw new RuntimeException(String.format("Book: cannot Return while book is in state: %s", state));
		}
		if (isDamaged) {
			state = BookState.DAMAGED;
		}
		else {
			state = BookState.AVAILABLE;
		}
	}

	
	@Override
	public void repair() {
		if (!state.equals(BookState.DAMAGED)) {
			throw new RuntimeException(String.format("Book: cannot repair while book is in state: %s", state));
		}
		state = BookState.AVAILABLE;
	}

	
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Book: ").append(id).append("\n").append("  Title:  ").append(title).append("\n")
				.append("  Author: ").append(author).append("\n").append("  CallNo: ").append(callNumber).append("\n")
				.append("  State:  ").append(state);

		return stringBuilder.toString();
	}

}

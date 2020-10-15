package library.entities.helpers;

import library.entities.Book;
import library.entities.IBook;

public class BookHelper implements IBookHelper {

	private static final long serialVersionUID = 1L;

	@Override
	public IBook makeBook(String author, String title, String callNo, int id) {
		return new Book(author, title, callNo, id);
	}

}

package library.entities.helpers;

import java.io.Serializable;

import library.entities.IBook;

public interface IBookHelper extends Serializable {

	IBook makeBook(String author, String title, String callNo, int id);

}
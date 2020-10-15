package library;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import library.borrowbook.BorrowBookControl;
import library.borrowbook.BorrowBookUI;
import library.borrowbook.IBorrowBookControl;
import library.entities.IBook;
import library.entities.ICalendar;
import library.entities.ILibrary;
import library.entities.ILoan;
import library.entities.IPatron;
import library.entities.helpers.BookHelper;
import library.entities.helpers.CalendarFileHelper;
import library.entities.helpers.LibraryFileHelper;
import library.entities.helpers.LoanHelper;
import library.entities.helpers.PatronHelper;
import library.fixbook.FixBookControl;
import library.fixbook.FixBookUI;
import library.fixbook.IFixBookControl;
import library.payfine.IPayFineControl;
import library.payfine.PayFineControl;
import library.payfine.PayFineUI;
import library.returnbook.IReturnBookControl;
import library.returnbook.ReturnBookControl;
import library.returnbook.ReturnBookUI;

public class Main {

	private static Scanner scannerInput;
	private static ILibrary library;
	private static String menuString;
	private static ICalendar calendar;
	private static SimpleDateFormat dateFormat;

	private static LibraryFileHelper libraryHelper;
	private static CalendarFileHelper calendarHelper;

	private static String getMenuString() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("\nLibrary Main Menu\n\n").append("  M  : add patron\n").append("  LM : list patrons\n")
				.append("\n").append("  B  : add book\n").append("  LB : list books\n").append("  FB : fix books\n")
				.append("\n").append("  L  : take out a loan\n").append("  R  : return a loan\n")
				.append("  LL : list loans\n").append("\n").append("  P  : pay fine\n").append("\n")
				.append("  T  : increment date\n").append("  Q  : quit\n").append("\n").append("Choice : ");

		return stringBuilder.toString();
	}

	public static void main(String[] args) {
		try {
			libraryHelper = new LibraryFileHelper(new BookHelper(), new PatronHelper(), new LoanHelper());
			calendarHelper = new CalendarFileHelper();

			scannerInput = new Scanner(System.in);
			calendar = calendarHelper.loadCalendar();
			library = libraryHelper.loadLibrary();

			dateFormat = new SimpleDateFormat("dd/MM/yyyy");

			for (IPatron patron : library.getPatronList()) {
				output(patron);
			}
			output(" ");
			for (IBook book : library.getBookList()) {
				output(book);
			}

			menuString = getMenuString();

			boolean isDone = false;

			while (!isDone) {

				Date currentDate = calendar.getDate();
				String dateString = dateFormat.format(currentDate);
				output("\n" + dateString);
				String userInput = getUserInput(menuString);

				switch (userInput.toUpperCase()) {

				case "M":
					addPatron();
					libraryHelper.saveLibrary(library);
					break;

				case "LM":
					listPatrons();
					break;

				case "B":
					addBook();
					libraryHelper.saveLibrary(library);
					break;

				case "LB":
					listBooks();
					break;

				case "FB":
					fixBooks();
					libraryHelper.saveLibrary(library);
					break;

				case "L":
					borrowBook();
					libraryHelper.saveLibrary(library);
					break;

				case "R":
					returnBook();
					libraryHelper.saveLibrary(library);
					break;

				case "LL":
					listLoans();
					break;

				case "P":
					payFine();
					libraryHelper.saveLibrary(library);
					break;

				case "T":
					incrementDate();
					calendarHelper.saveCalendar();
					libraryHelper.saveLibrary(library);
					break;

				case "Q":
					isDone = true;
					break;

				default:
					output("\nInvalid option\n");
					break;
				}

			}
		} catch (RuntimeException e) {
			output(e);
		}
		output("\nEnded\n");
	}

	private static void payFine() {
		IPayFineControl payFineControl = new PayFineControl(library);
		new PayFineUI(payFineControl).run();
	}

	private static void listLoans() {
		output("");
		for (ILoan loan : library.getCurrentLoansList()) {
			output(loan + "\n");
		}
	}

	private static void listBooks() {
		output("");
		for (IBook book : library.getBookList()) {
			output(book + "\n");
		}
	}

	private static void listPatrons() {
		output("");
		for (IPatron patron : library.getPatronList()) {
			output(patron + "\n");
		}
	}

	private static void borrowBook() {
		IBorrowBookControl borrowBookControl = new BorrowBookControl(library);
		new BorrowBookUI(borrowBookControl).run();
	}

	private static void returnBook() {
		IReturnBookControl returnBookControl = new ReturnBookControl(library);
		new ReturnBookUI(returnBookControl).run();
	}

	private static void fixBooks() {
		IFixBookControl fixBookControl = new FixBookControl(library);
		new FixBookUI(fixBookControl).run();
	}

	private static void incrementDate() {
		try {
			String daysString = getUserInput("Enter number of days: ");
			int days = Integer.valueOf(daysString).intValue();
			calendar.incrementDate(days);
			library.checkCurrentLoansOverDue();
			Date currentDate = calendar.getDate();
			String dateString = dateFormat.format(currentDate);
			output(dateString);

		} catch (NumberFormatException e) {
			output("\nInvalid number of days\n");
		}
	}

	private static void addBook() {

		String author = getUserInput("Enter author: ");
		String title = getUserInput("Enter title: ");
		String callNumber = getUserInput("Enter call number: ");
		IBook book = library.addBook(author, title, callNumber);
		output("\n" + book + "\n");

	}

	private static void addPatron() {
		try {
			String lastName = getUserInput("Enter last name: ");
			String firstName = getUserInput("Enter first name: ");
			String email = getUserInput("Enter email: ");
			String phoneString = getUserInput("Enter phone number: ");
			int phoneInt = Integer.valueOf(phoneString).intValue();
			IPatron patron = library.addPatron(lastName, firstName, email, phoneInt);
			output("\n" + patron + "\n");

		} catch (NumberFormatException e) {
			output("\nInvalid phone number\n");
		}

	}

	private static String getUserInput(String prompt) {
		System.out.print(prompt);
		return scannerInput.nextLine();
	}

	private static void output(Object object) {
		System.out.println(object);
	}

}

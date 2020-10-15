package library.entities.helpers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import library.entities.ILibrary;
import library.entities.Library;

public class LibraryFileHelper implements ILibraryHelper {

	private static final long serialVersionUID = 1L;

	public static final String LIBRARY_FILE = "library.obj";

	ILibrary library;
	IBookHelper bookHelper;
	IPatronHelper patronHelper;
	ILoanHelper loanHelper;

	public LibraryFileHelper(IBookHelper bookHelper, IPatronHelper patronHelper, ILoanHelper loanHelper) {
		this.bookHelper = bookHelper;
		this.patronHelper = patronHelper;
		this.loanHelper = loanHelper;
	}

	@Override
	public ILibrary loadLibrary() {
		Path path = Paths.get(LIBRARY_FILE);
		if (Files.exists(path)) {
			try (ObjectInputStream lof = new ObjectInputStream(new FileInputStream(LIBRARY_FILE));) {
				library = (Library) lof.readObject();
				lof.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			library = makeLibrary(bookHelper, patronHelper, loanHelper);
		}
		return library;
	}

	@Override
	public void saveLibrary(ILibrary library) {
		if (library != null) {
			try (ObjectOutputStream lof = new ObjectOutputStream(new FileOutputStream(LIBRARY_FILE));) {
				lof.writeObject(library);
				lof.flush();
				lof.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

	@Override
	public ILibrary makeLibrary(IBookHelper bookHelper, IPatronHelper patronHelper, ILoanHelper loanHelper) {
		return new Library(bookHelper, patronHelper, loanHelper);
	}

}

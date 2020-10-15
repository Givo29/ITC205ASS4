package library.entities.helpers;

import java.io.Serializable;

import library.entities.ILibrary;

public interface ILibraryHelper extends Serializable {

	ILibrary makeLibrary(IBookHelper bookHelper, IPatronHelper patronHelper, ILoanHelper loanHelper);

	public ILibrary loadLibrary();

	public void saveLibrary(ILibrary library);

}

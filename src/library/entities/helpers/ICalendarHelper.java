package library.entities.helpers;

import java.io.Serializable;

import library.entities.ICalendar;

public interface ICalendarHelper extends Serializable {

	public ICalendar loadCalendar();

	public void saveCalendar();

}

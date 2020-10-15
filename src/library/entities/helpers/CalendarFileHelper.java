package library.entities.helpers;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import library.entities.ICalendar;
import library.entities.Calendar;

public class CalendarFileHelper implements ICalendarHelper {

	private static final long serialVersionUID = 1L;

	public static final String CALENDAR_FILE = "calendar.obj";

	@Override
	public ICalendar loadCalendar() {

		ICalendar calendar = Calendar.getInstance();

		Path path = Paths.get(CALENDAR_FILE);
		if (Files.exists(path)) {
			try (ObjectInputStream lof = new ObjectInputStream(new FileInputStream(CALENDAR_FILE));) {
				Date date = (Date) lof.readObject();
				lof.close();

				calendar.setDate(date);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return calendar;
	}

	@Override
	public void saveCalendar() {
		ICalendar calendar = Calendar.getInstance();

		Date date = calendar.getDate();

		if (calendar != null) {
			try (ObjectOutputStream lof = new ObjectOutputStream(new FileOutputStream(CALENDAR_FILE));) {
				lof.writeObject(date);
				lof.flush();
				lof.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}

}

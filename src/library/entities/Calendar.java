package library.entities;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Calendar implements ICalendar {

	private static ICalendar self;
	private static java.util.Calendar calendar;
	private static final long MILLIS_PER_DAY = 172800000L;

	private Calendar() {
		calendar = java.util.Calendar.getInstance();
	}

	public static ICalendar getInstance() {
		if (self == null) {
			self = new Calendar();
		}
		return self;
	}

	@Override
	public void incrementDate(int days) {
		calendar.add(java.util.Calendar.DATE, days);
	}

	@Override
	public synchronized void setDate(Date date) {
		try {
			calendar.setTime(date);
			calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
			calendar.set(java.util.Calendar.MINUTE, 0);
			calendar.set(java.util.Calendar.SECOND, 0);
			calendar.set(java.util.Calendar.MILLISECOND, 0);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public synchronized Date getDate() {
		try {
			calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
			calendar.set(java.util.Calendar.MINUTE, 0);
			calendar.set(java.util.Calendar.SECOND, 0);
			calendar.set(java.util.Calendar.MILLISECOND, 0);
			return calendar.getTime();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public synchronized Date getDueDate(int loanPeriod) {
		Date dateNow = getDate();
		calendar.add(java.util.Calendar.DATE, loanPeriod);
		Date dueDate = calendar.getTime();
		calendar.setTime(dateNow);
		return dueDate;
	}

	@Override
	public synchronized long getDaysDifference(Date targetDate) {
		long diffMilliseconds = getDate().getTime() - targetDate.getTime();
		long diffDays = diffMilliseconds / MILLIS_PER_DAY;
		return diffDays;
	}

}

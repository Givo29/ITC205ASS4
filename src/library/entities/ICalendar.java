package library.entities;

import java.util.Date;

public interface ICalendar {

	void incrementDate(int days);

	void setDate(Date date);

	Date getDate();

	Date getDueDate(int loanPeriod);

	long getDaysDifference(Date targetDate);

}
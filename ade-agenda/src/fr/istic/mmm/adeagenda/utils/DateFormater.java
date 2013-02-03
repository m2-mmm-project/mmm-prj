package fr.istic.mmm.adeagenda.utils;

import java.util.Calendar;

import android.text.format.DateFormat;

public class DateFormater {

	public static String getURLString(int year, int month, int day) {
		return (String) DateFormat.format("yyyy-MM-dd",
				getCalendarFromInt(year, month, day));
	}

	public static String getDisplayString(int year, int month, int day) {
		return (String) DateFormat.format("d MMMM yyyy",
				getCalendarFromInt(year, month, day));
	}
	
	public static final String getDayString(Calendar cal) {
		return (String) DateFormat.format("EEEE", cal);
	}

	private static Calendar getCalendarFromInt(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
    	c.set(year, month, day);
		return c;
	}
}

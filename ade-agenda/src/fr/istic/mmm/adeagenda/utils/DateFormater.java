package fr.istic.mmm.adeagenda.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.text.format.DateFormat;

public class DateFormater {

	public static String dateToURLString(int year, int month, int day) {
		return (String) DateFormat.format("yyyy-MM-dd",
				getDateFromInt(year, month, day));
	}

	public static String dateToDisplayString(int year, int month, int day) {
		return (String) DateFormat.format("d MMMM yyyy",
				getDateFromInt(year, month, day));
	}
	
	public static final String getDayString(Calendar cal) {
		return (String) DateFormat.format("EEEE", cal);
	}

	private static Date getDateFromInt(int year, int month, int day) {
		// Janvier = 1
		month++;
		
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-M-d", Locale.FRANCE).parse(
					year + "-" + month + "-" + day);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}

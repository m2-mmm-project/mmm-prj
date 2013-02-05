package fr.istic.mmm.adeagenda.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
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
	
	public static String getDayString(Calendar cal) {
		return (String) DateFormat.format("EEEE", cal);
	}

	public static String getSQLDayString(Date day) {
		return (String) DateFormat.format("yyyy-MM-dd", day);
	}
	
	public static String getSQLDateString(Date day) {
		return (String) DateFormat.format("yyyy-MM-dd kk:mm", day);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static Date getSQLDate(String sqlDate) {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd kk:mm").parse(sqlDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return date;
	}
	
	private static Calendar getCalendarFromInt(int year, int month, int day) {
		Calendar c = Calendar.getInstance();
    	c.set(year, month, day);
		return c;
	}
}

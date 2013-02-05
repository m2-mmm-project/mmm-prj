package fr.istic.mmm.adeagenda.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.text.format.DateFormat;

public class DateFormater {

	public static String getDateURLString(int year, int month, int day) {
		return (String) DateFormat.format("yyyy-MM-dd",
				getCalendarFromInt(year, month, day));
	}

	public static String getDateDisplayString(int year, int month, int day) {
		return (String) DateFormat.format("d MMMM yyyy",
				getCalendarFromInt(year, month, day));
	}
	
	public static String getDayDisplayString(Calendar cal) {
		return (String) DateFormat.format("EEEE", cal);
	}

	public static String getDateSQLString(Date day) {
		return (String) DateFormat.format("yyyy-MM-dd", day);
	}
	
	public static String getDateTimeSQLString(Date day) {
		return (String) DateFormat.format("yyyy-MM-dd kk:mm", day);
	}
	
	public static String getDateString(Date date) {
		return (String) DateFormat.format("dd/MM/yy", date);
	}

	public static Object getTimeString(Date time) {
		return (String) DateFormat.format("kk:mm", time);
	}
	
	@SuppressLint("SimpleDateFormat")
	public static Date getDateTimeSQL(String sqlDate) {
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

package fr.istic.mmm.adeagenda.utils;

import java.util.Date;

public class DateFormater {

	public static String dateToString(int day, int month, int year) {

		String date_str = year + "-";

		if (month < 10) {
			date_str += "0" + month + "-";
		} else {
			date_str += month + "-";
		}

		if (day < 10) {
			date_str += "0" + day + "-";
		} else {
			date_str += day + "-";
		}
		return date_str;
	}
	
	public static String dateToString(Date date) {
		return date.toString();
	}
}

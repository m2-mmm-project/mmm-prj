package fr.istic.mmm.adeagenda.utils;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import fr.istic.mmm.adeagenda.R;

public class DateFormater {

	public static String dateToURLString(int year, int month, int day) {

		String date_str = year + "-";

		if (month < Calendar.OCTOBER) {
			date_str += "0" + month + "-";
		} else {
			date_str += month + "-";
		}

		if (day < 10) {
			date_str += "0" + day;
		} else {
			date_str += day;
		}

		return date_str;
	}

	public static String dateToDisplayString(Context context, int year, int month, int day) {

		return day
				+ " "
				+ context.getResources().getStringArray(R.array.french_month)[month]
				+ " " + year;
	}

	public static String dateToString(Date date) {
		return date.toString();
	}
}

package com.proximapp.worktime.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatetimeManager {

	private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	private static final SimpleDateFormat sdformat = new SimpleDateFormat(DATETIME_FORMAT);

	public static String format(Date date) {
		return sdformat.format(date);
	}

	public static Date parse(String text) throws ParseException {
		return sdformat.parse(text);
	}

	public static Date parseSafe(String text) {
		try {
			return DatetimeManager.parse(text);
		} catch (ParseException e) {
			return null;
		}
	}

}

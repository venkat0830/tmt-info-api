package com.example.mongodb.test.work;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class LocalDtTime {
	public static boolean isValidUnixDate(String unixDateStr) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date(Long.valueOf(unixDateStr));
			String dateStr = sdf.format(date);
			Date currentDate = new Date();
			Timestamp ts = Timestamp.valueOf(dateStr);
			Timestamp ts1 = Timestamp.valueOf(sdf.format(currentDate));
			sdf.setLenient(false);
			if (ts.before(ts1)) {
				System.out.println("Test done");
				return true;
			}

		} catch (NumberFormatException ex) {
			return false;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static String convertUnixDate(String unixDateStr) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date(Long.valueOf(unixDateStr));
			String dateStr = sdf.format(date);
			return dateStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getLastUpdatedDate(String recordType, boolean isUnixTimeStamp) {
		TimeZone cstTimeZone = null;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat();
		if (Boolean.FALSE.equals(isUnixTimeStamp)) {
			cstTimeZone = TimeZone.getTimeZone("CST6CDT");
			calendar = Calendar.getInstance(cstTimeZone);
			sdf = new SimpleDateFormat("CST6CDT", Locale.US);
			sdf.setTimeZone(cstTimeZone);
		}
		try {
			if (recordType.equals("RECON") || recordType.equals("PEND")) {
				calendar.add(Calendar.DATE, -14);
			}
			if (recordType.equals("HPC")) {
				calendar.add(Calendar.DATE, -30);
			}
		} catch (Exception ex) {
			System.out.println("exception encountered:" + ex.getMessage());
		}
		if (Boolean.TRUE.equals(isUnixTimeStamp)) {
			long timeStamp = calendar.getTimeInMillis();
			return Long.toString(timeStamp);
		}
		Date dtTime = calendar.getTime();
		return sdf.format(dtTime);
	}

	public static String getTodaysDate(boolean isUnixTimeStamp) {
		TimeZone cstTimeZone = null;
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat();
		if (Boolean.FALSE.equals(isUnixTimeStamp)) {
			cstTimeZone = TimeZone.getTimeZone("CST6CDT");
			calendar = Calendar.getInstance(cstTimeZone);
			sdf = new SimpleDateFormat("CST6CDT", Locale.US);
			sdf.setTimeZone(cstTimeZone);
		}
		if (Boolean.TRUE.equals(isUnixTimeStamp)) {
			long timeStamp = calendar.getTimeInMillis();
			return Long.toString(timeStamp);
		}
		Date dtTime = calendar.getTime();
		return sdf.format(dtTime);
	}

	public static String getPredDate() {

		String date;
		TimeZone cstTimeZone = TimeZone.getTimeZone("CST6CDT");
		Calendar calendar = Calendar.getInstance(cstTimeZone);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
		sdf.setTimeZone(cstTimeZone);
		Date dtTime = calendar.getTime();
		date = sdf.format(dtTime);
		return date;

	}

}

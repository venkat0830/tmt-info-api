package com.example.mongodb.test.work;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

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

}

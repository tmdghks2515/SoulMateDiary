package com.anan.Soulmate.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {
	
	public static String dateDiff(String date) {
		String result = "";
		Date today = new Date();
		try {
			SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date comment_date = format1.parse(date);
			long diff = today.getTime() - comment_date.getTime();
			long sec = diff / 1000;
			if (sec < 60)
				result = sec + "초 전";
			else if (sec < 60 * 60)
				result = sec / 60 + "분 전";
			else if (sec < 60 * 60 * 24)
				result = sec / (60 * 60) + "시간 전";
			else if (sec < 60 * 60 * 24 * 30)
				result = sec / (60 * 60 * 24) + "일 전";
			else if (sec < 60 * 60 * 24 * 30 * 365)
				result = sec / (60 * 60 * 24 * 30) + "달 전";
			else
				result = sec / (60 * 60 * 24 * 30 * 365) + "년 전";
		} catch (Exception e) {}

		return result;
	}
	
	public static String anniDateDiff(String dateStr) {
		Date now = new Date();
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long diff = now.getTime() - date.getTime();
		int dateDiff = (int) (diff/(1000*60*60*24));
		
		String result = "";
		if(dateDiff > 0)
			result = "D+" + dateDiff; 
		else if(dateDiff < 0)
			result = "D-"+(-dateDiff);
		else
			result = "D-day !!!";
		
		return result;
	}
	
}

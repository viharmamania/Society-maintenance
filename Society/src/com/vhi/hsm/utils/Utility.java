package com.vhi.hsm.utils;

import java.text.DateFormatSymbols;

public class Utility {
	
	private static DateFormatSymbols dateFormatSymbols;
	
	public static String getMonthNameFromNumber(int number) {
		if (number < 0 || number > 11)
			return "";
		
		if (dateFormatSymbols == null) {
			dateFormatSymbols = new DateFormatSymbols();
		}
		
		return dateFormatSymbols.getMonths()[number];
		
	}
	
}

package com.harunergul.permission.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

	
	public static List<Date> getDates(Date from, Date to){
	
		List<Date> dates = new ArrayList<Date>();
		if(from==null || to==null || to.before(from)) {
			return null;
		}else {
			Date loopInvariant = (Date) from.clone();
			Calendar cal = Calendar.getInstance();
			cal.setTime(loopInvariant);
			
			while(loopInvariant.before(to)) {
				dates.add(loopInvariant);
				cal.add(Calendar.DATE, 1);
				loopInvariant = cal.getTime();
			}
		}
		
		return dates;
		
	}
}

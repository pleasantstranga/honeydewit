
package com.honeydewit.calendar;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
	public static SimpleDateFormat tempFileNameFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.US);
	public static SimpleDateFormat monthDayYear = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
	public static SimpleDateFormat monthYear = new SimpleDateFormat("MMMM yyyy", Locale.US);
	public static SimpleDateFormat monthAbbrev = new SimpleDateFormat("MMM", Locale.US);
	public static SimpleDateFormat dayAbbrev = new SimpleDateFormat("EEE", Locale.US);
	public static SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MM/dd/yyyy", Locale.US);  
	public static SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
	public static SimpleDateFormat dateTimeFormat = new SimpleDateFormat("EEE MM/dd/yyyy hh:mm a", Locale.US);
	public static SimpleDateFormat exdateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'", Locale.US);
	public static SimpleDateFormat dayOfWeek = new SimpleDateFormat("EEEE", Locale.US);
	public static String[] daysInWeek = new String[]{"SU","MO","TU","WE","TH","FR","SA"};
	public static String[] weekdays = new String[] {"MO","TU","WE","TH","FR"};
	public static String[] weekendDays = new String[] {"SA","SU"};
	public static String[] monthsInYear = new String[]{"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	public static String[] daysInMonth = new String[] {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
	
	/**
	 * Converts mm/dd/yyyy to rrule string
	 * @param date The date as a string
	 * @return
	 */
	public static String convertDateToRRULE(String date) {
		String[] array = date.split("/");
		
		return array[2] + array[1] + array[0];
	}
	public static boolean isValidDate(String date) {
		try {
			monthDayYear.parse(date);
			return true;
		} 
		catch (ParseException e) {
			// TODO Auto-generated catch block
			return false;
		}
	   	     
	}
	/**
	 * Returns the same date with a time of 23:99
	 * @return
	 */
	public static GregorianCalendar getEndOfDay(GregorianCalendar day) {
		GregorianCalendar cal = (GregorianCalendar)day.clone();
		cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE,      cal.getMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND,      cal.getMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
		return cal;    
	}
	/**
	 * Returns the same date with a time of 00:00
	 * @return
	 */
	public static GregorianCalendar getBeginningOfDay(GregorianCalendar day) {
		GregorianCalendar cal = (GregorianCalendar)day.clone();
		cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE,      cal.getMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND,      cal.getMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
		return cal;

	}
	public static GregorianCalendar getBeginningOfMonth(GregorianCalendar day) {
		GregorianCalendar cal = (GregorianCalendar)day.clone();
		cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE,      cal.getMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND,      cal.getMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
		return cal;
	}
	public static GregorianCalendar getEndOfMonth(GregorianCalendar day) {
		GregorianCalendar cal = (GregorianCalendar)day.clone();
		cal.set(Calendar.DAY_OF_MONTH, cal.getMaximum(Calendar.DAY_OF_MONTH));
		cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE,      cal.getMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND,      cal.getMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
		return cal;    
	}
	/**
	 * Takes a date and adds months to it and returns the first of that month.
	 * @param day
	 * @param numMonthsFromDate
	 * @return
	 */
	public static GregorianCalendar getBeginningOfMonth(GregorianCalendar day, int numMonthsFromDate) {
		GregorianCalendar cal = (GregorianCalendar) day.clone();
		cal.add(Calendar.MONTH, numMonthsFromDate);
		cal = getBeginningOfMonth(cal);
		cal = getBeginningOfDay(cal);
		return cal;
	}
	/**
	 * Returns true if the current date's month and year are the same as the date passed in.
	 * @param date
	 * @return boolean
	 */
	public static boolean isMonthYearEqual(GregorianCalendar date1, GregorianCalendar date2) {
		boolean isCurrent = false;
		
		if(date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)) {
			if(date1.get(Calendar.MONTH) == date2.get(Calendar.MONTH)) {
				isCurrent = true;
			}
		}
		return isCurrent;
		
	}
	public static boolean isCurrentDate(int day, int month, int year) {
		GregorianCalendar cal = new GregorianCalendar();
		if(cal.get(Calendar.DAY_OF_MONTH) == day) {
			if(cal.get(Calendar.MONTH) == month)  {
				if(cal.get(Calendar.YEAR) == year)  {
					return true;
				}
			}
		}
		return false;
	}
	public static GregorianCalendar getCalendarFromString(String date) {
		String[] dateValues = date.split("/");		
		GregorianCalendar cal = new GregorianCalendar();
		cal.set(Calendar.MONTH, Integer.valueOf(dateValues[0])-1);
		cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(dateValues[1]));
		cal.set(Calendar.YEAR, Integer.valueOf(dateValues[2]));
		return getBeginningOfDay(cal);
	}
	public static GregorianCalendar setCurrentHourDate(GregorianCalendar date) {
		date.set(Calendar.HOUR_OF_DAY, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
		return date;
		
	}
	public static String getUTCDateTime(Calendar origDate) {
		// This prints: Date with default formatter: 2013-03-14 22:00:12 PDT
	    // As my machine is in PDT time zone
		Log.d("Date with default formatter: ", DateUtil.exdateFormat.format(origDate.getTime()));
	    SimpleDateFormat sdf = (SimpleDateFormat)DateUtil.exdateFormat.clone();
	    // This prints: Date with IST time zone formatter: 2013-03-15 10:30:12 GMT+05:30
	   
	    // This prints: Date CST time zone formatter: 2013-03-15 00:00:12 CDT        
	    TimeZone tz = TimeZone.getTimeZone("UTC");
	    sdf.setTimeZone(tz);
	    Log.d("Date UTC time zone formatter: ", sdf.format(origDate.getTime()));
	    return sdf.format(origDate.getTime());
	}
	/**
	 * Converts date to UTC and creates an exdate string
	 * @param originalDateFrom
	 * @return
	 */
	public static String getUTCExDateString(Calendar dateFrom, String origExDate) {
		origExDate = origExDate == null ? "" : origExDate.concat(",");
		String utcDate =  DateUtil.getUTCDateTime(dateFrom);
		return origExDate.concat(utcDate);
	}
	public static String getNextMonthAbbreviated(Calendar currentMonth) {
		Calendar nextMonth = (Calendar)currentMonth.clone();
		nextMonth.add(Calendar.MONTH, 1);
		return DateUtil.monthAbbrev.format(nextMonth.getTime());
	}
	public static String getPreviousMonthAbbreviated(Calendar currentMonth) {
		Calendar previousMonth = (Calendar)currentMonth.clone();
		previousMonth.add(Calendar.MONTH, -1);
		return DateUtil.monthAbbrev.format(previousMonth.getTime());
	}
	public static String getNextDayAbbreviated(Calendar currentDate) {
		Calendar nextDay = (Calendar)currentDate.clone();
		nextDay.add(Calendar.DAY_OF_MONTH, 1);
		return DateUtil.dayAbbrev.format(nextDay.getTime());
	}
	public static String getPreviousDayAbbreviated(Calendar currentDate) {
		Calendar previousDay = (Calendar)currentDate.clone();
		previousDay.add(Calendar.DAY_OF_MONTH, -1);
		return DateUtil.dayAbbrev.format(previousDay.getTime());
	}
}

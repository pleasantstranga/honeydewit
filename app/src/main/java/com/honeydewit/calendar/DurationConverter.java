package com.honeydewit.calendar;

/**
 * import org.joda.time.Interval;
 * import org.joda.time.PeriodType;
 */

 
public class DurationConverter {
 /**
	public static String convert(Calendar dateStart, Calendar dateEnd) {
 
		//HH converts hour in 24 hours format (0-23), day calculation
		Calendar dateStartClone = (Calendar)dateStart.clone();
		StringBuffer duration = new StringBuffer("P");
		try {
			Interval interval = new Interval(dateStartClone.getTimeInMillis(), dateEnd.getTimeInMillis());
			
			int diffWeeks = interval.toPeriod(PeriodType.weeks()).getWeeks();
			if(diffWeeks > 0 || diffWeeks < 0) {
				duration.append(diffWeeks).append("W");
				dateStartClone.add(Calendar.DATE, 7*diffWeeks);
				interval = new Interval(dateStartClone.getTimeInMillis(), dateEnd.getTimeInMillis());
				
			}
			int diffDays = interval.toPeriod(PeriodType.days()).getDays();
			if(diffDays > 0 || diffDays < 0) {
				duration.append(diffDays).append("D");
				dateStartClone.add(Calendar.DATE, diffDays);
				interval = new Interval(dateStartClone.getTimeInMillis(), dateEnd.getTimeInMillis());
			}
			int diffHours = interval.toPeriod(PeriodType.hours()).getHours();
			if(diffHours > 0 || diffHours < 0) {
				duration.append(diffHours).append("H");
				dateStartClone.add(Calendar.HOUR, diffHours);
				interval = new Interval(dateStartClone.getTimeInMillis(), dateEnd.getTimeInMillis());
				
			}
			int diffMinutes = interval.toPeriod(PeriodType.minutes()).getMinutes();
			if(diffMinutes >0 || diffMinutes < 0) {
				duration.append(diffMinutes).append("M");
			}
			
 
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d("Duration", duration.toString());
		return duration.toString();
	}
 **/
}
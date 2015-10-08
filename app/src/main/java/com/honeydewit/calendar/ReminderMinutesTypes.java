package com.honeydewit.calendar;

import java.util.ArrayList;
import java.util.List;

public enum ReminderMinutesTypes {
     ON_TIME(0,"On Time"),
     FIVE_MINUTES_BEFORE(5, "5 Minutes Before"),
     FIFTEEN_MINUTES_BEFORE(15,"15 Minutes Before"),
     THIRTY_MINUTES_BEFORE(30, "30 Minutes Before"),
     ONE_HOUR_BEFORE(60, "1 Hour Before"),
     TWO_HOUR_BEFORE(120, "1 Hour Before"),
     ONE_DAY_BEFORE(1800, "1 Day Before"),
     TWO_DAYS_BEFORE(3600, "2 Days Before"),
     ONE_WEEK_BEFORE(12600, "1 Week Before"),
     CUSTOM(-1, "Custom");
   
    
    private Integer minutes;
    private String title;
    
	ReminderMinutesTypes(Integer minutes, String title) {
    	this.minutes = minutes;
    	this.title = title;
    }
    public static List<String> getReminderMinutesTypes() {
    	List<String> types = new ArrayList<String>();
    	for(ReminderMinutesTypes repeatType : ReminderMinutesTypes.values()) {
    		types.add(repeatType.getTitle());
    	}
    	return types;
    }
  
	public Integer getMinutes() {
		return minutes;
	}

	public String getTitle() {
		return title;
	}
	public static Integer getMinutesByTitle(String title) {
		int minutes = 0;
		for(ReminderMinutesTypes reminderType : ReminderMinutesTypes.values()) {
			if(reminderType.getTitle().equals(title)) {
				minutes = reminderType.getMinutes();
				break;
			}
		}
		return minutes;
	}
	public static String getTitleByCode(Integer minutes) {
		String title = CUSTOM.getTitle();
		for(ReminderMinutesTypes reminderType : ReminderMinutesTypes.values()) {
			if(reminderType.getMinutes() == minutes) {
				title = reminderType.getTitle();
				break;
			}
		}
		return title;
	}
}

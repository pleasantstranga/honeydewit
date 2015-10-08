package com.honeydewit.calendar;

import android.provider.CalendarContract.Reminders;

import java.util.ArrayList;
import java.util.List;

public enum ReminderMethodTypes {
	METHOD_DEFAULT(Reminders.METHOD_DEFAULT, "Default"),
	METHOD_ALARM(Reminders.METHOD_ALARM, "Alarm"),
	METHOD_ALERT(Reminders.METHOD_ALERT, "Alert"),	
	METHOD_EMAIL(Reminders.METHOD_EMAIL, "Email"),
	METHOD_SMS (Reminders.METHOD_SMS, "SMS");
	
    private int code;
    private String title;
    
	ReminderMethodTypes(int code, String title) {
    	this.code = code;
    	this.title = title;
    }
    public static List<String> getTitles() {
    	List<String> types = new ArrayList<String>();
    	for(ReminderMethodTypes repeatMethodType : ReminderMethodTypes.values()) {
    		types.add(repeatMethodType.getTitle());
    	}
    	return types;
    }
    public static int getCodeByTitle(String title) {
    	int code = METHOD_DEFAULT.getCode();
    	for(ReminderMethodTypes repeatMethodType : ReminderMethodTypes.values()) {
    		if(repeatMethodType.getTitle().equals(title)) {
    			code = repeatMethodType.getCode();
    		}
    	}
    	return code;
    }
    public static String getTitleByCode(Integer code) {
		String title = METHOD_DEFAULT.getTitle();
		for(ReminderMethodTypes methodType : ReminderMethodTypes.values()) {
			if(methodType.getCode() == code) {
				title = methodType.getTitle();
				break;
			}
		}
		return title;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
}

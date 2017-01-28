package com.ajbtechnologies.calendar;

import java.util.ArrayList;
import java.util.List;

public enum RepeatTypes {
    NONE(0, "None"),
    DAILY(1, "Daily"),
    WEEKLY(2, "Weekly"),
    EVERY_TWO_WEEKS(3, "Every Other Week"),
    EVERY_THREE_WEEKS(4, "Every Three Weeks"),
    MONTHLY(5, "Monthly"),
    YEARLY(6, "Yearly"),
    CUSTOM(7, "Custom");
    
    private int code;
    private String eventTitle;
    
	RepeatTypes(int code, String eventTitle) {
    	this.code = code;
    	this.eventTitle = eventTitle;
    }
    public static List<String> getRepeatTypes() {
    	List<String> types = new ArrayList<String>();
    	for(RepeatTypes repeatType : RepeatTypes.values()) {
    		types.add(repeatType.getEventTitle());
    	}
    	return types;
    }
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getEventTitle() {
		return eventTitle;
	}
	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}
}

package com.ajbtechnologies;

import com.ajbtechnologies.calendar.Event;
import com.ajbtechnologies.pojos.BaseObject;


public class Reminder extends BaseObject{

	private static final long serialVersionUID = 1L;
	private Integer minutes;
	private Integer method;
	private Event event;
	
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public Integer getMinutes() {
		return minutes;
	}
	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}
	
	public Integer getMethod() {
		return method;
	}
	public void setMethod(Integer method) {
		this.method = method;
	}
}

package com.honeydewit.calendar;

import java.io.Serializable;
import java.util.Calendar;

public class EventInstance implements Serializable {
	private static final long serialVersionUID = 1L;
	private Event event;
	private Integer instanceId;
	private Calendar dateFrom;
	private Calendar dateTo;
	
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public Integer getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(Integer instanceId) {
		this.instanceId = instanceId;
	}
	public Calendar getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(Calendar dateFrom) {
		this.dateFrom = dateFrom;
	}
	public Calendar getDateTo() {
		return dateTo;
	}
	public void setDateTo(Calendar dateTo) {
		this.dateTo = dateTo;
	}
}

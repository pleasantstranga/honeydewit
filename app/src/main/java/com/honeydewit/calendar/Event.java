package com.honeydewit.calendar;

import com.honeydewit.Reminder;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

public class Event implements Serializable {
	
	private static final long serialVersionUID = -8393984795026406585L;
	private Integer calendarId;
	private Integer eventId;
	private Calendar dateFrom;
	private Calendar dateTo;
	private String title;
	private String eventTimezone;
	private String rdate;
	private RecurrenceRule rrule;
	private boolean isAllDay;
	private String duration;
	private List<Reminder> reminders;
	private String exDate = null;
	private Integer originalId;
	
	public Integer getOriginalId() {
		return originalId;
	}
	public void setOriginalId(Integer originalId) {
		this.originalId = originalId;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public Integer getCalendarId() {
		return calendarId;
	}
	public void setCalendarId(Integer calendarId) {
		this.calendarId = calendarId;
	}
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEventTimezone() {
		return eventTimezone;
	}
	public void setEventTimezone(String eventTimezone) {
		this.eventTimezone = eventTimezone;
	}
	
	public String getRdate() {
		return rdate;
	}
	public void setRdate(String rdate) {
		this.rdate = rdate;
	}
	public RecurrenceRule getRrule() {
		return rrule;
	}
	public void setRrule(RecurrenceRule rrule) {
		this.rrule = rrule;
	}
	public boolean isAllDay() {
		return isAllDay;
	}
	public void setAllDay(boolean isAllDay) {
		this.isAllDay = isAllDay;
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
	public String getExDate() {
		return exDate;
	}
	public void setExDate(String exDate) {
		this.exDate = exDate;
	}
	public List<Reminder> getReminders() {
		return reminders;
	}
	public void setReminders(List<Reminder> reminders) {
		this.reminders = reminders;
	}
}

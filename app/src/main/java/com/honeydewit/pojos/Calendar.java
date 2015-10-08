package com.honeydewit.pojos;

public class Calendar {
	private Integer id;
	private String accountName;
	private String calendarName;
	
	public Calendar(Integer id, String accountName, String calendarName) {
		this.id = id;
		this.accountName = accountName;
		this.calendarName = calendarName;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getCalendarName() {
		return calendarName;
	}
	public void setCalendarName(String calendarName) {
		this.calendarName = calendarName;
	}
	
}

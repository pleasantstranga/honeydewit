package com.ajbtechnologies.pojos;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "LIST_EVENTS")
public class ListEvent extends BaseObject {
	
	private static final long serialVersionUID = 1L;
	public static final String CALENDAR_CLMN = "CALENDAR_ID";
	public static final String EVENT_CLMN = "EVENT_ID";
	public static final String LIST_CLMN = "LIST_ID";
	
	@DatabaseField(columnName = EVENT_CLMN)
	private Integer eventId;
	@DatabaseField(columnName = CALENDAR_CLMN)
	private Integer calendarId;
	@DatabaseField(foreign = true, columnName = LIST_CLMN)
	private BasicList list;
	
	public ListEvent() {
		
	}
	public ListEvent(int calendarId, Integer eventId, BasicList list) {
		this.calendarId = calendarId;
		this.eventId = eventId;
		this.list = list;
	}
	
	public Integer getEventId() {
		return eventId;
	}
	public void setEventId(Integer eventId) {
		this.eventId = eventId;
	}
	public Integer getCalendarId() {
		return calendarId;
	}
	public void setCalendarId(Integer calendarId) {
		this.calendarId = calendarId;
	}
	public BasicList getList() {
		return list;
	}
	public void setList(BasicList list) {
		this.list = list;
	}
}

package com.honeydewit.calendar;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Instances;
import android.provider.CalendarContract.Reminders;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.honeydewit.Reminder;
import com.honeydewit.pojos.Calendar;
import com.honeydewit.pojos.ListEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CalendarDbHelper {	
	
	public boolean isListEventDeleted(Context context, ListEvent listEvent) {
		
		boolean isDeleted = true;
		
		final String[] EVENT_PROJECTION = new String[] {
			CalendarContract.Events._ID, 
		};

		// Run query
		Cursor cur = null;
		ContentResolver cr = context.getContentResolver();
		Uri uri = CalendarContract.Events.CONTENT_URI;   
		String selection = "((" + CalendarContract.Events.CALENDAR_ID + " = ?) AND (" 
		                        + CalendarContract.Events._ID + " = ?))";
		String[] selectionArgs = new String[] {String.valueOf(listEvent.getCalendarId()),String.valueOf(listEvent.getEventId())}; 
		// Submit the query and get a Cursor object back. 
		try {
			cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
			if(cur.getCount() >0) {
				isDeleted = false;
			}	
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(cur != null)
				cur.close();
		}
				
		return isDeleted;
	}
	public SparseIntArray getEventsInstancesCountByDateRangeMap(Context context, Integer calendarId, GregorianCalendar start, GregorianCalendar end) {
		SparseIntArray events = new SparseIntArray();
		// Projection array. Creating indices for this array instead of doing
		// dynamic lookups improves performance.
		final String[] EVENT_PROJECTION = new String[] {
			CalendarContract.Instances.BEGIN
		};

		// Run query
		Cursor cur = null;
		ContentResolver cr = context.getContentResolver();
		
		
		Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon(); 
		
		ContentUris.appendId(builder, start.getTimeInMillis());
		ContentUris.appendId(builder, end.getTimeInMillis()); 
		Uri uri = builder.build();
		// Submit the query and get a Cursor object back. =
		try {
			cur = cr.query(uri, EVENT_PROJECTION, CalendarContract.Instances.CALENDAR_ID + "=" + 2, null, null);
			
			while (cur.moveToNext()) {
				GregorianCalendar cal = new GregorianCalendar();
				cal.setTimeInMillis(Long.valueOf(cur.getLong(0)));
				int dayOfMonth = cal.get(java.util.Calendar.DAY_OF_MONTH);
				if(events.indexOfKey(dayOfMonth) < 0) {
					events.put(dayOfMonth, 0);
				}
				events.put(dayOfMonth, events.get(dayOfMonth)+1);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(cur != null)
			cur.close();
		}
		return events;
	}
	public MonthlyEventInstances getMonthlyEventsInstances(Context context, Integer calendarId, GregorianCalendar start, GregorianCalendar end) {
		MonthlyEventInstances instances = new MonthlyEventInstances();
		// Projection array. Creating indices for this array instead of doing
		// dynamic lookups improves performance.
		final String[] EVENT_PROJECTION = new String[] {
			CalendarContract.Instances.CALENDAR_ID,
			CalendarContract.Instances._ID, 
			CalendarContract.Instances.BEGIN, 
			CalendarContract.Instances.END,
			CalendarContract.Instances.TITLE,
			CalendarContract.Instances.RRULE,
			CalendarContract.Instances.EVENT_ID,
			CalendarContract.Instances.EVENT_TIMEZONE,
			CalendarContract.Instances.ALL_DAY,
			CalendarContract.Instances.EXDATE,
			CalendarContract.Instances.DTSTART,
			CalendarContract.Instances.DTEND,
			CalendarContract.Instances.ORIGINAL_ID
			
		};

		// Run query
		Cursor cur = null;
		ContentResolver cr = context.getContentResolver();
		
		
		Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon(); 
		
		ContentUris.appendId(builder, start.getTimeInMillis());
		ContentUris.appendId(builder, end.getTimeInMillis()); 
		Uri uri = builder.build();
		// Submit the query and get a Cursor object back. =
		try {
			cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
			
			while (cur.moveToNext()) {
				
				EventInstance instance = new EventInstance();
				Event event = new Event();
				event.setCalendarId(cur.getInt(0));
				instance.setInstanceId(cur.getInt(1));
				instance.setDateFrom(getCalendar(cur.getLong(2)));
				instance.setDateTo(getCalendar(cur.getLong(3)));
				event.setTitle(cur.getString(4));
				RecurrenceRule rule = new RecurrenceRule();
				rule.setRRule(cur.getString(5));
				event.setRrule(rule);    
				event.setEventId(cur.getInt(6));
			    event.setExDate(cur.getString(9));
			    event.setDateFrom(getCalendar(cur.getLong(10)));
				event.setDateTo(getCalendar(cur.getLong(11)));
			    event.setAllDay(cur.getInt(8) == 1);
			    event.setOriginalId(cur.getInt(12));
			    Log.d(this.getClass().getCanonicalName(), "Event Timezone: " + cur.getString(7));
			    event.setEventTimezone(cur.getString(7));
			    instance.setEvent(event);
			    addInstant(instances, instance);
			    if(event.getTitle().equals("test")) {
			    	Log.d("OrigId", "Event ID: " + event.getEventId() + "Orignl Id: " + event.getOriginalId());
			    }
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			cur.close();
			
		}
		sortMonthlyEvents(instances);
		return instances;
	}
	private void addInstant(MonthlyEventInstances instances,
			EventInstance instance) {
		int dayOfMonth = instance.getDateFrom().get(java.util.Calendar.DAY_OF_MONTH);
		if(instances.get(dayOfMonth) == null) {
			instances.put(dayOfMonth, new LinkedList<EventInstance>());
		}
		instances.get(dayOfMonth).add(instance);
	}
	
	public Event getCalendarEvent(Context context, Integer calendarId, Integer eventId) {
		Event event = null;
		// Projection array. Creating indices for this array instead of doing
		// dynamic lookups improves performance.
		final String[] EVENT_PROJECTION = new String[] {
			CalendarContract.Events.CALENDAR_ID,
			CalendarContract.Events._ID, 
			CalendarContract.Events.DTSTART, 
			CalendarContract.Events.DTEND,
			CalendarContract.Events.TITLE, 
			CalendarContract.Events.EVENT_TIMEZONE,
			CalendarContract.Events.RDATE,
			CalendarContract.Events.RRULE,
			CalendarContract.Events.EXDATE,
		};

		// Run query
		Cursor cur = null;
		ContentResolver cr = context.getContentResolver();
		Uri uri = CalendarContract.Events.CONTENT_URI;   
		String selection = "((" + CalendarContract.Events.CALENDAR_ID + " = ?) AND (" 
		                        + CalendarContract.Events._ID + " = ?))";
		String[] selectionArgs = new String[] {String.valueOf(calendarId),String.valueOf(eventId)}; 
		// Submit the query and get a Cursor object back. 
		try {
			cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
			while (cur.moveToNext()) {
				event = new Event();
				event.setCalendarId(cur.getInt(0));
				event.setEventId(cur.getInt(1));
				java.util.Calendar calFrom = java.util.Calendar.getInstance();
			    calFrom.setTimeInMillis(Long.valueOf(cur.getLong(2)));
			    java.util.Calendar calTo = java.util.Calendar.getInstance();
			    calTo.setTimeInMillis(Long.valueOf(Long.valueOf(cur.getLong(3))));
				event.setDateFrom(calFrom);
				event.setDateTo(calTo);
			    event.setTitle(cur.getString(4));
			    event.setEventTimezone(cur.getString(5));
			    event.setRdate(cur.getString(6));
			    event.setExDate(cur.getString(8));
			    RecurrenceRule rule = new RecurrenceRule();
			    rule.setRRule(cur.getString(7));
			    event.setRrule(rule);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(null != cur)
			cur.close();
		}
		
		return event;
	}
	private void sortMonthlyEvents(SparseArray<LinkedList<EventInstance>> monthlyEvents ) {
		if(monthlyEvents != null) {
			EventInstanceComparator comp = new EventInstanceComparator();
			for(int i = 0; i < monthlyEvents.size(); i++) {
				   int key = monthlyEvents.keyAt(i);
				   // get the object by the key.
				    Collections.sort(monthlyEvents.get(key), comp);
				}
		}
		
	}
	private void sortListEventsByDate(List<EventInstance> events ) {
		if(null != events) {
			EventInstanceComparator comp = new EventInstanceComparator();
			Collections.sort(events, comp);
		}
		
	}
	public Map<String, Calendar> getCalendars(Context context) {
		Map<String, Calendar> calendars = new HashMap<String, Calendar>();
		String[] projection = new String[] {
		       CalendarContract.Calendars._ID,
		       CalendarContract.Calendars.ACCOUNT_NAME,
		       CalendarContract.Calendars.CALENDAR_DISPLAY_NAME
		};
		ContentResolver contentResolver = context.getContentResolver();
		
		Cursor calendarCursor = null;
		try {
			calendarCursor = contentResolver.query(CalendarContract.Calendars.CONTENT_URI, projection, null, null, null);
			while (calendarCursor.moveToNext()) {
				calendars.put(calendarCursor.getString(1),new Calendar(calendarCursor.getInt(0),calendarCursor.getString(1),calendarCursor.getString(2)));
			}
		}
		finally {
			if(null != calendarCursor) {
				calendarCursor.close();
			}
		}
		
		return calendars;
	}
	
	public Event addUpdateCalendarEvent(Context ctx, Event event) throws InterruptedException {

	    ContentResolver contentResolver = ctx.getContentResolver();

	    ContentValues calEvent = new ContentValues();
	    calEvent.put(Events.CALENDAR_ID, event.getCalendarId());

	    calEvent.put(Events.TITLE, event.getTitle());
	    calEvent.put(Events.DTSTART, event.getDateFrom().getTimeInMillis()); 
	    
	    calEvent.put(Events.EVENT_TIMEZONE, event.getEventTimezone());
	    
	    if(event.isAllDay()) {
	        calEvent.put(Events.ALL_DAY, 1);
	    }
	    if(null != event.getRrule()) {
	    	if(null != event.getRrule().getRRule()) {
	    		calEvent.put(Events.RRULE, event.getRrule().getRRule());
	    	}
	    	else {
	    		 String rrule = RecurrenceRuleConverter.toString(event.getRrule());
	    		 calEvent.put(Events.RRULE, rrule);
	    	}
	    }
	    if(null != event.getExDate()) {
	   
	    	calEvent.put(Events.EXDATE, event.getExDate());
	    	
	    }
 	    if(null != event.getDuration()) {
	        calEvent.put(Events.DURATION, event.getDuration());
	    }
	    if(null != event.getDateTo()) {
	        calEvent.put(Events.DTEND, event.getDateTo().getTimeInMillis());
	    }
	  
	    Uri uri = null;

		if(null != event.getEventId()) {
			uri = ContentUris.withAppendedId(Events.CONTENT_URI, event.getEventId());
			int rows = ctx.getContentResolver().update(uri, calEvent, null, null);
			Log.d("Rows updated: " ,  String.valueOf(rows));
		}
		else {
			uri = contentResolver.insert(Events.CONTENT_URI, calEvent);
			event.setEventId(Integer.parseInt(uri.getLastPathSegment()));
		}

	    return event;
	}
	
	public List<EventInstance> getEventInstances(Context context, Integer calendarId, Integer eventId) {
		// Projection array. Creating indices for this array instead of doing
		// dynamic lookups improves performance.
		final String[] EVENT_PROJECTION = new String[] {
			CalendarContract.Instances.CALENDAR_ID,
			CalendarContract.Instances._ID, 
			CalendarContract.Instances.BEGIN, 
			CalendarContract.Instances.END,
			CalendarContract.Instances.TITLE,
			CalendarContract.Instances.RRULE,
			CalendarContract.Instances.EVENT_ID,
			CalendarContract.Instances.EVENT_TIMEZONE,
			CalendarContract.Instances.ALL_DAY,
			CalendarContract.Instances.EXDATE,
			CalendarContract.Instances.DTSTART,
			CalendarContract.Instances.DTEND
			
		};

		// Run query
		Cursor cur = null;
		ContentResolver cr = context.getContentResolver();
		
		
		Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon(); 
		
		ContentUris.appendId(builder, Long.MIN_VALUE);
		ContentUris.appendId(builder, Long.MAX_VALUE); 
		Uri uri = builder.build();
		List<EventInstance> instances = new ArrayList<EventInstance>();
		String selection = "((" + Instances.CALENDAR_ID + " = ?) AND (" + Instances.EVENT_ID + " = ?))";
		String[] selectionArgs = new String[] {String.valueOf(calendarId),String.valueOf(eventId)}; 
		try {
			cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
			while (cur.moveToNext()) {
				EventInstance instance = new EventInstance();
				instance.setInstanceId(cur.getInt(1));
				instance.setDateFrom(getCalendar(cur.getLong(2)));
				instance.setDateTo(getCalendar(cur.getLong(3)));
				
				Event event = new Event();
				event.setCalendarId(cur.getInt(0));
				
				event.setDateFrom(getCalendar(cur.getLong(10)));
				event.setDateTo(getCalendar(cur.getLong(11)));
			    event.setTitle(cur.getString(4));
			    event.setEventId(cur.getInt(6));
			    RecurrenceRule rule = new RecurrenceRule();
			    rule.setRRule(cur.getString(5));
			    event.setRrule(rule);  
			    event.setAllDay(cur.getInt(8) == 1);
			    event.setExDate(cur.getString(9));
			    instance.setEvent(event);
			    instances.add(instance);
			}
		}
		
		finally {
			if(null != cur) {
				cur.close();
			}
			sortListEventsByDate(instances);
			
			
		}
		return instances;
	}

	public List<EventInstance> getEventInstancesByMonth(Context context, Integer calendarId, Integer eventId, GregorianCalendar startMonth) {
		// Projection array. Creating indices for this array instead of doing
		// dynamic lookups improves performance.
		final String[] EVENT_PROJECTION = new String[] {
			CalendarContract.Instances.CALENDAR_ID,
			CalendarContract.Instances._ID, 
			CalendarContract.Instances.BEGIN, 
			CalendarContract.Instances.END,
			CalendarContract.Instances.TITLE,
			CalendarContract.Instances.RRULE,
			CalendarContract.Instances.EVENT_ID,
			CalendarContract.Instances.EVENT_TIMEZONE,
			CalendarContract.Instances.ALL_DAY,
			CalendarContract.Instances.EXDATE,
			CalendarContract.Instances.DURATION,
			CalendarContract.Instances.DTSTART,
			CalendarContract.Instances.DTEND,
		};

		// Run query
		Cursor cur = null;
		ContentResolver cr = context.getContentResolver();
		
		
		Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon(); 
		ContentUris.appendId(builder, startMonth.getTimeInMillis());
		ContentUris.appendId(builder, DateUtil.getEndOfMonth(startMonth).getTimeInMillis()); 
		Uri uri = builder.build();
		List<EventInstance> instances = new ArrayList<EventInstance>();
		String selection = "((" + Instances.CALENDAR_ID + " = ?) AND (" + Instances.EVENT_ID + " = ?))";
		String[] selectionArgs = new String[] {String.valueOf(calendarId),String.valueOf(eventId)}; 
		try {
			cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
			while (cur.moveToNext()) {
				EventInstance instance = new EventInstance();
				instance.setInstanceId(cur.getInt(1));
				instance.setDateFrom(getCalendar(Long.valueOf(cur.getLong(2))));
				instance.setDateTo(getCalendar(Long.valueOf(cur.getLong(3))));
				
				Event event = new Event();
				event.setCalendarId(cur.getInt(0));
			    event.setTitle(cur.getString(4));
			    String rrule = cur.getString(5);
			    RecurrenceRule rule = RecurrenceRuleConverter.fromString(rrule);
			    rule.setRRule(rrule);
			    event.setRrule(rule);  
			    event.setEventId(cur.getInt(6));
			    event.setEventTimezone(cur.getString(7));
			    event.setAllDay(cur.getInt(8) == 1);
			    event.setExDate(cur.getString(9));
			    event.setDuration(cur.getString(10));
				event.setDateFrom(getCalendar(Long.valueOf(cur.getLong(11))));
				event.setDateTo(getCalendar(Long.valueOf(cur.getLong(12))));
				
				instance.setEvent(event);
			    instances.add(instance);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(null != cur) {
				cur.close();
			}
			
			sortListEventsByDate(instances);
			
			
			
		}
		return instances;
	}
	public EventInstance getNextEventInstance(Context context, Integer calendarId, Integer eventId, GregorianCalendar startDate) {
		// Projection array. Creating indices for this array instead of doing
		// dynamic lookups improves performance.
		final String[] EVENT_PROJECTION = new String[] {
			CalendarContract.Instances.CALENDAR_ID,
			CalendarContract.Instances._ID, 
			CalendarContract.Instances.BEGIN, 
			CalendarContract.Instances.END,
			CalendarContract.Instances.TITLE,
			CalendarContract.Instances.RRULE,
			CalendarContract.Instances.EVENT_ID,
			CalendarContract.Instances.EVENT_TIMEZONE,
			CalendarContract.Instances.ALL_DAY,
			CalendarContract.Instances.DTSTART,
			CalendarContract.Instances.DTEND
		};

		// Run query
		Cursor cur = null;
		ContentResolver cr = context.getContentResolver();
		
		
		Uri.Builder builder =  Instances.CONTENT_URI.buildUpon(); 
		ContentUris.appendId(builder, startDate.getTimeInMillis());
		ContentUris.appendId(builder, Long.MAX_VALUE); 
		Uri uri = builder.build();
		EventInstance instance = null;
		String selection = "((" + Instances.CALENDAR_ID + " = ?) AND (" + Instances.EVENT_ID + " = ?))";
		String[] selectionArgs = new String[] {String.valueOf(calendarId),String.valueOf(eventId)}; 
		try {
			cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, "begin LIMIT 1");
			
			while (cur.moveToNext()) {
				instance = new EventInstance();
				instance.setInstanceId(cur.getInt(1));
				instance.setDateFrom(getCalendar(Long.valueOf(cur.getLong(2))));
				instance.setDateTo(getCalendar(Long.valueOf(cur.getLong(3))));
				
				Event event = new Event();
				event.setCalendarId(cur.getInt(0));
				event.setDateFrom(getCalendar(Long.valueOf(cur.getLong(9))));
				event.setDateTo(getCalendar(Long.valueOf(cur.getLong(10))));
			    event.setTitle(cur.getString(4));
			    event.setEventId(cur.getInt(6));
			    RecurrenceRule rule = new RecurrenceRule();
			    rule.setRRule(cur.getString(5));
			    event.setRrule(rule);  
			    event.setAllDay(cur.getInt(8) == 1);
			    
			    instance.setEvent(event);
			    
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(cur != null) {
				cur.close();	
			}
					
		}
		return instance;
	}
	public int deleteAllInstances(Context context, long eventId) {
	
		  int iNumRowsDeleted = 0;
          Uri eventsUri = Events.CONTENT_URI;
          Uri uri = ContentUris.withAppendedId(eventsUri, eventId);
          iNumRowsDeleted = context.getContentResolver().delete(uri, null, null);
          return iNumRowsDeleted;
          
       
		
	}
	
	private java.util.Calendar getCalendar(Long dateInMillis) {
		java.util.Calendar cal = java.util.Calendar.getInstance();
	    cal.setTimeInMillis(Long.valueOf(dateInMillis));
	    return cal;
	}
	public List<Reminder> getReminders(Context ctx, Event event) {
		List<Reminder> reminders = new ArrayList<Reminder>();
		final String[] EVENT_PROJECTION = new String[] {
				Reminders._ID,
				Reminders.MINUTES,
				Reminders.METHOD
			};

			// Run query
			Cursor cur = null;
			ContentResolver cr = ctx.getContentResolver();
			try {
				cur = Reminders.query(cr, event.getEventId(),EVENT_PROJECTION);
				while (cur.moveToNext()) {
					Reminder reminder = new Reminder();
					reminder.set_id(cur.getInt(0));
					reminder.setMinutes(cur.getInt(1));
					reminder.setMethod(cur.getInt(2));
					if(event.getReminders() == null) {
						event.setReminders(new ArrayList<Reminder>());
					}
					event.getReminders().add(reminder);
					reminder.setEvent(event);
					reminders.add(reminder);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			return reminders;
	}
	public void addUpdateReminders(Context ctx, Integer eventId, List<Reminder> reminders, boolean isUpdate) throws InterruptedException {


		deleteAllRemindersForEvent(ctx, eventId);


		ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
		for(Reminder reminder : reminders) {
			 
			 operations.add(ContentProviderOperation.newInsert(Reminders.CONTENT_URI)
					 .withValue(Reminders.EVENT_ID, reminder.getEvent().getEventId())
					 .withValue(Reminders.METHOD, reminder.getMethod())
					 .withValue(Reminders.MINUTES, reminder.getMinutes())
					 .withYieldAllowed(true).build());
			 
		}
		try {
			ctx.getContentResolver().applyBatch(CalendarContract.AUTHORITY, operations);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void deleteAllRemindersForEvent(Context ctx, Integer eventid) {
		//What we want to update
		List<Integer> reminderIds = getReminderIdsByEventId(ctx, eventid);
		for(Integer reminderId : reminderIds) {
			Uri reminderUri = ContentUris.withAppendedId(CalendarContract.Reminders.CONTENT_URI, reminderId);
			int rows = ctx.getContentResolver().delete(reminderUri, null, null);
			Log.d(this.getClass().getName(), "deleted " + rows + " reminders");
		}
		
		
	}
	public List<Integer> getReminderIdsByEventId(Context ctx, Integer eventId) {
		List<Integer> reminders = new ArrayList<Integer>();
		final String[] EVENT_PROJECTION = new String[] {
				Reminders._ID,
			};

			// Run query
			Cursor cur = null;
			ContentResolver cr = ctx.getContentResolver();
			try {
				cur = Reminders.query(cr, eventId,EVENT_PROJECTION);
				while (cur.moveToNext()) {
					
					reminders.add(cur.getInt(0));
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			return reminders;
	}
}

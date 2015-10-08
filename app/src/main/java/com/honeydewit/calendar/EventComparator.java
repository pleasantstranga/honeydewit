package com.honeydewit.calendar;

import java.util.Comparator;

public class EventComparator implements Comparator<Event> {
		 
	
		@Override
		public int compare(Event event1, Event event2) {
			if(event1.isAllDay() && !event2.isAllDay()) {
				return -1;
			}
			return event1.getDateFrom().compareTo(event2.getDateFrom());
		}	

}

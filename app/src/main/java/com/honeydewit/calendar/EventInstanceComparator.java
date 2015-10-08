package com.honeydewit.calendar;

import java.util.Comparator;

public class EventInstanceComparator implements Comparator<EventInstance> {
		 
	
		@Override
		public int compare(EventInstance instance1, EventInstance instance2) {
			if(instance1.getEvent().isAllDay() && !instance2.getEvent().isAllDay()) {
				return -1;
			}
			return instance1.getDateFrom().compareTo(instance2.getDateFrom());
		}	

}

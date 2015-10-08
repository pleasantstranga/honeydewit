package com.honeydewit.calendar;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/*	String[] tests = { "FREQ=DAILY;COUNT=10",
		"FREQ=DAILY;UNTIL=19971224T000000Z",
		"Every Other Day forever: RRULE:FREQ=DAILY;INTERVAL=2",
		"Every 10 days for 5 occurrences: RRULE:FREQ=DAILY;INTERVAL=10;COUNT=5",
		"Every day in January, for 3 years: RRULE:FREQ=YEARLY;UNTIL=20000131T140000Z;BYMONTH=1;BYDAY=SU,MO,TU,WE,TH,FR,SA",
	    "Every day in January, for 3 years: RRULE:FREQ=DAILY;UNTIL=20000131T140000Z;BYMONTH=1",
		"--Weekly 10 Occurrences: RRULE:FREQ=WEEKLY;COUNT=10",
		"--Weekly Until: RRULE:FREQ=WEEKLY;UNTIL=19971224T000000Z",
		"--Every other week - forever:RRULE:FREQ=WEEKLY;INTERVAL=2;WKST=SU",
		"--Weekly on Tuesday and Thursday for five weeks: RRULE:FREQ=WEEKLY;UNTIL=19971007T000000Z;WKST=SU;BYDAY=TU,TH",
		"--Weekly on Tuesday and Thursday for five weeks:RRULE:FREQ=WEEKLY;COUNT=10;WKST=SU;BYDAY=TU,TH",
		"--Every other week on Monday, Wednesday, and Friday until December 24, 1997: RRULE:FREQ=WEEKLY;INTERVAL=2;UNTIL=19971224T000000Z;WKST=SU;BYDAY=MO,WE,FR",
		"Monthly on the first Friday for 10 occurrences:RRULE:FREQ=MONTHLY;COUNT=10;BYDAY=1FR",
		"Monthly on the first Friday until December 24, 1997:RRULE:FREQ=MONTHLY;UNTIL=19971224T000000Z;BYDAY=1FR",
		"Every other month on the first and last Sunday of the month for 10 occurrences: RRULE:FREQ=MONTHLY;INTERVAL=2;COUNT=10;BYDAY=1SU,-1SU",
		"Monthly on the second-to-last Monday of the month for 6 months: RRULE:FREQ=MONTHLY;COUNT=6;BYDAY=-2MO",
		"Monthly on the third-to-the-last day of the month, forever:RRULE:FREQ=MONTHLY;BYMONTHDAY=-3",
		"--Monthly on the 2nd and 15th of the month for 10 occurrences:RRULE:FREQ=MONTHLY;COUNT=10;BYMONTHDAY=2,15",
		"Monthly on the first and last day of the month for 10 occurrences:RRULE:FREQ=MONTHLY;COUNT=10;BYMONTHDAY=1,-1",
		"Every 18 months on the 10th thru 15th of the month for 10 occurrences:RRULE:FREQ=MONTHLY;INTERVAL=18;COUNT=10;BYMONTHDAY=10,11,12,13,14,15",
		"Every Tuesday, every other month:RRULE:FREQ=MONTHLY;INTERVAL=2;BYDAY=TU",
		"Yearly in June and July for 10 occurrences:RRULE:FREQ=YEARLY;COUNT=10;BYMONTH=6,7",
		"Every other year on January, February, and March for 10 occurrences:RRULE:FREQ=YEARLY;INTERVAL=2;COUNT=10;BYMONTH=1,2,3",
		"Every third year on the 1st, 100th, and 200th day for 10 occurrences:RRULE:FREQ=YEARLY;INTERVAL=3;COUNT=10;BYYEARDAY=1,100,200",
		"Every 20th Monday of the year, forever:RRULE:FREQ=YEARLY;BYDAY=20MO",
		"Monday of week number 20 (where the default start of the week is Monday), forever:RRULE:FREQ=YEARLY;BYWEEKNO=20;BYDAY=MO",
		"Every Thursday in March, forever:RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=TH",
		"Every Thursday, but only during June, July, and August, forever:RRULE:FREQ=YEARLY;BYDAY=TH;BYMONTH=6,7,8",
		"Every Friday the 13th, forever:RRULE:FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13",
		"--The first Saturday that follows the first Sunday of the month, forever:RRULE:FREQ=MONTHLY;BYDAY=SA;BYMONTHDAY=7,8,9,10,11,12,13",
		"--Every 4 years, the first Tuesday after a Monday in November,forever (U.S. Presidential Election day):RRULE:FREQ=YEARLY;INTERVAL=4;BYMONTH=11;BYDAY=TU;BYMONTHDAY=2,3,4,5,6,7,8",
		"--The third instance into the month of one of Tuesday, Wednesday, or Thursday, for the next 3 months:RRULE:FREQ=MONTHLY;COUNT=3;BYDAY=TU,WE,TH;BYSETPOS=3",
		"--The second-to-last weekday of the month:RRULE:FREQ=MONTHLY;BYDAY=MO,TU,WE,TH,FR;BYSETPOS=-2",
		"--Every 3 hours from 9:00 AM to 5:00 PM on a specific day:RRULE:FREQ=HOURLY;INTERVAL=3;UNTIL=19970902T170000Z",
		"--Every 15 minutes for 6 occurrences:RRULE:FREQ=MINUTELY;INTERVAL=15;COUNT=6",
		"--Every hour and a half for 4 occurrences:RRULE:FREQ=MINUTELY;INTERVAL=90;COUNT=4",
		"--Every 20 minutes from 9:00 AM to 4:40 PM every day:RRULE:FREQ=DAILY;BYHOUR=9,10,11,12,13,14,15,16;BYMINUTE=0,20,40",
		"--Every 20 minutes from 9:00 AM to 4:40 PM every day:RRULE:FREQ=MINUTELY;INTERVAL=20;BYHOUR=9,10,11,12,13,14,15,16" };
		**/
public class RecurrenceRuleConverter {

	private static final String FIELDS_SEP = ";";
	private static final String KEY_SEP = "=";
	private static final String VALUE_SEP = ",";
	
	public static RecurrenceRule fromString(String line) {
		RecurrenceRule rule = new RecurrenceRule();
		if(null != line) {
			String[] values = line.split(";");
			
			Class<?> c = rule.getClass();
			for(String property : values) {
				int seperator = property.indexOf("=");
				String fieldName = property.substring(0, seperator).toLowerCase();
				Object fieldValue = property.substring(seperator+1);
				try {
					Field field = c.getDeclaredField(fieldName);
					Class<?> type = field.getType();
					field.setAccessible(true);
					
					if(type.equals(Integer.class)) {
						fieldValue = Integer.parseInt(fieldValue.toString());
					}
					else if(type.equals(Set.class)) {
						ParameterizedType pt = (ParameterizedType) field.getGenericType();  
						Type genericType = pt.getActualTypeArguments()[0];
						
						fieldValue = convertStringToSet(fieldValue.toString(), genericType.toString());
					}
				    field.set(rule, fieldValue);
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				
			}
			rule.setRRule(line);
		}
		return rule;
		
	}
	
	public static String toString(RecurrenceRule rule) {
	
		StringBuffer sb = new StringBuffer();
		
		//sb.append(OBJ_TYPE + NAME_SEP);
		Class<?> c = rule.getClass();
		try {
			Field[] fields = c.getDeclaredFields();
			for(Field field : fields) {
				field.setAccessible(true);
				Class<?> type = field.getType();
				if(type.getName().equals("java.util.Set")) {
					if(null != field.get(rule)) {
						Set<?> set = (Set<?>)field.get(rule);
						sb.append(field.getName().toUpperCase()).append(KEY_SEP);
						for(Object obj : set) {
							String value = (String)obj;
							if(value.startsWith("0")) {
								value = value.substring(1);
							}
							sb.append(value).append(VALUE_SEP);
						}
						sb.deleteCharAt(sb.length()-1);
						sb.append(FIELDS_SEP);
					}
				}
				else {
					if(!field.getName().equals("serialVersionUID") && !field.getName().equalsIgnoreCase("untilDateUTC") && null != field.get(rule)) {
						Object obj = field.get(rule);
						if(field.getName().toUpperCase().equals("FREQ")) {
							String prepend = field.getName().toUpperCase() + KEY_SEP + ((String)obj).toUpperCase() + FIELDS_SEP;
							sb.insert(0, prepend);
						}
						else {
							sb.append(field.getName().toUpperCase()).append(KEY_SEP).append(obj).append(FIELDS_SEP);
						}
					}
				}
			}
			sb.deleteCharAt(sb.length()-1);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
		
	}
	public<T extends Comparable<? super T>> List<T> asSortedList(Collection<T> c) {
	  List<T> list = new ArrayList<T>(c);
	  java.util.Collections.sort(list);
	  return list;
	}
	private static Set<java.lang.Object> convertStringToSet(String string, String genericType) {
		Set<java.lang.Object> set = new HashSet<java.lang.Object>();
		for(String itemToAdd : string.split(",")) {
			if(genericType.equals("class java.lang.Integer")) {
				set.add(Integer.parseInt(itemToAdd));
			}
			else {
				set.add(itemToAdd);
			}
			
		}
		return set;
	}
}

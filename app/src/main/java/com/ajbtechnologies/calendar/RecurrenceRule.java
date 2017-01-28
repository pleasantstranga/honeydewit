package com.ajbtechnologies.calendar;

import java.io.Serializable;
import java.util.Set;


public class RecurrenceRule implements Serializable {

	/**Serial Id**/
	private static final long serialVersionUID = -5663960142269345607L;
	private String freq;
	private Integer interval;
	private Integer count;
	private Set<String> bymonth;
	private Set<Integer> byweekno;
	private Set<String> byday;
	private Set<Integer> byyearday;
	private Set<Integer> byhour;
	private Set<Integer> byminute;
	private String until;
	private String wkst;
	private Set<String> bymonthday;
	private Integer bysetpos;
	private boolean untildateutc;
	private String rrule;
	
	public String getFreq() {
		return freq;
	}
	public void setFreq(String frqy) {
		this.freq = frqy;
	}
	public Integer getInterval() {
		return interval;
	}
	public void setInterval(Integer interval) {
		this.interval = interval;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Set<String> getByMonth() {
		return bymonth;
	}
	public void setByMonth(Set<String> byMonth) {
		this.bymonth = byMonth;
	}
	public Set<Integer> getByWeekNumber() {
		return byweekno;
	}
	public void setByWeekNumber(Set<Integer> byWeekNo) {
		this.byweekno = byWeekNo;
	}
	public Set<String> getByDay() {
		return byday;
	}
	public void setByDay(Set<String> byDay) {
		this.byday = byDay;
	}
	public Set<Integer> getByYearDay() {
		return byyearday;
	}
	public void setByYearDay(Set<Integer> byYearDay) {
		this.byyearday = byYearDay;
	}
	public Set<Integer> getByHour() {
		return byhour;
	}
	public void setByHour(Set<Integer> byHour) {
		this.byhour = byHour;
	}
	public Set<Integer> getByMinute() {
		return byminute;
	}
	public void setByMinute(Set<Integer> byMinute) {
		this.byminute = byMinute;
	}
	public String getUntil() {
		return until;
	}
	public void setUntil(String until) {
		this.until = until;
	}
	public String getWeekStart() {
		return wkst;
	}
	public void setWeekStart(String wkst) {
		this.wkst = wkst;
	}
	public Set<String> getByMonthDay() {
		return bymonthday;
	}
	public void setByMonthDay(Set<String> byMonthDay) {
		this.bymonthday = byMonthDay;
	}
	public Integer getBySetPosition() {
		return bysetpos;
	}
	public void setBySetPosition(Integer bySetPos) {
		this.bysetpos = bySetPos;
	}
	public boolean isUntilDateUTC() {
		return untildateutc;
	}
	public void setUntilDateUTC(boolean untilDateUTC) {
		this.untildateutc = untilDateUTC;
	}
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("RecurranceRule: \n");
		if(null != freq) {
			stringBuilder.append("Frequency: " + freq + "\n");
		}
		if(null != interval) {
			stringBuilder.append("Interval: " + interval + "\n");
		}
		if(null != count) {
			stringBuilder.append("Count: " + count + "\n");
		}
		if(null != until) {
			stringBuilder.append("Until: " + until + "\n");
		}
		if(null != wkst) {
			stringBuilder.append("Weekstart: " + wkst + "\n");
		}
		if(null != bysetpos) {
			stringBuilder.append("BySetPosition: " + bysetpos + "\n");
		}
		if(null != bymonth) {
			stringBuilder.append("ByMonth: " + convertSetToString(bymonth) + "\n");
		}
		if(null != byweekno) {
			stringBuilder.append("ByWeekNum: " + convertSetToString(byweekno) + "\n");
		}
		if(null != byyearday) {
			stringBuilder.append("ByYearDay: " + convertSetToString(byyearday) + "\n");
		}
		if(null != bymonthday) {
			stringBuilder.append("ByMonthDay: " + convertSetToString(bymonthday) + "\n");
		}
		if(null != byday) {
			stringBuilder.append("ByDay: " + byday.toString() + "\n");
		}
		
		return stringBuilder.toString();
	}
	private String convertSetToString(Set<?> values) {
		String string = "";
		for(Object value : values) {
			string += value.toString() + ",";
		}
		return string;
		
	}
	public String getRRule() {
		return rrule;
	}
	public void setRRule(String rrule) {
		this.rrule = rrule;
	}

}

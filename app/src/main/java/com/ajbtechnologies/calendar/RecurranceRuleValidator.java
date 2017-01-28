package com.ajbtechnologies.calendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RecurranceRuleValidator {
	public List<String> errors = new ArrayList<String>();
	String[] validFrequencies = {"SECONDLY","MINUTELY","HOURLY","DAILY","WEEKLY","MONTHLY","YEARLY"};
	
	public boolean validate(RecurrenceRule rule) {
		return validateWhen(rule) &&
				validateFrequency(rule) &&
				validateInterval(rule);
	}
	private boolean validateWhen(RecurrenceRule rule) {
		if(null != rule.getUntil() && null != rule.getCount()) {
			errors.add("You can not choose both \"Until\" and \"Count\".");
			return false;
		}
		if(null != rule.getUntil()) {
			if(!rule.isUntilDateUTC()) {
				if(rule.getUntil().length() != 8 || isValidDate(rule.getUntil())) {
					errors.add("You must pass a valid until date.");
					return false;
				}
			}
			else {
				
			}
		}
		return true;
	}
	private boolean isValidDate(String date) {
		try {
			Long.parseLong(date);
		}
		catch(NumberFormatException exc) {
			return false;
		}
		return true;
	}
	private boolean validateFrequency(RecurrenceRule rule) {
		errors.add("The frequency chosen is not valid.");
		return Arrays.asList(validFrequencies).contains(rule.getFreq());
	}
	private boolean validateInterval(RecurrenceRule rule) {
		if(null != rule.getInterval()) {
			if(rule.getInterval() <1) {
				errors.add("When set, interval must be greater than or equal to 1.");
			}
		}
		return true;
	}
}

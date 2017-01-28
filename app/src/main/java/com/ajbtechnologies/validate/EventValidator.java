package com.ajbtechnologies.validate;

import android.content.Context;
import android.content.res.Resources;

import com.ajbtechnologies.R;
import com.ajbtechnologies.calendar.Event;

public class EventValidator extends BaseValidator {
	Event event;
	Resources res;
	
	@Override
	public boolean validate(Context context, Object object) {
		event = (Event) object;
		res = context.getResources();
		validateEventName();
		validateDates();
		return getErrorMessages().size() == 0;
	}

	private void validateEventName() {
		if(event.getTitle() == null || event.getTitle().length() == 0) {
			addErrorMessage(res.getString(R.string.eventNameRequired));
		}
		else if(event.getTitle().length() < 3){
			addErrorMessage(res.getString(R.string.eventMin3CharRequired));
		}
	}
	private void validateDates() {
		if(event.getRrule() == null) {
			if(event.getDateTo().getTimeInMillis() < event.getDateFrom().getTimeInMillis()) {
				addErrorMessage(res.getString(R.string.eventDateLaterStart));
			}
		}
		else if(event.getDuration() != null && (event.getDuration().length() == 1 || event.getDuration().contains("-"))) {
			addErrorMessage(res.getString(R.string.eventDateLaterStart));
		}
	}
	
	
}

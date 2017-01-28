package com.ajbtechnologies.validate;

import android.content.Context;
import android.content.res.Resources;

import com.ajbtechnologies.R;
import com.ajbtechnologies.pojos.ListItem;

public class ToDoItemValidator extends BaseValidator {
	
	@Override
	public boolean validate(Context context, Object object) {
		ListItem listItem = (ListItem) object;
			Resources res = context.getResources();
			
			if(listItem.getName().isEmpty()) {
				addErrorMessage(res.getString(R.string.nameRequired));
			}
			else if(listItem.getName().length() <3) {
				addErrorMessage(res.getString(R.string.min3CharRequired));
			}
			return getErrorMessages().size() == 0;
	}
	
}

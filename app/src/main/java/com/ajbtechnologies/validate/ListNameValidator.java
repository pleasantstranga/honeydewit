package com.ajbtechnologies.validate;

import android.content.Context;
import android.content.res.Resources;

import com.ajbtechnologies.Constants;
import com.ajbtechnologies.Application;
import com.ajbtechnologies.R;

public class ListNameValidator extends BaseValidator {

	
	@Override
	public boolean validate(Context context, Object object) {
			clearErrorMessages();
			String listName = (String)object;
		
			Resources res = context.getResources();
			if(null == listName || listName.length() == 0) {
				
				getErrorMessages().add(res.getString(R.string.nameRequired));
			}
			else if(listName.length() < 3) {
				getErrorMessages().add(res.getString(R.string.min3CharRequired));
			}
			else {
				String currentListName = ((Application)context).getCurrentList().getListName();
				boolean isCurrentListName = currentListName.equals(listName);
                boolean isExists = ((Application) context.getApplicationContext()).getShoppingListDbHelper().isListExists(listName, Constants.SHOPPING_LIST_TYPE_CDE, true);
				getErrorMessages().add(res.getString(R.string.listExists).replace("?", "\"" + listName + "\""));

			}

			return getErrorMessages().size() == 0;
	}
}

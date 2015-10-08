package com.honeydewit.validate;

import android.content.Context;
import android.content.res.Resources;

import com.honeydewit.Constants;
import com.honeydewit.HoneyDewApplication;
import com.honeydewit.R;

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

                boolean isExists = ((HoneyDewApplication) context.getApplicationContext()).getShoppingListDbHelper().isListExists(listName, Constants.SHOPPING_LIST_TYPE_CDE, true);
				if(isExists) {
					getErrorMessages().add(res.getString(R.string.listExists).replace("?", "\"" + listName + "\""));
				}	
			}

			return getErrorMessages().size() == 0;
	}
}

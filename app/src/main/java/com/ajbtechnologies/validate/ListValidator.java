package com.ajbtechnologies.validate;

import android.content.Context;
import android.content.res.Resources;

import com.ajbtechnologies.Application;
import com.ajbtechnologies.R;
import com.ajbtechnologies.pojos.BasicList;

public class ListValidator extends BaseValidator {

	
	@Override
	public boolean validate(Context context, Object object) {
			BasicList list = (BasicList)object;
		
			Resources res = context.getResources();
			if(null == list.getListName() || list.getListName().length() == 0) {
				
				getErrorMessages().add(res.getString(R.string.nameRequired));
			}
			else if(list.getListName().length() < 3) {
				getErrorMessages().add(res.getString(R.string.min3CharRequired));
			}
			else {
			
				BasicList listFromDb = ((Application)context).getShoppingListDbHelper().getListByName(list.getListName(), list.getListTypeId(), true);
				if(listFromDb != null && listFromDb.get_id() != list.get_id()) {
					getErrorMessages().add(res.getString(R.string.listExists).replace("?", "\"" + list.getListName() + "\""));
				}	
			}
			return getErrorMessages().size() == 0;
	}
}

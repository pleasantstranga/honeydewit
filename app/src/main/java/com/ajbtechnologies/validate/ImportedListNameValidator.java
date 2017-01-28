package com.ajbtechnologies.validate;

import android.content.Context;
import android.content.res.Resources;

import com.ajbtechnologies.Constants;
import com.ajbtechnologies.Application;
import com.ajbtechnologies.R;

/**
 * Used to validate Workbook objects
 * @author aaronbernstein
 *
 */
public class ImportedListNameValidator extends BaseValidator {
	
	boolean isValidateListExists = true;
	
	@Override
	public boolean validate(Context context, Object object) {
			String name = (String)object;
			Resources res = context.getResources();

			if(name == null || name.length() < 3) {
				getErrorMessages().add(res.getString(R.string.min3CharRequired));
			}
			else if(isValidateListExists){
				if(((Application)context.getApplicationContext()).getShoppingListDbHelper().isListExists(name, Constants.SHOPPING_LIST_TYPE_CDE, true)) {
					String error = res.getString(R.string.listExistsMessage);
					error = error.replace("^",name );
					getErrorMessages().add(error);
				}
			}
			return getErrorMessages().size() == 0;
	}

	public void setValidateListExists(boolean isValidateListExists) {
		this.isValidateListExists = isValidateListExists;
	}
}

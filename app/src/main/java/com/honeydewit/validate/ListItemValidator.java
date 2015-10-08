package com.honeydewit.validate;

import android.content.Context;
import android.content.res.Resources;

import com.honeydewit.HoneyDewApplication;
import com.honeydewit.R;
import com.honeydewit.pojos.ListItem;
import com.honeydewit.utils.StringUtils;


public class ListItemValidator extends BaseValidator {
	private HoneyDewApplication context;

	public ListItemValidator(HoneyDewApplication context) {
		this.context = context;
	}
	@Override
	public boolean validate(Context context, Object object) {
		ListItem listItem = (ListItem) object;
		Resources res = context.getResources();
		clearErrorMessages();
		if(StringUtils.isEmpty(listItem.getName())) {

			addErrorMessage(res.getString(R.string.nameRequired));
		}
		else if(listItem.getName().length() <3) {

			addErrorMessage(res.getString(R.string.min3CharRequired));
		}
		if(listItem.getQuantity() == null || listItem.getQuantity() < 1) {

			addErrorMessage(res.getString(R.string.quantityRequired));
		}
		if(listItem.getPricePerUnit() == null) {
			addErrorMessage(res.getString(R.string.invalidPrice));

		}
		if(listItem.getDiscountCoupon() == null) {

			addErrorMessage(res.getString(R.string.invalidDiscount));

		}
		return getErrorMessages().size() == 0;
	}

	
}

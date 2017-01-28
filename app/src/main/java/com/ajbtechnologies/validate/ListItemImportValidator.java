package com.ajbtechnologies.validate;

import android.content.res.Resources;

import com.ajbtechnologies.Application;
import com.ajbtechnologies.ImportError;
import com.ajbtechnologies.R;
import com.ajbtechnologies.pojos.ListItem;
import com.ajbtechnologies.utils.StringUtils;


public class ListItemImportValidator extends BaseImportValidator {
	boolean isValidateAttachment = false;
	private Application context;

	public ListItemImportValidator(Application context) {
		this.context = context;
	}
	@Override
	public boolean validate(Object object) {
		boolean isValid = true;
		ListItem listItem = (ListItem) object;
		Resources res = context.getResources();

		if(StringUtils.isEmpty(listItem.getName())) {
			if(!isValidateAttachment) {
				addErrorMessage(listItem.getName(),res.getString(R.string.nameRequired));
			}
			else {
				ImportError error = new ImportError();
				error.setLineNumber(listItem.getRowNumber());
				error.setList(listItem.getList());
				error.setError(res.getText(R.string.nameRequired).toString());
				error.setListItem(listItem);
				getImportErrors().add(error);
			}
			isValid = false;
		}
		else if(listItem.getName().length() <3) {
			if(!isValidateAttachment) {
				addErrorMessage(listItem.getName(),res.getString(R.string.min3CharRequired));
			}
			else {
				ImportError error = new ImportError();
				error.setLineNumber(listItem.getRowNumber());
				error.setList(listItem.getList());
				error.setError(res.getText(R.string.min3CharRequired).toString());
				error.setListItem(listItem);
				getImportErrors().add(error);
			}
			isValid = false;
		}
		if(listItem.getQuantity() == null || listItem.getQuantity() < 1) {
			if(!isValidateAttachment) {
				addErrorMessage(listItem.getName(),res.getString(R.string.quantityRequired));
			}
			else {
				ImportError error = new ImportError();
				error.setLineNumber(listItem.getRowNumber());
				error.setList(listItem.getList());
				error.setError(res.getText(R.string.quantityRequired).toString());
				error.setListItem(listItem);
				getImportErrors().add(error);
			}
			isValid = false;
		}
		if((isValidateAttachment && listItem.getPricePerUnit() == null)) {
			if(!isValidateAttachment) {
				addErrorMessage(listItem.getName(),res.getString(R.string.invalidPrice));
			}
			else {
				ImportError error = new ImportError();
				error.setLineNumber(listItem.getRowNumber());
				error.setList(listItem.getList());
				error.setError(res.getText(R.string.invalidPrice).toString());
				error.setListItem(listItem);
				getImportErrors().add(error);
			}
			isValid = false;
		}
		if((isValidateAttachment && listItem.getDiscountCoupon() == null)) {
			if(!isValidateAttachment) {
				addErrorMessage(listItem.getName(),res.getString(R.string.invalidDiscount));
			}
			else {
				ImportError error = new ImportError();
				error.setLineNumber(listItem.getRowNumber());
				error.setList(listItem.getList());
				error.setError(res.getText(R.string.invalidDiscount).toString());
				error.setListItem(listItem);
				getImportErrors().add(error);
			}
			isValid = false;
		}
		return isValid;
	}
	public boolean isValidateAttachment() {
		return isValidateAttachment;
	}

	public void setValidateAttachment(boolean isValidateAttachment) {
		this.isValidateAttachment = isValidateAttachment;
	}
	
}

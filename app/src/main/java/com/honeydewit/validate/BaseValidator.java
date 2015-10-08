package com.honeydewit.validate;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseValidator {
	public Context context;
	private List<String> errorMessages = new ArrayList<String>();

	public abstract boolean validate(Context context, Object object);
	public List<String> getErrorMessages() {
		return errorMessages;
	}
	public void clearErrorMessages() {
		errorMessages.clear();
	}
	public void addErrorMessage(String errorMessage) {
		getErrorMessages().add(errorMessage);
	}
}

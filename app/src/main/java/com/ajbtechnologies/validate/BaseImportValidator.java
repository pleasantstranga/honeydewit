package com.ajbtechnologies.validate;

import com.ajbtechnologies.ImportError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public abstract class BaseImportValidator {
	private List<ImportError> importErrors = new ArrayList<ImportError>();
	private HashMap<String,List<String>> errors = new HashMap<String, List<String>>();

	public abstract boolean validate(Object object);

	public List<ImportError> getImportErrors() {
		return importErrors;
	}
	public void addImportError(ImportError error) {

		importErrors.add(error);
	}
	public HashMap<String,List<String>> getErrorMessages() {
		return errors;
	}
	public void addErrorMessage(String sheetName,String error) {
		if(!errors.containsKey(sheetName)) {
            errors.put(sheetName, new ArrayList<String>());
        }
        errors.get(sheetName).add(error);
	}
}

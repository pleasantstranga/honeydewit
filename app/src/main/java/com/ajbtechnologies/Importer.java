package com.ajbtechnologies;

import com.ajbtechnologies.converter.Sheet;

import java.io.File;
import java.util.List;


public interface Importer {

	SerializableArrayList<ImportedList> prepareImport();
	void doRedirect(int resultCode, Integer messageId);
	boolean validate(List<Sheet> sheets);
	void deleteTempFiles(File tempFiles);
 }

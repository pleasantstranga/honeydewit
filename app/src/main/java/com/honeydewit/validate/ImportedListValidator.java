package com.honeydewit.validate;

import com.honeydewit.Constants;
import com.honeydewit.converter.Cell;
import com.honeydewit.converter.Sheet;
import com.honeydewit.dataaccess.DbHelperImpl;
import com.honeydewit.pojos.ImportHeader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ImportedListValidator extends  BaseImportValidator {

	private DbHelperImpl dbHelperImpl;

	public ImportedListValidator(DbHelperImpl dbHelper) {
		this.dbHelperImpl = dbHelper;
	}
	@SuppressWarnings("unchecked")
	@Override
	public boolean validate(Object lists) {
		return validateSheets((List<Sheet>)lists);
	}
	public boolean validateSheets(List<Sheet> sheets) {
		for(Sheet sheet : sheets) {
			Map<String, Integer> columnCounts = getColumnCounts(sheet);
			validateRequiredHeaders(sheet,columnCounts);
			validateColumnCount(sheet,columnCounts);
		}
		return getErrorMessages().size() == 0;
	}
	private void validateRequiredHeaders(Sheet sheet, Map<String, Integer> columnCounts) {
		List<ImportHeader> importHeaders = dbHelperImpl.getImportHeaders();
        String sheetName = sheet.getRevisedName() != null ? sheet.getRevisedName() : sheet.getName();
		for(ImportHeader importHeader : importHeaders) {
			if(importHeader.isRequired()) {
				if(!columnCounts.containsKey(importHeader.getName())) {
					StringBuffer errorMessage = new StringBuffer();
					errorMessage.append(sheetName + ": ");
					errorMessage.append("You must include \"^\" when importing. Please fix the file and try again.");
					
					String error = errorMessage.toString().replace(Constants.STRING_REPLACE_CHAR, importHeader.getName());
					addErrorMessage(sheetName,error);
				}
			}
		}
	}
	private void validateColumnCount(Sheet sheet, Map<String, Integer> columnCounts) {
        String sheetName = sheet.getRevisedName() != null ? sheet.getRevisedName() : sheet.getName();
        if(!getErrorMessages().containsKey(sheetName)) {
            for(Map.Entry<String, Integer> columnCount : columnCounts.entrySet()){
                if(columnCount.getValue() > 1) {
                    StringBuffer errorMessage = new StringBuffer();
                    errorMessage.append(sheetName + ": ");
                    errorMessage.append("There are duplicate columns with the name \"^\". Column names must be unique.");

                    String error = errorMessage.toString().replace(Constants.STRING_REPLACE_CHAR, columnCount.getKey());
                    addErrorMessage(sheetName,error);
                }
            }
        }

	}
	private Map<String, Integer> getColumnCounts(Sheet sheet) {
		
		Map<String, Integer> columnHeaderCount = new HashMap<String, Integer>();
		for(Map.Entry<String, Cell> columnHeader : sheet.getColumnHeaders().entrySet()){ 
			String lcColumn = columnHeader.getValue().getStringValue();
			if(columnHeaderCount.containsKey(lcColumn)) {
				Integer count = columnHeaderCount.get(lcColumn);
				count++;
				columnHeaderCount.put(lcColumn, count);
			}
			else {
				columnHeaderCount.put(lcColumn, 1);	
			}
		}
		return columnHeaderCount;
	}

}

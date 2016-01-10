package com.honeydewit.converter;

import android.content.Context;
import android.util.Log;

import com.honeydewit.Constants;
import com.honeydewit.pojos.BasicList;
import com.honeydewit.pojos.ListItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WorkbookToListsConverter extends BaseImportParser  {

	private Row currentRow = null;
	private Map<String, String> columnHeaders = null;
	private Workbook workbook;

	public WorkbookToListsConverter(Context context, Workbook workbook){
		this.workbook = workbook;
		this.columnHeaders = new HashMap<String, String>();
	}
	public static String generateListName(String workbookName, String sheetName) {
		return sheetName;
	}
	public Map<BasicList, List<ListItem>> convert() {
		Map<BasicList, List<ListItem>> lists = new HashMap<BasicList, List<ListItem>>();
		for(Sheet sheet : workbook.getSheets()) {
			try {
				Sheet currentSheet = sheet;
                String sheetName = sheet.getRevisedName() != null ? sheet.getRevisedName() :sheet.getName();
                BasicList list = initList(workbook.getName(), sheetName);
				lists.put(list, convertSheet(workbook, currentSheet));
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return lists;
	}
	private BasicList initList(String workbookName, String sheetName) throws SQLException {
		BasicList list = new BasicList();
		list.setListName(generateListName(workbookName, sheetName));
		list.setListTypeId(Constants.SHOPPING_LIST_TYPE_CDE);
		list.setIsShowErrorInd(true);
		list.setIsShowErrorDialogInd(true);
		return list;
	}
	private List<ListItem> convertSheet(Workbook workbook, Sheet sheet) throws SQLException  {
        List<ListItem> listItems = new ArrayList<ListItem>();
        int listItemRowNumber = 0;
        for(Map.Entry<Integer,Row> entry : sheet.getRows().entrySet()) {
            Row row = entry.getValue();

            if (entry.getKey() == 1) {
                populateColumnHeaders(row);
            } else if (!row.isEmptyRow()) {

                listItems.add(convertRow(row, listItemRowNumber));
                listItemRowNumber += 1;
            }
        }
		return listItems;
	}
	private ListItem convertRow(Row row, Integer listItemRowNumber) {
		ListItem listItem = new ListItem();
        //default quantity
        listItem.setQuantity(1D);
        listItem.setImportRow(row.getRowNumber());
		listItem.setRowNumber(listItemRowNumber);

		
		for(Map.Entry<String, Cell> cell : row.getCells().entrySet()) {
			String columnName = columnHeaders.get(cell.getValue().getColumn());
			if(columnName != null) {
				String cellValue = cell.getValue().getStringValue() != null ? cell.getValue().getStringValue().trim() : null;
				processCell(columnName, cellValue, listItem);
			}
			else {
				System.out.println("Cell is empty: " + cell.getValue().getColumn());
			}
		}
        Log.d(this.getClass().getName(),"ListItem: " + listItem.getName() + "Row Number: " + listItem.getRowNumber());
		return listItem;
	}
	
	public void populateColumnHeaders(Row row) {
		for(Map.Entry<String, Cell> cell : row.getCells().entrySet()) {
			this.columnHeaders.put(cell.getKey(), cell.getValue().getStringValue());
		}	
	}
	
}

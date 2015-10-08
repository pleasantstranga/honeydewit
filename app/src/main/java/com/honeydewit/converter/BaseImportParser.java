package com.honeydewit.converter;

import com.honeydewit.pojos.BasicList;
import com.honeydewit.pojos.ListItem;

import java.util.List;
import java.util.Map;

public abstract class BaseImportParser {
	
	private Map<String, String> columnHeaders = null;
	public abstract  Map<BasicList, List<ListItem>> convert();
    public abstract void populateColumnHeaders(Row row);

	public static void processCell(String columnName, String cellValue, ListItem listItem) {
		
		if(columnName.equalsIgnoreCase("item")) {
			listItem.setName(cellValue);
			return;
		}
		if(columnName.equalsIgnoreCase("quantity")) {
			try {
                listItem.setQuantity(Double.valueOf(cellValue));
			}
			catch(Exception e) {
				listItem.setQuantity(0D);
			}
			return;
		}
		if(columnName.equalsIgnoreCase("description")) {
			listItem.setDescription(cellValue);
			return;
		}
		if(columnName.equalsIgnoreCase("price")) {
			try {
				listItem.setPricePerUnit(Double.valueOf(cellValue));
			}
			catch(NumberFormatException nfe) {
				listItem.setPricePerUnit(null);
			}
			return;
		}
		if(columnName.equalsIgnoreCase("unit")) {
			listItem.setUnitType(String.valueOf(cellValue));
			return;
		}
		if(columnName.equalsIgnoreCase("discount")) {
			try {
				listItem.setDiscountCoupon(Double.valueOf(cellValue));
			}
			catch(NumberFormatException nfe) {
				listItem.setDiscountCoupon(null);
			}
			return;
		}
		if(columnName.equalsIgnoreCase("discount_typ")) {
			listItem.setDiscountType(cellValue);
		}
	}
	public Map<String, String> getColumnHeaders() {
		return columnHeaders;
	}

	public void setColumnHeaders(Map<String, String> columnHeaders) {
		this.columnHeaders = columnHeaders;
	}
	
	
	
}

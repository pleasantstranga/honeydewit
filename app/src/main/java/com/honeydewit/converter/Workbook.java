package com.honeydewit.converter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Workbook implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1128037469397096006L;
	private String name;
	private List<Sheet> sheets = new ArrayList<Sheet>();

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Sheet> getSheets() {
		return sheets;
	}
	public void setSheets(List<Sheet> sheets) {
		this.sheets = sheets;
	}
	public void addSheet(Sheet sheet) {
		sheet.setWorkbook(this);
		sheets.add(sheet);
		
	}

	public Sheet getSheetById(String id) {
		for(Sheet sheet : sheets) {
			if(sheet.getId().equals(id)) return sheet;

		}
		return null;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
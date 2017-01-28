package com.ajbtechnologies.converter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Column implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String columnNumber;
	String columnName;
	Map<String, Cell> cells = new HashMap<String, Cell>();
	
	public String getColumnNumber() {
		return columnNumber;
	}
	public void setColumnNumber(String columnNumber) {
		this.columnNumber = columnNumber;
	}
	public Map<String, Cell> getCells() {
		return cells;
	}
	public void setCells(Map<String, Cell> cells) {
		this.cells = cells;
	}
	public void addCell(Cell cell) {
		cells.put(cell.getCellNumber(), cell);
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
}

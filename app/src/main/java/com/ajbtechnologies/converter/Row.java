package com.ajbtechnologies.converter;

import com.ajbtechnologies.utils.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class Row  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1398777796402214860L;
	Integer rowNumber;
	Map<String, Cell> cells = new HashMap<String, Cell>();
	
	
	public Integer getRowNumber() {
		return rowNumber;
	}
	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}
	public Map<String, Cell> getCells() {
		return cells;
	}
	public void addCell(Cell cell) {
		cells.put(cell.getColumn(), cell);
	}
	@Override
	public String toString() {
		return "Row Number: " + rowNumber + " Cells: " + cells.size();
	}
	public boolean isEmptyRow() {
		boolean isAllEmpty = true;
		for(Cell cell : cells.values()) {
			if(!StringUtils.isEmpty(cell.getStringValue().trim())) {
				isAllEmpty = false;
				break;
			}
		}
		return isAllEmpty;
	}
	
}

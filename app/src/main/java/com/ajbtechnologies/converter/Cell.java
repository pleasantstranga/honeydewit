package com.ajbtechnologies.converter;

import java.io.Serializable;

public class Cell implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cellNumber;
	private Object type;
	private Object value;
	private String column;
	private Integer rowNumber;
	
	public String getCellNumber() {
		return cellNumber;
	}
	public void setCellNumber(String cellNumber) {
		this.cellNumber = cellNumber;
		this.column = cellNumber.replaceAll("\\d*$", "");
		this.rowNumber = Integer.valueOf(cellNumber.replaceAll("[^\\d.]", ""));
	}
	public Object getType() {
		return type;
	}
	public void setType(Object type) {
		this.type = type;
	}
	private Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getColumn() {
		return column;
	}
	public Integer getRowNumber() {
		return rowNumber;
	}
	public String getStringValue() {
		if(null == getValue()) {
			return "";
		}
		return String.valueOf(value);
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Value: " + value + "\n");
		sb.append("Cell Number: " + cellNumber + "\n");
		sb.append("Column: " + column + "\n");
		sb.append("Row Number: " + rowNumber + "\n");
		sb.append("Type: " + type + "\n");
		return sb.toString();
	}
}

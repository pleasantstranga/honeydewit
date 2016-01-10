package com.honeydewit.converter;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sheet implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 761731639431685835L;
	private String name;
	private String id;
	private Integer index;
	@SuppressLint("UseSparseArrays")
	private Map<Integer,Row> rows = new TreeMap<>();
	private Pattern pattern = Pattern.compile("\\d+"); 
	private Workbook workbook;
	private String revisedName = null;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public Map<Integer, Row> getRows() {
		
		return rows;
	}
	public void setRows(Map<Integer,Row> rows) {
		this.rows = rows;
	}
	public void addRow(Row row) {
		this.rows.put(row.getRowNumber(), row);
	}
	public Cell getCell(String cellId) {
		String cellClmn = null;
		Integer rowNum = null;
		Matcher m = pattern.matcher(cellId);
		if(m.find()) {
			rowNum = Integer.parseInt(m.group());
			cellClmn = cellId.substring(0,cellId.indexOf(m.group()));
		}
		Row row = getRows().get(rowNum);
		if(row == null) {
			row = new Row();
			row.setRowNumber(rowNum);
			addRow(row);
		}
		Cell cell = row.getCells().get(cellClmn);
		if(cell == null) {
			cell = new Cell();
			cell.setCellNumber(cellClmn);
			row.addCell(cell);
		}
		return cell;
		
	}
	@Override
	public String toString() {
		return "Name: " + name + " ID: " + id + " Index: " + index + " Rows: " + rows.size();
 		
	}
	public Workbook getWorkbook() {
		return workbook;
	}
	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}
	public String getRevisedName() {
		return revisedName;
	}
	public void setRevisedName(String revisedName) {
		this.revisedName = revisedName;
	}
	public Map<String, Cell> getColumnHeaders() {
		return getRows().get(1).getCells();

	}
    public Row getRow(Integer rowNumber) {
        if(rowNumber == null || !getRows().containsKey(rowNumber)) {
            return new Row();
        }
        else {
           return getRows().get(rowNumber);
        }
    }
}

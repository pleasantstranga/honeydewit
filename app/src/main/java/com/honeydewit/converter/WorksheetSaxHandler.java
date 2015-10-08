package com.honeydewit.converter;

import android.util.SparseArray;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.Map;

public class WorksheetSaxHandler extends DefaultHandler {
	private static final String INDEX = "r";
	private static final String VALUE = "v";
	private static final String STRING = "s";
	private static final String TYPE = "t";
	private static final String ROW = "row";
	private static final String CELL = "c";
	private static final String HYPERLINK = "hyperlink";
	private SparseArray<String> sharedStrings;
	private Map<String,String> relationships;
	private Sheet currentSheet;
	private Row currentRow;
	private Cell currentCell;
	private String currentTag= null;
	private boolean ignoreEmptyRows = false;
	
	public WorksheetSaxHandler(Sheet sheet, SparseArray<String> sharedStrings, Map<String, String> relationships) {
		this.currentSheet = sheet;
		this.sharedStrings = sharedStrings;
		this.relationships = relationships;

	}
    public WorksheetSaxHandler(Sheet sheet, SparseArray<String> sharedStrings, Map<String, String> relationships, boolean ignoreEmptyRows) {
        this.currentSheet = sheet;
        this.sharedStrings = sharedStrings;
        this.relationships = relationships;
        this.ignoreEmptyRows = ignoreEmptyRows;

    }
	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		this.currentTag = qName;
		Map<String, String> attributeMap = getAttributeMap(attributes);
		if(qName.equals(ROW)) {
			currentRow = new Row();
			currentRow.setRowNumber(Integer.parseInt(attributeMap.get(INDEX)));
		}
		else if(qName.equals(CELL)) {
			
			currentCell = new Cell();
			currentCell.setCellNumber(attributeMap.get(INDEX));
			String type = attributeMap.get(TYPE);
			if(type != null && type.equals(STRING)) {
				currentCell.setType(String.class);
			}
			else {
				currentCell.setType(Double.class);
			}
		}
		else if(currentTag.equals(HYPERLINK)) {
			String cellNum = attributeMap.get("ref");
			String relationship = attributeMap.get("id");
			String cellValue = relationships.get(relationship);
			
			currentSheet.getCell(cellNum).setValue(cellValue);
		}
	}

	public void endElement(String uri, String localName,String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if(qName.equals(ROW)) {
            if(!ignoreEmptyRows || (ignoreEmptyRows && !currentRow.isEmptyRow())) {
                currentSheet.addRow(currentRow);
            }
		}
		if(qName.equals(VALUE)) {
			currentRow.addCell(currentCell);
		}
	}

	public void characters(char ch[], int start, int length) throws SAXException {
		super.characters(ch, start, length);
		try {
			String value = getTagValue(ch, start, length);
			if(currentTag.equals(VALUE)) {
				
				if(currentCell.getType().equals(String.class)) {
					int index = Integer.parseInt(value);
					currentCell.setValue(sharedStrings.get(index));
				}
				else {
					currentCell.setValue(Double.parseDouble(value));
				}
			}
			
		}
		catch(NumberFormatException nfe) {
			try {
				String value = getTagValue(ch, start, length);
				currentCell.setValue(value);
			}
			catch(Exception e) {
				nfe.printStackTrace();
			}
			
		}
		
	}
	private String getTagValue(char[] ch, int start, int length) {
		StringBuilder sb = new StringBuilder();
		for(int index = start; index < length;index++) {
			sb.append(ch[index]);
		}
		return sb.toString();
	}
	public Map<String, String> getAttributeMap(Attributes attributes) {
		Map<String,String> map = new HashMap<String, String>();
		for (int i = 0; i < attributes.getLength(); i++) {
		    String attName = attributes.getLocalName(i);
		    String attValue = attributes.getValue(i);
		    map.put(attName,attValue);
		
		}
		return map;
	}
	
}

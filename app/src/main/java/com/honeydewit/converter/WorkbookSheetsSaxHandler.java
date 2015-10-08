package com.honeydewit.converter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

public class WorkbookSheetsSaxHandler extends DefaultHandler {

	private static final String ID = "id";
	private static final String SHEET_ID = "sheetId";
	private static final String NAME = "name";
	private static final String SHEET = "sheet";


	private Sheet sheet;
	private List<Sheet> sheets;
	
	public WorkbookSheetsSaxHandler(List<Sheet> sheets) {
		this.sheets = sheets;
	}
	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		
		if(qName.equals(SHEET)) {
			sheet = new Sheet();
			for (int i = 0; i < attributes.getLength(); i++) {
			    String attName = attributes.getLocalName(i);
			    String attValue = attributes.getValue(i);
			    if(attName.equalsIgnoreCase(NAME)) {
			    	sheet.setName(attValue);
			    }
			    else if(attName.equalsIgnoreCase(SHEET_ID)) {
			    	sheet.setIndex(Integer.valueOf(attValue));
			    }
			    else if(attName.equalsIgnoreCase(ID)) {
			    	sheet.setId(attValue);
			    }
			}
		}
	}

	public void endElement(String uri, String localName,String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if(qName.equals(SHEET)) {
			sheets.add(sheet);
		}
	}

	public void characters(char ch[], int start, int length) throws SAXException {
		super.characters(ch, start, length);
	}
}

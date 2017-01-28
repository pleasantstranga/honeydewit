package com.ajbtechnologies.converter;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Map;

public class WorkbookRelationshipsSaxHandler extends DefaultHandler {

	
	private static final String ID = "Id";
	private static final String TARGET = "Target";
	private static final String RELATIONSHIP = "Relationship";
	private Map<String,String> relationships;
	
	public WorkbookRelationshipsSaxHandler(Map<String, String> relationships) {
		this.relationships = relationships;
	}
	
	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if(qName.equals(RELATIONSHIP)) {
			
			String id = attributes.getValue(ID);
			String target = attributes.getValue(TARGET);
			relationships.put(id, target);
		}
	}

	public void endElement(String uri, String localName,String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		
	}

	public void characters(char ch[], int start, int length) throws SAXException {
		super.characters(ch, start, length);
		
	}
}

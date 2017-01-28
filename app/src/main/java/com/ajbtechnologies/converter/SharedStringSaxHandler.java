package com.ajbtechnologies.converter;

import android.util.SparseArray;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SharedStringSaxHandler extends DefaultHandler {

	
	private static final String TEXT = "t";
	private static final String STRING = "si";
	private SparseArray<String> sharedStrings;
	private Integer index = -1;
	private StringBuilder stringBuilder;
	private String currentQName = "";
	public SharedStringSaxHandler(SparseArray<String> sharedStrings) {
		this.sharedStrings = sharedStrings;
	}
	
	public void startElement(String uri, String localName,String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		currentQName = qName;
		if(qName.equals(STRING)) {
			stringBuilder = new StringBuilder();
		}
		if(qName.equals(TEXT)) {
			index++;
		}
	}

	public void endElement(String uri, String localName,String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if(qName.equals(STRING)) {
			sharedStrings.put(index, stringBuilder.toString());
		}
	}

	public void characters(char ch[], int start, int length) throws SAXException {
		super.characters(ch, start, length);
		if(currentQName.equals(TEXT)) {
			for(int index = start; index < length;index++) {
				stringBuilder.append(ch[index]);
			}
		}
	}
}

package com.ajbtechnologies.converter;

import android.util.SparseArray;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

class XLSXWorkbookFactory {

	public static Workbook createWorkbook(File tempDirectory, Map<String,String> checkedSheetsNames)  throws Exception{
		WorkbookGenerator generator = new WorkbookGenerator(tempDirectory,checkedSheetsNames);
		return generator.generateWorkbook();

	}
	
	public static SparseArray<String> getWorkbookSheetNames(File directory) throws ParserConfigurationException, SAXException, IOException  {
		List<Sheet> worksheets = new ArrayList<Sheet>();
		BufferedInputStream inputStream;
	
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			inputStream = new BufferedInputStream(new FileInputStream(directory));
			DefaultHandler sh = new WorkbookSheetsSaxHandler(worksheets);
			saxParser.parse(inputStream, sh);
		
		 SparseArray<String> sheetIndexes = new SparseArray<String>();
		for(Sheet sheet : worksheets) {
			sheetIndexes.put(sheet.getIndex(), sheet.getName());
		}
		return sheetIndexes;
	}
	private static class WorkbookGenerator {
		private SAXParser saxParser = null;
		private Workbook workbook = null;
		private SparseArray<String> sharedStrings = null;
		private Map<String,String> checkedSheetsNames = null;
		private File tempDirectory = null;
		private static final String WKSHTS_DRCTY = "xl/worksheets/";
		private static final String WKBK_REL = "workbook.xml.rels";
		private static final String SHARED_STRINGS_XML = "sharedStrings.xml";
		private static final String WORKBOOK_XML = "workbook.xml";

        private Map<String, String> workbookRelationships;

		public WorkbookGenerator(File tempDirectory, Map<String,String> checkedSheetsNames) throws Exception {
			this.saxParser = SAXParserFactory.newInstance().newSAXParser();
			this.workbook = new Workbook();
			this.sharedStrings = new SparseArray<String>();
			this.tempDirectory = tempDirectory;
			this.checkedSheetsNames = checkedSheetsNames;
            this.workbookRelationships = new HashMap<String, String>();
        }
		public Workbook generateWorkbook() throws Exception {
			walk(tempDirectory);
			walkSheets(tempDirectory);
			return workbook;
		}
		
		private void walk(File root) throws Exception {	
			File[] list = root.listFiles();
		
			if (list == null) return;
			for ( File f : list ) {
				if ( f.isDirectory() ) {
					walk(f);
				}
				else {
					readXml(f);
				}
			}
		}
		private void walkSheets(File root) throws Exception {
			File[] sheets = new File(root,WKSHTS_DRCTY).listFiles();
            if (sheets == null) return;

            for(Sheet sheet : workbook.getSheets()) {
                for ( File sheetFile : sheets ) {
                    String sheetName = workbookRelationships.get(sheet.getId());
                    String sheetFileName = sheetFile.getName();
                    if(sheetName.contains(sheetFileName)) {
                        processSheet(sheet,sheetFile);
                        //workbook.addSheet(sheet);
                    }
                }
            }

		}
		private void readXml(File file) throws SAXException, IOException {
			//must build shared strings and sheets first before building workbook
			BufferedInputStream inputStream = null;
			try {
				if(file.getName().endsWith(SHARED_STRINGS_XML)) {
					inputStream = new BufferedInputStream(new FileInputStream(file));
					DefaultHandler sh = new SharedStringSaxHandler(sharedStrings);
					saxParser.parse( inputStream, sh);
				}
				else if(file.getName().endsWith(WORKBOOK_XML)) {
					inputStream = new BufferedInputStream(new FileInputStream(file));
					DefaultHandler sh = new WorkbookSaxHandler(workbook, checkedSheetsNames);
					saxParser.parse(inputStream, sh);
				}
				else if(file.getName().equals(WKBK_REL)) {
					inputStream = new BufferedInputStream(new FileInputStream(file));
					DefaultHandler sh = new WorkbookRelationshipsSaxHandler(workbookRelationships);
					saxParser.parse(inputStream, sh);
				}
			}
			
			finally {
				if(inputStream != null) {
					inputStream.close();
				}
			}
		}
		private void processSheet(Sheet sheet, File file) throws Exception {
				BufferedInputStream inputStream = null;
				try {
					inputStream = new BufferedInputStream(new FileInputStream(file));
                    DefaultHandler sh = new WorksheetSaxHandler(sheet, sharedStrings, new HashMap<String, String>());
				    saxParser.parse(inputStream, sh);
				}
				finally {
					if(inputStream != null) {
						inputStream.close();
					}
				}
			}
	}

}
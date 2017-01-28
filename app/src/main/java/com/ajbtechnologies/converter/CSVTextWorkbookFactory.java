package com.ajbtechnologies.converter;

import com.ajbtechnologies.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import static java.lang.Math.floor;
import static java.lang.Math.log;

public class CSVTextWorkbookFactory  {

	String columnLine;
	InputStream fileInput;
	Workbook workbook;
	
	public static Workbook createWorkbook(File xlsTempFile,String fileName)  throws Exception{
		WorkbookGenerator generator = new WorkbookGenerator(xlsTempFile,fileName);
		return generator.generateWorkbook();

	}
	
	private static class WorkbookGenerator {
		private File file;
        private String fileName;
		public WorkbookGenerator(File file, String fileName) {
            this.fileName = fileName;
            this.file = file;
		}
		private Workbook generateWorkbook() {
			BufferedReader br = null;
			Workbook workbook = new Workbook();
			Sheet sheet = new Sheet();
			sheet.setId(file.getName());
			sheet.setIndex(1);
			sheet.setName(fileName);
			workbook.addSheet(sheet);
			try {
				br = new BufferedReader(new FileReader(file));
				String line;
				int rowNumber = 0;
				while ((line = br.readLine()) != null) {
					rowNumber+=1;
					sheet.addRow(processRow(line, rowNumber));
					
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				try {
					br.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			return workbook;
		}
		private Row processRow(String line, int rowNumber) {
			
			Row row = new Row();
			row.setRowNumber(rowNumber);
			String[] cellsValues =  line.trim().split(Constants.SPLIT);
			int cellNumber = 0;
			for(String cellValue : cellsValues) {
				cellNumber +=1;
				processCell(rowNumber, row, cellNumber, cellValue);
			}
			return row;
			
		}
		private void processCell(int rowNumber, Row row, int cellNumber,
				String cellValue) {
			Cell cell = new Cell();
			String cellColumnIndex = generateColumnString(cellNumber) + rowNumber;
			cell.setCellNumber(cellColumnIndex);
			cell.setType(String.class);
			cell.setValue(cellValue);
			row.addCell(cell);
		}
		private String generateColumnString(int columnNumber) {
		    char[] buf = new char[(int) floor(log(25 * (columnNumber + 1)) / log(26))];
		    for (int i = buf.length - 1; i >= 0; i--) {
		    	columnNumber--;
		        buf[i] = (char) ('A' + columnNumber % 26);
		        columnNumber /= 26;
		    }
		    StringBuffer sb = new StringBuffer();
		    for(char c : buf) {
		    	sb.append(c);
		    }
		    return sb.toString();
		}
	}
	
	
}

package com.honeydewit.converter;

import android.util.Log;
import android.util.SparseArray;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.CellReferenceHelper;
import jxl.CellType;
import jxl.read.biff.BiffException;


class XLSWorkbookFactoryJExcel { 
	
	public static Workbook createWorkbook(File xlsTempFile)  throws Exception{ 
		WorkbookGenerator generator = new WorkbookGenerator(xlsTempFile);	
		return generator.generateWorkbook();

	}
	public static Workbook createWorkbook(File xlsTempFile, Map<String,String> checkedSheetsNames)  throws Exception{
		WorkbookGenerator generator = new WorkbookGenerator(xlsTempFile,checkedSheetsNames);
		return generator.generateWorkbook();

	}
	public static  SparseArray<String> getWorkbookSheetNames(File tempFile) throws IOException, BiffException {
		long tStart = System.currentTimeMillis();
		SparseArray<String> sheetIndexes = new SparseArray<String>();
		InputStream is = null;
		jxl.Workbook hssfWorkbook = null;
		try {
			
			is = new BufferedInputStream(new FileInputStream(tempFile),2048);
			hssfWorkbook = jxl.Workbook.getWorkbook(is);
			
			List<Sheet> worksheets = new ArrayList<Sheet>();
			int numSheets = hssfWorkbook.getNumberOfSheets();
			for(int index = 0;index<numSheets;index++) {
				jxl.Sheet hssfSheet = hssfWorkbook.getSheet(index);
				Sheet sheet  = new Sheet();
				sheet.setIndex(index);
				sheet.setName(hssfSheet.getName());
				worksheets.add(sheet);
			}

			for(Sheet sheet : worksheets) {
				sheetIndexes.put(sheet.getIndex(), sheet.getName());
			}
		}
		finally {
			if(is != null) {
				is.close();	
			}
			if(hssfWorkbook != null) {
				hssfWorkbook.close();	
			}
		}
		long end = System.currentTimeMillis();
		double elapsedSeconds = (end-tStart) / 1000.0;
		Log.d("XlsWorkbook","Elapsed Time Show Seets: " + elapsedSeconds);
		return sheetIndexes;
	}
	private static class WorkbookGenerator {
		private Workbook workbook = null;
		private File xlsTempFile = null;
		private Map<String,String> checkedSheetsNames;
		
		
		public WorkbookGenerator(File xlsTempFile) {
			workbook = new Workbook();
			workbook.setName(xlsTempFile.getName());
			this.xlsTempFile = xlsTempFile;
		}
		public WorkbookGenerator(File xlsTempFile,Map<String,String> checkedSheetsNames) {
			workbook = new Workbook();
			workbook.setName(xlsTempFile.getName());
			this.xlsTempFile = xlsTempFile;
			this.checkedSheetsNames = checkedSheetsNames;
		}
		public Workbook generateWorkbook() {
			long start = System.currentTimeMillis();
			jxl.Workbook hssfWorkbook = null;
			BufferedInputStream inputStream = null;
			try {
				inputStream = new BufferedInputStream(new FileInputStream(xlsTempFile),2048);
				hssfWorkbook = jxl.Workbook.getWorkbook(inputStream);
				for(int sheetIndex = 0; sheetIndex < hssfWorkbook.getNumberOfSheets();sheetIndex++)  {
                    jxl.Sheet sheet = hssfWorkbook.getSheet(sheetIndex);
                    if(checkedSheetsNames.containsKey(sheet.getName())) {
                        workbook.addSheet(processSheet(sheet));
                    }
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				try {
					if(inputStream != null) {
						inputStream.close();
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
			}
			long end = System.currentTimeMillis();
			double elapsedSeconds = (end - start) / 1000.0;
			Log.d("XlsWorkbook","Elapsed Time Generate Workbook: " + elapsedSeconds);
			return workbook;
		}
		private Sheet processSheet(jxl.Sheet hssfSheet) throws Exception {

			Sheet sheet = new Sheet();
			sheet.setId("");

            if(checkedSheetsNames != null) {
                sheet.setName(checkedSheetsNames.get(hssfSheet.getName()));
            }
			else {
                sheet.setName(hssfSheet.getName());
            }

			int numRow = hssfSheet.getRows();
			for(int x = 0;x<numRow;x++) {
                Row row = processRow(hssfSheet.getRow(x));
                if(row!=null && row.getRowNumber() != null) {
                    sheet.addRow(row);
                }

			}
			return sheet;
		}
		
		private String geStringValueOfCell(jxl.Cell cell) throws Exception {
			if(cell != null) {
				return String.valueOf(cell.getContents());
			}
			return "";
		}
		private Object getCellType(jxl.Cell cell) throws Exception {
			
				if(cell.getType() == CellType.BOOLEAN || cell.getType() == CellType.BOOLEAN_FORMULA) {
					return Boolean.class;
				}
				if(cell.getType() == CellType.DATE || cell.getType() == CellType.DATE_FORMULA) {
					return Date.class;
				}
				if(cell.getType() == CellType.EMPTY || cell.getType() == CellType.STRING_FORMULA ||
						cell.getType() == CellType.ERROR || cell.getType() == CellType.FORMULA_ERROR ||
						cell.getType() == CellType.LABEL) {
					return String.class;
				}
				if(cell.getType() == CellType.NUMBER || cell.getType() == CellType.NUMBER_FORMULA) {
					return Double.class;
				}
				else {
					return String.class;
				}
		}
	
		private com.honeydewit.converter.Row processRow(jxl.Cell[] hssfRow) {
            com.honeydewit.converter.Row row = new com.honeydewit.converter.Row();
            try {
                for(int x = 0; x< hssfRow.length;x++) {
                    com.honeydewit.converter.Cell cell = new com.honeydewit.converter.Cell();

                    Cell hssfCell = hssfRow[x];
                    cell.setCellNumber(CellReferenceHelper.getCellReference(hssfCell));
                    cell.setType(getCellType(hssfCell));
                    cell.setValue(geStringValueOfCell(hssfCell));
                    Integer rowNumber = hssfCell.getRow()+1;
                    row.setRowNumber(rowNumber);
                    row.addCell(cell);

                }
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            return row;
		}
	}
}



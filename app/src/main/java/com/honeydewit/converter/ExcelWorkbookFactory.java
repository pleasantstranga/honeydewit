package com.honeydewit.converter;

import android.annotation.SuppressLint;
import android.util.SparseArray;

import com.honeydewit.Constants;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import jxl.read.biff.BiffException;


@SuppressLint("DefaultLocale")
public class ExcelWorkbookFactory {
	
	@SuppressLint("DefaultLocale")
	public static Workbook createWorkbook(File file, String fileName, Map<String,String> checkedSheetsNames) throws Exception {
		Workbook wb = null;
		if(fileName.toLowerCase().endsWith(Constants.XLSX_EXT)) {
			wb = XLSXWorkbookFactory.createWorkbook(file,checkedSheetsNames);
		}
		else if(fileName.toLowerCase().endsWith(Constants.XLS_EXT)) {
			wb = XLSWorkbookFactoryJExcel.createWorkbook(file,checkedSheetsNames);
		}
		if(wb != null) {
			wb.setName(fileName);
		}
      		return wb;
	}
	public static SparseArray<String> getSheetNames(File tempFile) throws IOException, ParserConfigurationException, SAXException, BiffException {
		SparseArray<String> sheetIndexes = null;
		if(tempFile.getAbsolutePath().endsWith(Constants.XML_EXT)) {
			sheetIndexes = XLSXWorkbookFactory.getWorkbookSheetNames(tempFile);
		}
		else if(tempFile.getAbsolutePath().endsWith(Constants.XLS_EXT)) {
			sheetIndexes = XLSWorkbookFactoryJExcel.getWorkbookSheetNames(tempFile);
		}
		return sheetIndexes;
	}
}

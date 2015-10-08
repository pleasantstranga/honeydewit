package com.honeydewit.pagination;

import android.annotation.SuppressLint;

import com.honeydewit.ImportedList;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Pages extends HashMap<Integer, Page> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7353050905816984961L;
	private int currentPage;

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	
	@SuppressLint("UseSparseArrays")
	public Map<Integer, String> getCheckedItemsOnPages() {
		Map<Integer,String> checkedSheetsIndexes = null;
		for(Map.Entry<Integer, Page> page : this.entrySet()) {
			for(ImportedList info : page.getValue().getSheetPages()) {
				if(info.isChecked()) {
					if(null == checkedSheetsIndexes) {
						checkedSheetsIndexes = new HashMap<Integer, String>();
					}
					String sheetName = info.getCurrentName();
					if(info.getNewName() != null && !sheetName.equals(info.getNewName())) {
						sheetName = info.getNewName();
					}
					checkedSheetsIndexes.put(info.getIndex(), sheetName);
				}
			}
		}
		return checkedSheetsIndexes;
	}
	
	
}

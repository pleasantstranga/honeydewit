package com.ajbtechnologies.pagination;

import com.ajbtechnologies.ImportedList;

import java.util.ArrayList;
import java.util.List;


public class Page {
		private List<ImportedList> sheetPages;
		private Integer pageIndex;
		
		public Page() {
			sheetPages = new ArrayList<ImportedList>();
		}
		public void addSheetInfo(ImportedList sheetInfo) {
			sheetPages.add(sheetInfo);
		}
		public void removeSheetInfo(ImportedList info) {
			sheetPages.remove(info);
		}
		public List<ImportedList> getSheetPages() {
			return sheetPages;
		}
		public void setSheetPages(List<ImportedList> sheetPages) {
			this.sheetPages = sheetPages;
		}
		public Integer getPageIndex() {
			return pageIndex;
		}
		public void setPageIndex(Integer pageIndex) {
			this.pageIndex = pageIndex;
		}
		
		
	}

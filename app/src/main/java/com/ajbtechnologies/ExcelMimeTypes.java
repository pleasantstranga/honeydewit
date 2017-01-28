package com.ajbtechnologies;


public enum ExcelMimeTypes {

	XLS("application/vnd.ms-excel"), 
	XLSX("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	 
	private String mimeType;
 
	ExcelMimeTypes(String mimeType) {
		this.mimeType = mimeType;
	}

	public String getMimeType() {
		return mimeType;
	}
	public static boolean isExcelMimeType(String mimeType) {
		for (ExcelMimeTypes mimeTypEnum : ExcelMimeTypes.values()) {
			  if( mimeTypEnum.getMimeType().equals(mimeType)) {
				  return true;
			  }
		}
		return false;
	}
}

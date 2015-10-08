package com.honeydewit;

import java.io.Serializable;

public class ImportedList implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -931496629487590247L;
	private boolean checked;
	private String currentName;
	private String newName;
	private Integer index;
	private boolean isFromExcelFile;
	private boolean isReplace;
	private boolean isRename;
	
	public boolean isReplace() {
		return isReplace;
	}

	public void setReplace(boolean isReplace) {
		this.isReplace = isReplace;
	}

	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getCurrentName() {
		return currentName;
	}

	public void setCurrentName(String currentName) {
		this.currentName = currentName;
	}

	public String getNewName() {
		return newName;
	}

	public void setNewName(String newName) {
		this.newName = newName;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public boolean isFromExcelFile() {
		return isFromExcelFile;
	}

	public void setFromExcelFile(boolean isFromExcelFile) {
		this.isFromExcelFile = isFromExcelFile;
	}

	public boolean isRename() {
		return isRename;
	}

	public void setRename(boolean isRename) {
		this.isRename = isRename;
	}
}
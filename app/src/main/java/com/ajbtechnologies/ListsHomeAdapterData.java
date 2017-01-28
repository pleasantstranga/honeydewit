package com.ajbtechnologies;

import java.io.Serializable;

public class ListsHomeAdapterData implements Serializable {
	private String listName;
	private Integer itemCount;
	private Integer errorCount;
	private Integer listId;
	private boolean isChecked;
	private int listTypeId;

	public int getListTypeId() {
		return listTypeId;
	}

	public void setListTypeId(int listTypeId) {
		this.listTypeId = listTypeId;
	}


	
	public Integer getListId() {
		return listId;
	}
	public void setListId(Integer listId) {
		this.listId = listId;
	}
	public Integer getErrorCount() {
		return errorCount;
	}
	public void setErrorCount(Integer errorCount) {
		this.errorCount = errorCount;
	}
	public String getListName() {
		return listName;
	}
	public void setListName(String listName) {
		this.listName = listName;
	}
	public Integer getItemCount() {
		return itemCount;
	}
	public void setItemCount(Integer count) {
		this.itemCount = count;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
}

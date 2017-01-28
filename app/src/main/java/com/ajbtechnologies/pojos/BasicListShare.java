package com.ajbtechnologies.pojos;

import com.ajbtechnologies.Constants;
import com.ajbtechnologies.ImportError;
import com.j256.ormlite.dao.ForeignCollection;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Root
public class BasicListShare extends BaseObject {
	
	private static final long serialVersionUID = 1L;
	public BasicListShare() {
		
	}
	@Attribute
	private String listName;
	
	@Attribute
	private int listTypeId;
	
	@Attribute
	private int enabled;
	
	@Attribute(required = false)
	private String dateFilled;
	
	@Attribute(required=false)
	private int store_id;
	
	@Attribute(required=false)
	private String description;
	
	@Attribute
	private Integer checked = 0;
	
	@Attribute
	private Integer isShowErrorInd = Constants.TRUE;
	
	@Attribute
	private Integer isShowErrorDialogInd = Constants.TRUE;
	
	@ElementList
    public List<ListItem> items = new ArrayList<ListItem>();
	
	@ElementList
	public List<ListEvent> events = new ArrayList<ListEvent>();
	
	@ElementList
	public List<ImportError> errors = new ArrayList<ImportError>();
	
	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public int getListTypeId() {
		return listTypeId;
	}

	public void setListTypeId(int listTypeId) {
		this.listTypeId = listTypeId;
	}

	public int getEnabled() {
		return enabled;
	}

	public void setEnabled(int enabled) {
		this.enabled = enabled;
	}

	public String getDateFilled() {
		return dateFilled;
	}

	public void setDateFilled(String dateFilled) {
		this.dateFilled = dateFilled;
	}

	public int getStore_id() {
		return store_id;
	}

	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getChecked() {
		return checked;
	}

	public void setChecked(Integer checked) {
		this.checked = checked;
	}

	public Integer getIsShowErrorInd() {
		return isShowErrorInd;
	}

	public void setIsShowErrorInd(Integer isShowErrorInd) {
		this.isShowErrorInd = isShowErrorInd;
	}

	public Integer getIsShowErrorDialogInd() {
		return isShowErrorDialogInd;
	}

	public void setIsShowErrorDialogInd(Integer isShowErrorDialogInd) {
		this.isShowErrorDialogInd = isShowErrorDialogInd;
	}

	public List<ListItem> getItems() {
		return items;
	}

	public void setItems(ForeignCollection<ListItem> items) {
		for(ListItem item : items) {
			getItems().add(item);
		}
	}
	
	public List<ListEvent> getEvents() {
		return events;
	}

	public void setEvents(Collection<ListEvent> events) {
		for(ListEvent item : events) {
			getEvents().add(item);
		}
	}

	public List<ImportError> getErrors() {
		return errors;
	}

	public void setErrors(Collection<ImportError> errors) {
		for(ImportError error : errors) {
			getErrors().add(error);
		}
	}
	
}

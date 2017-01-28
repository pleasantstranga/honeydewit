package com.ajbtechnologies.pojos;

import android.content.Context;

import com.ajbtechnologies.Constants;
import com.ajbtechnologies.Application;
import com.ajbtechnologies.ImportError;
import com.ajbtechnologies.converter.Sheet;
import com.ajbtechnologies.dataaccess.ListDaoImpl;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = BasicList.TABLE_NAME, daoClass = ListDaoImpl.class)
@Root
public class BasicList extends BaseObject {
	
	private static final long serialVersionUID = 1L;
	public static final String LIST_NAME_CLM = "LIST_NAME";
	public static final String LIST_TYPE_ID_CLM = "LIST_TYPE_ID";
	public static final String ENABLED_CLM = "ENABLED";
	public static final String DATE_FILLED_CLM = "DATE_FILLED";
	public static final String STORES_ID_CLM = "STORES_ID";
	public static final String DESC_CLM = "DESC";
	public static final String TABLE_NAME = "LISTS";
	public static final String ITEMS = "items";
	public static final String ERRORS = "errors";
	public static final String EVENTS = "events";
	public static final String LIST_EVENT_CLM = "LIST_EVENT_ID";
	public static final String CHECKED_CLM = "CHECKED";
	public static final String IS_SHOW_ERROR_IND_CLM = "IS_SHOW_ERROR_IND";
	public static final String IS_SHOW_ERROR_DIALOG_IND_CLM = "IS_SHOW_ERROR_DIALOG_IND";
	public static final String ROW_NUM_CMN = "ROW_NUM";

	public transient Sheet importSheet;
	
	@Attribute
	@DatabaseField(columnName = LIST_NAME_CLM)
	private String listName;
	
	@Attribute
	@DatabaseField(columnName = LIST_TYPE_ID_CLM)
	private int listTypeId;
	
	@Attribute
	@DatabaseField(columnName = ENABLED_CLM)
	private int enabled;
	
	@Attribute(required = false)
	@DatabaseField(columnName = DATE_FILLED_CLM)
	private String dateFilled;
	
	@Attribute(required=false)
	@DatabaseField(columnName = STORES_ID_CLM)
	private int store_id;
	
	@Attribute(required=false)
	@DatabaseField(columnName = DESC_CLM)
	private String description;
	
	@Attribute
	@DatabaseField(columnName = CHECKED_CLM)
	private Integer checked = 0;
	
	
	@Attribute
	@DatabaseField(columnName = IS_SHOW_ERROR_IND_CLM)
	private Integer isShowErrorInd = Constants.TRUE;
	
	@Attribute
	@DatabaseField(columnName = IS_SHOW_ERROR_DIALOG_IND_CLM)
	private Integer isShowErrorDialogInd = Constants.TRUE;


	@Attribute(required=false)
	@DatabaseField(columnName = ROW_NUM_CMN)
	private Integer rowNumber;

	@ElementList
	@ForeignCollectionField(eager = true, orderColumnName = ListItem.ROW_NUM_CMN, orderAscending = true)
    public ForeignCollection<ListItem> items;
	
	@ElementList
	@ForeignCollectionField(eager = false)
    ForeignCollection<ListEvent> events;
	
	@ElementList
	@ForeignCollectionField(eager = true)
	public ForeignCollection<ImportError> errors;
	
	
	public ForeignCollection<ListItem> getItems() {
		
		return items;
	}
	public List<ListItem> getItems(boolean showErrors) {
	    List<ListItem> listItems = new ArrayList<ListItem>();
		if(showErrors) {
			if(items != null) {
				for(ListItem item : items) {
					
						listItems.add(item);
					
				}
			}
		}
		else {
				for(ListItem item : items) {
					if(!item.hasErrors()) {
						listItems.add(item);
					}
				}
		
			return listItems;
		}
		return listItems;
	}
	public List<ImportError> getErrorsList() {
	    List<ImportError> errors = new ArrayList<ImportError>();
		
			if(getErrors() != null) {
				for(ImportError error : getErrors()) {
					
						errors.add(error);
					
				}
			}
			return errors;
		}
	public void setItems(ForeignCollection<ListItem> items) {
		this.items = items;
	}

	public BasicList() {
		
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
	
	public void addListEvents(Context context, List<ListEvent> listEvents) {
		if(events == null) {
			try {
				events = ((Application)context).getShoppingListDbHelper().getListDao().getEmptyForeignCollection(BasicList.EVENTS);
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		if(null != listEvents) {
			events.addAll(listEvents);
		}
		
	}
	public void addErrors(Context context, List<ImportError> errors) {
		if(this.errors == null) {
			try {
				this.errors = ((Application)context).getShoppingListDbHelper().getListDao().getEmptyForeignCollection(BasicList.ERRORS);
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		if(null != errors) {
			this.errors.addAll(errors);	
		}
		
	}
	public ForeignCollection<ListEvent> getEvents() {
		return events;
	}

	public void setEvents(ForeignCollection<ListEvent> events) {
		this.events = events;
	}
	public ListEvent getFirstEvent() {
		ListEvent event = null;
		if(null != events && events.size() > 0) {
			event = events.iterator().next();
		}
		return event;
	}
	public void addEvent(Context context, ListEvent listEvent) {
		if(events == null) {
			try {
				events = ((Application)context).getShoppingListDbHelper().getListDao().getEmptyForeignCollection("events");
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		events.add(listEvent);
	}
	public void removeEvent(ListEvent listEvent) {
		
		events.remove(listEvent);
		
	}
	public void reset() {
		 
	}
	public boolean isSerialized() {
		return get_id() != null;
	}

	public String getListName() {
		return listName;
	}

	public Integer getChecked() {
		return checked;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public void setChecked(Integer checked) {
		this.checked = checked;
	}

	public ForeignCollection<ImportError> getErrors() {
		return errors;
	}

	public void setErrors(ForeignCollection<ImportError> errors) {
		this.errors = errors;
	}
	public boolean isShowErrorInd() {
		return isShowErrorInd == Constants.TRUE;
	}

	public void setIsShowErrorInd(boolean isShowErrorInd) {
		this.isShowErrorInd = (isShowErrorInd) ? Constants.TRUE : Constants.FALSE;
	}
	public List<ListItem> getItemsWithErrors() {
		ArrayList<ListItem> items = new ArrayList<ListItem>(); 
		if(null != getItems()) {
			for(ListItem item : getItems()) {
		    	if(item.hasErrors()) {
		    		items.add(item);
		        }
		    }
		}
	    return items;
	}
	public List<String> getItemErrors(int itemId) {
	    for(ListItem item : getItems()) {
	    	if(item.get_id() == itemId) {
	    		return item.getErrorsAsStrings();
	        }
	    }
	    return null;
	}
	public Integer getIsShowErrorInd() {
		return isShowErrorInd;
	}
	public void setIsShowErrorInd(Integer isShowErrorInd) {
		this.isShowErrorInd = isShowErrorInd;
	}
	public boolean isShowErrorDialogInd() {
		return isShowErrorDialogInd == Constants.TRUE;
	}
	public void setIsShowErrorDialogInd(boolean isShowErrorDialogInd) {
		this.isShowErrorDialogInd = (isShowErrorDialogInd) ? Constants.TRUE : Constants.FALSE;
	}
	public Integer getIsShowErrorDialogInd() {
		return isShowErrorDialogInd;
	}
	public void setIsShowErrorDialogInd(Integer isShowErrorDialogInd) {
		this.isShowErrorDialogInd = isShowErrorDialogInd;
	}
	public void addListItem(ListItem item) {
		items.add(item);
	}
	public ListItem getItemById(Integer id) {
		for(ListItem item : items) {
			if(item.get_id().equals(id)) {
				return item;
			}
		}
		return null;
	}
	public int getNumberCheckedListItems() {
		int num = 0;
		for(ListItem item : getItems()) {
			if(item.getQuantity() != null && item.getQuantity() > 0) {
				num +=1;
			}
		}
		return num;
	}
	public double getCheckedItemsPrice() {
		double price = 0d;
		for(ListItem item : items) {
			if(item.isChecked()) {
				double itemTotalPrice = item.getPricePerUnit() * item.getQuantity();
				price += itemTotalPrice;
			}
		}
		return price;
		
	}
	public Sheet getImportSheet() {
		return importSheet;
	}
	public void setImportSheet(Sheet importSheet) {
		this.importSheet = importSheet;
	}
	public Integer getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}


}
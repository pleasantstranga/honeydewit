package com.honeydewit.pojos;

import com.honeydewit.Constants;
import com.honeydewit.ImportError;
import com.honeydewit.dataaccess.ListDaoImpl;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = BasicList.TABLE_NAME, daoClass = ListDaoImpl.class)
public class BasicListLight extends BaseObject {
	
	private static final long serialVersionUID = 1L;
	public static final String LIST_NAME_CLM = "LIST_NAME";
	public static final String LIST_TYPE_ID_CLM = "LIST_TYPE_ID";
	public static final String ENABLED_CLM = "ENABLED";
	public static final String DATE_FILLED_CLM = "DATE_FILLED";
	public static final String STORES_ID_CLM = "STORES_ID";
	public static final String DESC_CLM = "DESC";
	public static final String TABLE_NAME = "LISTS";
	public static final String ITEMS = "items";
	public static final String LIST_EVENT_CLM = "LIST_EVENT_ID";
	public static final String CHECKED_CLM = "CHECKED";
	public static final String IS_SHOW_ERROR_IND_CLM = "IS_SHOW_ERROR_IND";
	public static final String IS_SHOW_ERROR_DIALOG_IND_CLM = "IS_SHOW_ERROR_DIALOG_IND";
	
	@DatabaseField(columnName = LIST_NAME_CLM)
	private String listName;
	@DatabaseField(columnName = LIST_TYPE_ID_CLM)
	private int listTypeId;
	@DatabaseField(columnName = ENABLED_CLM)
	private int enabled;
	@DatabaseField(columnName = DESC_CLM)
	private String description;
	@DatabaseField(columnName = CHECKED_CLM)
	private Integer checked = 0;
	@ForeignCollectionField(eager = true)
    public ForeignCollection<ImportError> errors;
	@DatabaseField(columnName = IS_SHOW_ERROR_IND_CLM)
	private Integer isShowErrorInd = Constants.TRUE;
	@DatabaseField(columnName = IS_SHOW_ERROR_DIALOG_IND_CLM)
	private Integer isShowErrorDialogInd = Constants.TRUE;
	
	
	public BasicListLight() {
		
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
}

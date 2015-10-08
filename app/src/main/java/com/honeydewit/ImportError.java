package com.honeydewit;
import com.honeydewit.pojos.BaseObject;
import com.honeydewit.pojos.BasicList;
import com.honeydewit.pojos.ListItem;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = ImportError.TABLE_NAME )
public class ImportError extends BaseObject {
	
	private static final long serialVersionUID = 1L;
	public static final String TABLE_NAME = "IMPORT_ERRORS";
	public static final String LIST_ID_CLMN = "LIST_ID";
	public static final String LIST_ITEM_ID_CLMN = "LIST_ITEM_ID";
	public static final String LINE_NUM_CLMN = "LINE_NUM";
	public static final String ERROR_CLMN = "ERROR";
	
	
	@DatabaseField(canBeNull = false, foreign = true, columnName = LIST_ITEM_ID_CLMN)
	private ListItem listItem;
	@DatabaseField(canBeNull = false, foreign = true, columnName = LIST_ID_CLMN)
	private BasicList list;
	@DatabaseField(canBeNull = false, columnName = LINE_NUM_CLMN)
	private Integer lineNumber;
	@DatabaseField(canBeNull = false, columnName = ERROR_CLMN)
	private String error;
	
	public BasicList getList() {
		return list;
	}
	public void setList(BasicList list) {
		this.list = list;
	}
	public Integer getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(Integer lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public ListItem getListItem() {
		return listItem;
	}
	public void setListItem(ListItem listItem) {
		this.listItem = listItem;
	}
	
}

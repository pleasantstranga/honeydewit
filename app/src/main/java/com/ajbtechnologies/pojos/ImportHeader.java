package com.ajbtechnologies.pojos;
import com.ajbtechnologies.Constants;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "IMPORT_HEADERS")
public class ImportHeader extends BaseObject {
	
	private static final long serialVersionUID = 1L;
	public static final String NAME_CLMN = "NAME";
	public static final String IS_REQUIRED_CLM = "IS_REQUIRED";
	
	@DatabaseField(columnName = NAME_CLMN)
	private String name;
	@DatabaseField(columnName = IS_REQUIRED_CLM)
	private Integer isRequired;
	
	public ImportHeader() {
		
	}
	public ImportHeader(String name, Integer isRequired) {
		this.name = name;
		this.isRequired = isRequired;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isRequired() {
		return isRequired == Constants.TRUE;
	}
	public void setIsRequired(Integer isRequired) {
		this.isRequired = isRequired;
	}
	
}

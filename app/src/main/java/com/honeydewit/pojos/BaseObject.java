package com.honeydewit.pojos;

import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;
import java.util.Observable;

public class BaseObject extends Observable implements Serializable {

	private static final long serialVersionUID = 1L;
	public final static String DATE_CREATED_CMN = "DATE_CREATED";
	public final static String DATE_MODIFIED_CMN = "DATE_MODIFIED";
	public final static String ID_CMN = "_id";
	public final static String CREATE_MODULE_CMN = "CREATE_MODULE";
	public final static String MODIFIED_MODULE_CMN = "MODIFIED_MODULE";
	@DatabaseField(columnName = ID_CMN, generatedId = true)
	private Integer _id;
	@DatabaseField(columnName = DATE_CREATED_CMN)
	private String dateCreated;
	@DatabaseField(columnName = DATE_MODIFIED_CMN)
	private String dateModified;
	@DatabaseField(columnName = CREATE_MODULE_CMN)
	private String createModule;
	@DatabaseField(columnName = MODIFIED_MODULE_CMN)
	private String modifiedModule;
	
	
	public Integer get_id() {
		return _id;
	}
	public void set_id(Integer _id) {
		this._id = _id;
	}
	public String getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}
	public String getDateModified() {
		return dateModified;
	}
	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}
	public String getCreateModule() {
		return createModule;
	}
	public void setCreateModule(String createModule) {
		this.createModule = createModule;
	}
	public String getModifiedModule() {
		return modifiedModule;
	}
	public void setModifiedModule(String modifiedModule) {
		this.modifiedModule = modifiedModule;
	}
	
}

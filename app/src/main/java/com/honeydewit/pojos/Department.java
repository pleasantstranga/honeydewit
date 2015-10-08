package com.honeydewit.pojos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "DEPTS")
public class Department extends BaseObject {

	private static final long serialVersionUID = 1L;
	public static final String DEPT_NAME_CMN = "DEPT_NAME";
	@DatabaseField(columnName = DEPT_NAME_CMN)
	private String departmentName;
	
	public Department() {
	}
	
	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
}

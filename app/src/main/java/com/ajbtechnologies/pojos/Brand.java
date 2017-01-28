package com.ajbtechnologies.pojos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "BRANDS")
public class Brand extends BaseObject {

	private static final long serialVersionUID = 1L;
	public final String BRAND_NAME_CMN = "BRAND_NAME";
	@DatabaseField(columnName = BRAND_NAME_CMN)
	private String brandName;
	
	public Brand() {
	}
	
	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	
}

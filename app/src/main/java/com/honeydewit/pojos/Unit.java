package com.honeydewit.pojos;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "UNITS")
	

public class Unit extends BaseObject{
	private static final long serialVersionUID = 1L;
	public static final String UNIT_MEAS_CMN = "UNIT_MEAS";
	public static final String DOMN_CMN = "DOMN";
	public static final String TYP_NAM_CMN = "TYP_NAM";
	public static final String TYP_CDE_CMN = "TYP_CDE";
	public static final String CONV_TYP_CDE_CMN = "CONV_TYP_CDE";
	public static final String NUM_UNITS_CMN = "NUM_UNITS";
	
	@DatabaseField(columnName = UNIT_MEAS_CMN)
	private String unitToConvertWith;
	@DatabaseField(columnName = DOMN_CMN)
	private String domain;
	@DatabaseField(columnName = TYP_NAM_CMN)
	private String description;
	@DatabaseField(columnName = TYP_CDE_CMN)
	private String abbreviation;
	@DatabaseField(columnName = CONV_TYP_CDE_CMN)
	private String conversionType;
	@DatabaseField(columnName = NUM_UNITS_CMN)
	private float numUnitsToConvertWith;
	
	
	
	public String getUnitToConvertWith() {
		return unitToConvertWith;
	}



	public void setUnitToConvertWith(String unitToConvertWith) {
		this.unitToConvertWith = unitToConvertWith;
	}



	public String getDomain() {
		return domain;
	}



	public void setDomain(String domain) {
		this.domain = domain;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public String getAbbreviation() {
		return abbreviation;
	}



	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}



	public String getConversionType() {
		return conversionType;
	}



	public void setConversionType(String conversionType) {
		this.conversionType = conversionType;
	}



	public float getNumUnitsToConvertWith() {
		return numUnitsToConvertWith;
	}



	public void setNumUnitsToConvertWith(float numUnitsToConvertWith) {
		this.numUnitsToConvertWith = numUnitsToConvertWith;
	}



	public Unit() {
		
	}
}

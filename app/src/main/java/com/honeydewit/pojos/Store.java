package com.honeydewit.pojos;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "STORES")
public class Store extends BaseObject{
	private static final long serialVersionUID = 1L;
	@DatabaseField(columnName = "STORE_NAME")
	private String storeName;
	@DatabaseField(columnName = "STORE_NUMBER")
	private String storeNumber;
	@DatabaseField(columnName = "STORES_TYPES_ID")
	private Integer storeTypeId;
	@DatabaseField(columnName = "ADDRESS_LINE_1")
	private String addressLine1;
	@DatabaseField(columnName = "ADDRESS_LINE_2")
	private String addressLine2;
	@DatabaseField(columnName = "ADDRESS_LINE_3")
	private String addressLine3;
	@DatabaseField(columnName = "ADDRESS_LINE_4")
	private String addressLine4;
	@DatabaseField(columnName = "CITY")
	private String city;
	@DatabaseField(columnName = "POSTAL_SUBDIVISION_TYPE_ID")
	private Integer postalSubdivisionTypeId;
	@DatabaseField(columnName = "POSTAL_SUBDIVISION")
	private String postalSubdivision;
	@DatabaseField(columnName = "POSTAL_CODE")
	private String postalCode;
	@DatabaseField(columnName = "CNTRY")
	private String cntry;
	@DatabaseField(columnName = "PHONE")
	private String phone;
	
	public Store() {
		
	}
	
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}
	public Integer getStoreTypeId() {
		return storeTypeId;
	}
	public void setStoreTypeId(Integer storeTypeId) {
		this.storeTypeId = storeTypeId;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getAddressLine3() {
		return addressLine3;
	}
	public void setAddressLine3(String addressLine3) {
		this.addressLine3 = addressLine3;
	}
	public String getAddressLine4() {
		return addressLine4;
	}
	public void setAddressLine4(String addressLine4) {
		this.addressLine4 = addressLine4;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getPostalSubdivisionTypeId() {
		return postalSubdivisionTypeId;
	}
	public void setPostalSubdivisionTypeId(Integer postalSubdivisionTypeId) {
		this.postalSubdivisionTypeId = postalSubdivisionTypeId;
	}
	public String getPostalSubdivision() {
		return postalSubdivision;
	}
	public void setPostalSubdivision(String postalSubdivision) {
		this.postalSubdivision = postalSubdivision;
	}
	public String getCntry() {
		return cntry;
	}
	public void setCntry(String cntry) {
		this.cntry = cntry;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
}

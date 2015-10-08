package com.honeydewit.pojos;

import com.honeydewit.ImportError;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root
@DatabaseTable(tableName = "LISTS_ITEMS")
public class ListItem extends BaseObject {
	
	private static final long serialVersionUID = 1L;
	public static final String STORE_CMN = "STORE_ID";
	public static final String BRAND_CMN = "BRAND_ID";
	public static final String ITEM_CMN = "ITEM_ID";
	public static final String QUANTITY_CMN = "QUANTITY";
	public static final String LIST_CMN = "LISTS_ID";
	public static final String DISCOUNT_CMN = "DISCOUNT";
	public static final String PRICE_UNIT_CMN = "PRICE_UNIT";
	public static final String ROW_NUM_CMN = "ROW_NUM";
	public static final String DISCOUNT_TYP_CMN = "DISCOUNT_TYP";
	public static final String CHECKED_CLM = "CHECKED";
	public static final String IMG_NAM_CMN = "IMG_NAM";
	public static final String ITEM_NAME_CMN = "ITEM_NAME";
	public static final String UNIT_TYPE_CMN = "UNIT_TYPE";
	public static final String DESCRIPTION_CMN = "DESC";
	public static final String TABLE_NAME = "LISTS_ITEMS";
	public static final String IMPORT_ROW_CMN = "IMPORT_ROW";
	public static final String IS_CAMERA_IMAGE = "IS_CAMERA_IMAGE";
	public static final String IS_DRAWING_IMAGE = "IS_DRAWING_IMAGE";
	@Attribute
	@DatabaseField(columnName = ITEM_NAME_CMN)
	private String name ="";
	
	@Attribute(required=false)
	@DatabaseField(columnName = UNIT_TYPE_CMN)
	private String unitType="";
	
	@Attribute(required=false)
	@DatabaseField(columnName = DESCRIPTION_CMN)
	private String description="";
	
	@Attribute
	@DatabaseField(columnName = QUANTITY_CMN)
	private Double quantity = 1D;
	
	@Attribute(required=false)
	@DatabaseField(columnName = DISCOUNT_CMN)
	private Double discountCoupon = 0D;
	
	@Attribute(required=false)
	@DatabaseField(columnName = DISCOUNT_TYP_CMN)
	private String discountType;
	
	@Attribute(required=false)
	@DatabaseField(columnName = PRICE_UNIT_CMN)
	private Double pricePerUnit = 0D;
	
	@Attribute(required=false)
	@DatabaseField(columnName = ROW_NUM_CMN)
	private Integer rowNumber;
	
	@Attribute
	@DatabaseField(canBeNull = true, columnName = CHECKED_CLM)
	private Integer checked = 0;

	@Attribute
	@DatabaseField(canBeNull = true, columnName = IS_CAMERA_IMAGE)
	private Integer isCameraImage = 0;

	@Attribute
	@DatabaseField(canBeNull = true, columnName = IS_DRAWING_IMAGE)
	private Integer isDrawingImage = 0;
	
	@Attribute(required=false)
	@DatabaseField(canBeNull = true, columnName = IMG_NAM_CMN)
	private String imageName  = null;
	
	@Element(required=false)
	@DatabaseField(canBeNull = true, foreign = true, columnName = STORE_CMN)
	private Store store;
	
	@Element(required=false)
	@DatabaseField(canBeNull = true, foreign = true, columnName = BRAND_CMN)
	private Brand brand;


    @Element(required=false)
    @DatabaseField(canBeNull = true, columnName = IMPORT_ROW_CMN)
    private Integer importRow;

	//@Element(required=false)
	@DatabaseField(canBeNull = true, foreign = true, columnName = LIST_CMN)
	private BasicList list;
	
	
	@ForeignCollectionField(eager = true)
    public ForeignCollection<ImportError> errors;
	
	@ElementList(required=false)
	public ArrayList<ImportError> errorsList;
	
	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}



	public Brand getBrand() {
		return brand;
	}



	public void setBrand(Brand brand) {
		this.brand = brand;
	}


	public Double getQuantity() {
		return quantity;
	}



	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}



	public BasicList getList() {
		return list;
	}



	public void setList(BasicList list) {
		this.list = list;
	}

	
	public ListItem() {
	}



	public Double getDiscountCoupon() {
		return discountCoupon;
	}



	public void setDiscountCoupon(Double discountCoupon) {
		this.discountCoupon = discountCoupon;
	}
	public Double getPricePerUnit() {
		return pricePerUnit;
	}


	public void setPricePerUnit(Double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public Integer getRowNumber() {
		return rowNumber;
	}


	public String getDiscountType() {
		return discountType;
	}



	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}



	public Integer getChecked() {
		return checked;
	}



	public void setRowNumber(Integer rowNumber) {
		this.rowNumber = rowNumber;
	}



	public void setChecked(Integer checked) {
		this.checked = checked;
	}



	public ForeignCollection<ImportError> getErrors() {
		return errors;
	}




	public boolean hasErrors() {
		return getErrors() != null && getErrors().size() > 0;
	}
	public List<String> getErrorsAsStrings() {
		List<String> errors = new ArrayList<String>();
		for(ImportError error : getErrors()) {
			errors.add(error.getError());
		}
		return errors;
	}
	
	public void removeErrors() {
		if(hasErrors()) {
			this.errors = null;
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String itemName) {
		this.name = itemName;
		setChanged();
		notifyObservers(itemName);		
	}

    public Integer getImportRow() {
        return importRow;
    }

    public void setImportRow(Integer importRow) {
        this.importRow = importRow;
    }
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isChecked() {
		return checked == 1;
	}
	public boolean isCameraImage() {
		return isCameraImage == 1;
	}
	public boolean isDrawingImage() {
		return isDrawingImage == 1;
	}
	public void setIsCameraImage(Integer isCameraImage) {
		this.isCameraImage = isCameraImage;
	}
	public void setIsDrawingImage(Integer isDrawingImage) {
		this.isDrawingImage = isDrawingImage;
	}
}

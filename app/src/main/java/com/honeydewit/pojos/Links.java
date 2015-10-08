package com.honeydewit.pojos;

import com.honeydewit.Constants;
import com.j256.ormlite.field.DatabaseField;

public class Links extends BaseObject {
	private static final long serialVersionUID = 1L;
	public static final String LINKS_TYPE_CDE_CLMN = "LINKS_TYP_CDE";
	public static final String INTENT_CLMN = "INTENT";
	public static final String LINK_TXT_CLMN = "LINK_TXT";
	public static final String SUB_CAT_CLMN = "LINK_SUB_CAT_CDE";
	public static final String IMAGE_NAME_CLMN = "IMAGE_NAME";
	public static final String IS_FINISH_ON_NEW_INTENT = "IS_FINISH_ON_NEW_INTENT";
	
	@DatabaseField(columnName = LINKS_TYPE_CDE_CLMN)
	private Integer linksTypeCode;
	@DatabaseField(columnName = SUB_CAT_CLMN)
	private Integer subCategoryCode;
	@DatabaseField(columnName = INTENT_CLMN)
	private String intent;
	@DatabaseField(columnName = LINK_TXT_CLMN)
	private String linkTxt;
	@DatabaseField(columnName = IMAGE_NAME_CLMN)
	private String imageName;
	@DatabaseField(columnName = IS_FINISH_ON_NEW_INTENT)
	private Integer isFinishOnNewIntent;

	public Integer getLinksTypeCode() {
		return linksTypeCode;
	}
	public void setLinksTypeCode(Integer linksTypeCode) {
		this.linksTypeCode = linksTypeCode;
	}
	public String getIntent() {
		return intent;
	}
	public void setIntent(String intent) {
		this.intent = intent;
	}
	public String getLinkTxt() {
		return linkTxt;
	}
	public void setLinkTxt(String linkTxt) {
		this.linkTxt = linkTxt;
	}
	public Integer getSubCategoryCode() {
		return subCategoryCode;
	}
	public void setSubCategoryCode(Integer subCategoryCode) {
		this.subCategoryCode = subCategoryCode;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public void setIsFinishOnNewIntent(boolean isFinishOnNewIntent) {
		if(isFinishOnNewIntent) {
			this.isFinishOnNewIntent = Constants.TRUE;
		}
		else {
			this.isFinishOnNewIntent = Constants.FALSE;

		}

	}
	public boolean isFinishOnNewInent() {
		return this.isFinishOnNewIntent == Constants.TRUE;
	}
}

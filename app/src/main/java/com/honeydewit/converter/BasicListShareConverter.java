package com.honeydewit.converter;

import android.content.Context;

import com.honeydewit.HoneyDewApplication;
import com.honeydewit.pojos.BasicList;
import com.honeydewit.pojos.BasicListShare;

public class BasicListShareConverter {
	
	
	public static BasicListShare convertToBasicListShare(BasicList list) {
		BasicListShare share = new BasicListShare();

		share.setListName(list.getListName());
		share.setListTypeId(list.getListTypeId());
		share.setEnabled(list.getEnabled());
		share.setDateFilled(list.getDateFilled());
		share.setStore_id(list.getStore_id());
		share.setDescription(list.getDescription());
		share.setChecked(list.getChecked());
		share.setIsShowErrorInd(list.getIsShowErrorInd());
		share.setIsShowErrorDialogInd(list.getIsShowErrorDialogInd());
		share.setItems(list.getItems());
		share.setEvents(list.getEvents());
		share.setErrors(list.getErrors());
		
		return share;
	}
	public static BasicList convertFromBasicListShare(Context context, BasicListShare share) {
		BasicList list = new BasicList();

		list.setListName(share.getListName());
		list.setListTypeId(share.getListTypeId());
		list.setEnabled(share.getEnabled());
		list.setDateFilled(share.getDateFilled());
		list.setStore_id(share.getStore_id());
		list.setDescription(share.getDescription());
		list.setChecked(share.getChecked());
		list.setIsShowErrorInd(share.getIsShowErrorInd());
		list.setIsShowErrorDialogInd(share.getIsShowErrorDialogInd());
		
		((HoneyDewApplication)context.getApplicationContext()).getShoppingListDbHelper().addUpdateShoppingList(list);
		((HoneyDewApplication)context.getApplicationContext()).getShoppingListDbHelper().addAllListItems(list, share.getItems());
		return list;
	}
	
}

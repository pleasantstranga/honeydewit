package com.ajbtechnologies.converter;

import android.os.Parcel;
import android.os.Parcelable;

import com.ajbtechnologies.pojos.ListItem;

import java.util.ArrayList;
import java.util.List;

public class InvalidListItem implements Parcelable {
     private ListItem listItem;
     private List<String> errorMessages = new ArrayList<String>();

     public InvalidListItem (ListItem listItem, List<String> errorMessages) {
    	 this.listItem = listItem;
    	 this.errorMessages = errorMessages;
     }
     public int describeContents() {
         return 0;
     }

     public void writeToParcel(Parcel out, int flags) {
         out.writeSerializable(listItem);
         out.writeStringList(errorMessages);
     }

     public static final Parcelable.Creator<InvalidListItem> CREATOR  = new Parcelable.Creator<InvalidListItem>() {
         public InvalidListItem createFromParcel(Parcel in) {
             return new InvalidListItem(in);
         }

         public InvalidListItem[] newArray(int size) {
             return new InvalidListItem[size];
         }
     };
     
     private InvalidListItem(Parcel in) {
         listItem = (ListItem)in.readSerializable();
         in.readStringList(errorMessages);
     }
	
	public ListItem getListItem() {
		return listItem;
	}
	public void setListItem(ListItem listItem) {
		this.listItem = listItem;
	}
	public List<String> getErrorMessages() {
		return errorMessages;
	}
	public void setErrorMessages(List<String> errorMessages) {
		this.errorMessages = errorMessages;
	}
 }
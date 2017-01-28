package com.ajbtechnologies;

import android.widget.TextView;

import com.ajbtechnologies.pojos.ListItem;

import java.util.List;

public class ListItemWatcher  {
	private List<ItemsPrice> items = null;
	private TextView totalItemsCheckedView;
	private TextView totalItemsPriceView;
	private double totalPrice = 0d;
	private double itemsChecked = 0d;
	
	public ListItemWatcher(List<ItemsPrice> items, TextView totalItemsCheckedView, TextView totalItemsPriceView) {
		this.items = items;
		this.totalItemsCheckedView = totalItemsCheckedView;
		this.totalItemsPriceView = totalItemsPriceView;
		for(ItemsPrice item : this.items) {
			totalPrice += item.getTotalItemsPrice();
			itemsChecked += 1;
		}
		notifyDataChanged();
	}
	public void add(ItemsPrice item) {
		items.add(item);		
		notifyDataChanged();
	}
	public void removeItem(ListItem item) {
		ItemsPrice itemPrice = getItemById(item.get_id());
		items.remove(itemPrice);
		notifyDataChanged() ;
	}
	public void setChecked(ListItem item) {
		if(item.isChecked()) {
			add(new ItemsPrice(item.get_id(), item.getQuantity(), item.getPricePerUnit()));
		}
		else {
			removeItem(item);
		}
		notifyDataChanged();
	}
	private ItemsPrice getItemById(int id) {
		for(ItemsPrice item : items) {
			if(item.getListItemId() == id) {
				return item;
			}
		}
		return null;
	}
	private void notifyDataChanged() {
		itemsChecked = 0;
		totalPrice = 0;
		
		for(ItemsPrice item : this.items) {
			itemsChecked +=1;
			totalPrice += item.getTotalItemsPrice();
		}
		totalItemsCheckedView.setText(String.valueOf(itemsChecked));
		totalItemsPriceView.setText(String.valueOf(totalPrice));
	}
} 

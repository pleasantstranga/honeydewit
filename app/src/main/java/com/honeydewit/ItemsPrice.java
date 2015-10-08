package com.honeydewit;

public class ItemsPrice {
	private double price;
	private double quantity;
	private double totalPrice;
	private int listItemId;
	
	public int getListItemId() {
		return listItemId;
	}
	public void setListItemId(int listItemId) {
		this.listItemId = listItemId;
	}
	public ItemsPrice(int id, double quantity, double price) {
		this.listItemId = id;
		this.quantity = quantity;
		this.price = price;
		this.totalPrice = price * quantity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getQuantity() {
		return quantity;
	}
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}
	public double getTotalItemsPrice() {
		return totalPrice;
	}
}

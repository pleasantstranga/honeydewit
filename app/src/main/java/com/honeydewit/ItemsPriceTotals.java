package com.honeydewit;

public class ItemsPriceTotals {
	
	private double quantity;
	private double totalPrice;
	
	
	public ItemsPriceTotals(double quantity, double price) {
		this.quantity = quantity;
		this.totalPrice = price;
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

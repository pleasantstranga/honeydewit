package com.ajbtechnologies;

import android.graphics.Bitmap;

public class CustomBitmap {
	Bitmap bitmap;
	String bitmapFileName;
	
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getBitmapFileName() {
		return bitmapFileName;
	}
	public void setBitmapFileName(String bitmapFileName) {
		this.bitmapFileName = bitmapFileName;
	}
}

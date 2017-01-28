package com.ajbtechnologies.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.util.Log;

import com.ajbtechnologies.utils.ScalingUtilities.ScalingLogic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtil {
	
	
	public static Bitmap getThumbnailBitmap(String fileName) {
		final int THUMBNAIL_HEIGHT = 86;
		final int THUMBNAIL_WIDTH = 62;
		Bitmap rotatedBitmap = null;
		try {
			File f = new File(fileName);
			if(f.exists()) {
				f = null;
				Bitmap bMap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(fileName), THUMBNAIL_WIDTH, THUMBNAIL_HEIGHT);
				Matrix matrix = new Matrix();
				matrix.postRotate(getRotation(fileName)); 

				// create a new bitmap from the original using the matrix to transform the result
				rotatedBitmap = Bitmap.createBitmap(bMap , 0, 0, bMap .getWidth(), bMap .getHeight(), matrix, true);
				bMap.recycle();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return rotatedBitmap;
	}
	public static Bitmap getScaledBitmap(String imageSource, float maxWidth) throws Exception {
	
		Bitmap unscaledBitmap = BitmapFactory.decodeFile(imageSource);

		Log.d("ImageUtil UnScaled", imageSource + " size in Megabytes: " +  Double.valueOf(unscaledBitmap.getAllocationByteCount())/1000000d);

		float bitmapWidth = (float)unscaledBitmap.getWidth();
		float percent = getPercent(bitmapWidth, maxWidth);

		// create a new bitmap from the original using the matrix to transform the result
		Bitmap resizedBitmap = Bitmap.createBitmap(unscaledBitmap , 0, 0, unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), createMatrix(imageSource, unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), percent), true);
		unscaledBitmap.recycle();    

		Log.d("ImageUtil Scaled", imageSource + " size in Megabytes: " +  Double.valueOf(resizedBitmap.getAllocationByteCount())/1000000d);
		resizedBitmap = compressBitmap(resizedBitmap,imageSource);

       return resizedBitmap;
   }
   public static int getRotation(String filePath) {
   	int rotation = 0;
   	try {
			ExifInterface exif = new ExifInterface(filePath);
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
			if(orientation == 1) {
				rotation = 0;
			}
			else if(orientation == 3) {
				rotation = 180;
			}
			else if (orientation == 6) {
				rotation = 90;
			}
			else if(orientation == 8) {
				rotation = -90;
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   	return rotation;
   }
   
   public static File createTempImageFile(String directoryName, String tempImageName, String tempImageNameExtension) throws IOException {
       return File.createTempFile(tempImageName,tempImageNameExtension, new File(directoryName));
   }
   private static Matrix createMatrix(String imageSource, int bmWidth, int bmHeight, float percent) {
	   float newWidth = Float.valueOf(bmWidth)*(percent);
	   float newHeight = Float.valueOf(bmHeight)*(percent);
	   float scaleWidth = newWidth / bmWidth;
	   float scaleHeight = newHeight / bmHeight;
	   Matrix matrix = new Matrix();
       matrix.postRotate(getRotation(imageSource));
	   matrix.postScale(scaleWidth, scaleHeight);
	   return matrix;
	   
   }
   public static Bitmap getBitmap(String path,int mDstWidth, int mDstHeight) {
       
	   return ScalingUtilities.decodeFile(path,
               mDstWidth, mDstHeight, ScalingLogic.FIT);
   }
   private static float getPercent(float bitmapWidth, float maxWidth) {
	   float percent = 0;
	  
	   
	   percent = maxWidth/bitmapWidth;
	     
	   return percent;
	   
   }
   
   private static Bitmap compressBitmap(Bitmap originalBitmap, String filePath) {

	   File imageFile = new File(filePath);
       Log.d("ImageUtil Before Compressed", filePath + " size in Megabytes: " +  Double.valueOf(originalBitmap.getAllocationByteCount())/1000000d);

	   if (imageFile.exists()) {
	       imageFile.delete();
	     }

	       try {
	           FileOutputStream out = new FileOutputStream(filePath);
	           originalBitmap.compress(Bitmap.CompressFormat.JPEG, 30, out);
	           out.flush();
	           out.close();

	       } catch (Exception e) {
	    	   originalBitmap.recycle();
	           e.printStackTrace();
	       }
	       Bitmap newBitmap = BitmapFactory.decodeFile(filePath);
	       return newBitmap;

	   }
}

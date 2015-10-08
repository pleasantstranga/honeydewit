package com.honeydewit.adapters;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageButton;

import com.honeydewit.utils.ImageUtil;

import java.lang.ref.WeakReference;

public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<View> imageViewReference;
        private Boolean isScale = true;
        
        public BitmapWorkerTask(boolean isScale, View imageView) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            this.imageViewReference = new WeakReference<View>(imageView);
            this.isScale = isScale;
        }

        // Decode image in background.
        @Override
        public Bitmap doInBackground(String... params){
        	 Bitmap bm = null;
        	 try {
        		 if(isScale) {	 
        			 bm = ImageUtil.getScaledBitmap(params[0],Float.valueOf(params[1]));
        		 }
        		 else {

        		     bm = ImageUtil.getBitmap(params[0],Integer.valueOf(params[2]),Integer.valueOf(params[3]));
        		 }
        	 }
        	 catch(Exception e) {
        		 try {
					throw new Exception();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        		 
        	 }
        	 return bm;
			 
        }

        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
        	try {
        		 if (bitmap != null) {
                     final View imageView = imageViewReference.get();
                     if (imageView != null) {
						 ((ImageButton)imageView).setImageBitmap(bitmap);
                     }
                 }
        	}
        	catch(Exception e) {
        		e.printStackTrace();
        	}
           
        }
    }
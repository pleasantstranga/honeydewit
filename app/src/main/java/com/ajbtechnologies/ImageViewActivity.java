package com.ajbtechnologies;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

public class ImageViewActivity extends BasicActivity {
	public String fileName;
	public ImageView image;
	public Button closeButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_image);
		image = (ImageView)findViewById(R.id.imageView);
		closeButton = (Button)findViewById(R.id.closeButton);
		fileName = getIntent().getStringExtra("photoPath");


		File imgFile = new  File(fileName);

		if(imgFile.exists()){

			Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

			image.setImageBitmap(myBitmap);

		}

		closeButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
	}
	
	
}

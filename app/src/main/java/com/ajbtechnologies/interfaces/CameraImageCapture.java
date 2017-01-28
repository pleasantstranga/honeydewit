package com.ajbtechnologies.interfaces;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.ajbtechnologies.utils.ImageUtil;

import java.io.File;
import java.io.IOException;

/**
 * Created by aaronbernstein on 1/14/16.
 */
public class CameraImageCapture implements ImageCapture {

    private Context context;

    public CameraImageCapture(Context context) {
        this.context = context;
    }
    @Override
    public void capture(int requestCode, String path, String fileName, String fileNameExtension) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = ImageUtil.createTempImageFile(path, fileName, fileNameExtension);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    ex.printStackTrace();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                    takePictureIntent.putExtra("imageName", photoFile.getName());
                    ((Activity)context).getIntent().putExtra("imageName", photoFile.getName());
                    ((Activity)context).startActivityForResult(takePictureIntent, requestCode);
                }
            }

        }

}

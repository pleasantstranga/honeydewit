package com.ajbtechnologies.converter;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.fileutil.FileUtil;
import com.fileutil.ZipUtil;
import com.ajbtechnologies.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImportUtils {
	
	public static String getAttachmentFileName(Context context, Uri uri, InputStream inputStream) {
		String attachmentFileName = null;
		
		if (uri != null) {
			Cursor c = context.getContentResolver().query(uri, null, null, null, null);
			if(c != null) {
				c.moveToFirst();
				final int fileNameColumnId = c.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME);
				if (fileNameColumnId >= 0) attachmentFileName = c.getString(fileNameColumnId);
				c.close();
			}
			else {
				attachmentFileName = uri.getLastPathSegment();
			}
		}

		return attachmentFileName;
	}
	public static InputStream getFileInputStream(Uri uri, Context context)  {
		InputStream fileInput = null;
		try {
			if(ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
				fileInput = context.getContentResolver().openInputStream(uri);
			}
			else {
				fileInput = new FileInputStream(uri.getPath());
			}

		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		return fileInput;
	}
    public static File generateImportTempFiles(final InputStream inputStream, final String tempDirectory, final String fileName) throws Exception {


        try {

            if (fileName.endsWith(Constants.XLSX_EXT) || fileName.endsWith(Constants.HDEW_EXT)) {
                return ZipUtil.unzipFromStream(inputStream, tempDirectory, fileName);
            } else {
                return FileUtil.generateFileFromInputStream(inputStream, tempDirectory, fileName);
            }
        }
        finally {
            inputStream.close();
        }

    }
	
}

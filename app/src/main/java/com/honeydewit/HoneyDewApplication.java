package com.honeydewit;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.honeydewit.calendar.CalendarDbHelper;
import com.honeydewit.dataaccess.DbHelperImpl;
import com.honeydewit.pojos.BasicList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HoneyDewApplication extends Application {
	
	private BasicList currentList;

	private static Typeface typeface;
	private static DbHelperImpl shoppingListDbHelper;
	private static CalendarDbHelper calDbHelper;
    private File storageDirectory = Environment.getExternalStorageDirectory();
    private List<Integer> listsIdsToDelete = new ArrayList<Integer>();

	public Set<Integer> getCurrentNotifications() {
		return currentNotifications;
	}

	public void setCurrentNotifications(Set<Integer> currentNotifications) {
		this.currentNotifications = currentNotifications;
	}

	private Set<Integer> currentNotifications = new HashSet<>();

    public List<Integer> getListsIdsToDelete() {
        return listsIdsToDelete;
    }

    public void setListsIdsToDelete(List<Integer> listsIdsToDelete) {
        this.listsIdsToDelete = listsIdsToDelete;
    }

    public File getStorageDirectory() {
        return storageDirectory;
    }
	public Typeface getTypeface() {
		return typeface;
	}

	public void setTypeface(Typeface typeface) {
		HoneyDewApplication.typeface = typeface;
	}

	public BasicList getCurrentList() {
		return currentList;
	}
 
	public void setCurrentList(BasicList currentList) {
		this.currentList = currentList;

	}
	public DbHelperImpl getShoppingListDbHelper() {
		if(null == HoneyDewApplication.shoppingListDbHelper) {
			HoneyDewApplication.shoppingListDbHelper = new DbHelperImpl(this);
		}
		return HoneyDewApplication.shoppingListDbHelper;
	}
	public void setShoppingListDbHelper(DbHelperImpl ShoppingListDbHelper) {
		HoneyDewApplication.shoppingListDbHelper = ShoppingListDbHelper;
		 
	}
	public void setCalDbHelper(CalendarDbHelper calDbHelper) {
		HoneyDewApplication.calDbHelper = calDbHelper;
	}
	public CalendarDbHelper getCalDbHelper() {
		if(null == calDbHelper) {
			setCalDbHelper(new CalendarDbHelper());
		}
		return calDbHelper;
	}
	
    public void saveDialogVisibilityPreferences(String key, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.apply();
	}
    public void saveSplashScreenPreferences(String key, boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.apply();
	}
    public boolean isShowDialog(String dialogKey) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		return sharedPreferences.getBoolean(dialogKey, true);

	}
    public boolean isShowSplashScreen(String splahScreenKey) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		return sharedPreferences.getBoolean(splahScreenKey, true);

	}
    public void loadBannerAd(AdView adView) {
    	final TelephonyManager tm =(TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        String deviceid = tm.getDeviceId();
		AdRequest adRequest = new AdRequest.Builder()
		.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		.addTestDevice(deviceid)
		.build();


		//load small ad
		
		adView.loadAd(adRequest);

    }

	public String getImagesDirectory() {
		String imagesDirectory = storageDirectory.toString() + "/HoneyDewIt/images/";


		File myDir = new File(imagesDirectory); 
		if(!myDir.exists()) {
			myDir.mkdirs();
		}
		return imagesDirectory;

	}
	public String getZipDirectory() {
		String imagesDirectory = storageDirectory.toString() + "/HoneyDewIt/temp/zipFiles/";


		File myDir = new File(imagesDirectory); 
		if(!myDir.exists()) {
			myDir.mkdirs();
		}
		return imagesDirectory;

	}
	 /**
     * Returns the import temp directory as a file object and creates the directory if needed
     * @return
     */
    public File getImportDirectory() {
		File folder = new File(storageDirectory.toString() + "/HoneyDewIt/temp/imports/");
		if(!folder.exists()){
			folder.mkdirs();
		}
		return folder;
    }
    public void sendXml(String[] emailAddresses, Uri uri) {
    	if (uri != null) {
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,emailAddresses);
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "HoneyDewIt List Share");
			emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			emailIntent.setType("plain/text");
			emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Someone wants to share a list with you. Please click on the attachment and open it with the HoneyDew application. If you do not have the HoneyDewIt application please download it from the Google Play store.");
			emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
			startActivity(emailIntent);
		}
    	else {
    		Toast.makeText(this, "There was an error sharing the list. Please try again", Toast.LENGTH_LONG).show();
    	}
		
	}
    public void sendError(String[] emailAddresses, String error) {
        if (error != null) {
            Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,emailAddresses);
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "HoneyDewIt Error Report");
            emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,error);
            startActivity(emailIntent);
        }
        else {
            Toast.makeText(this, "There was an error sending the report. Please try again", Toast.LENGTH_LONG).show();
        }

    }
    public File getExportTempDirectory() {
		File folder = new File(storageDirectory.toString() + "/HoneyDewIt/temp/export/");
		if(!folder.exists()){
			folder.mkdirs();
		}
		return folder;
    }
    
}

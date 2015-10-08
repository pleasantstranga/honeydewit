package com.honeydewit;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import com.honeydewit.calendar.CalendarDbHelper;
import com.honeydewit.dataaccess.DbHelperImpl;
import com.honeydewit.pojos.BasicList;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProgramInitializer extends OrmLiteSqliteOpenHelper {
	private SQLiteDatabase myDataBase;
	static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "conversions";
	
	public ProgramInitializer(Context context,
			CursorFactory factory) {
		super(context, DATABASE_NAME, factory, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	public static void initialize(HoneyDewApplication context) {


		try {
			initializeConversionDb(context);
			initDbHelpers(context);
			initLogDirectory(context);
			initImagesDirectory(context);
			initImportsTempDirectory(context);
			initExportTempDirectory(context);
			initNotesList(context);
			context.setTypeface(Typeface.createFromAsset(context.getAssets(),"fonts/RobotoCondensed-Regular.ttf"));

			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
			if(!sharedPreferences.contains(Constants.SHOW_HOME_SPLASH_SCREEN)) {
				context.saveSplashScreenPreferences(Constants.SHOW_HOME_SPLASH_SCREEN,true);
			}

		}
		catch(Exception e) {
			e.printStackTrace();
		}

		
	}
	private static void initNotesList(HoneyDewApplication context) throws Exception {
		BasicList basicList = context.getShoppingListDbHelper().getListByName("Notes", Constants.NOTES_LIST_TYP_CODE, true);
		if(basicList == null) {
			basicList = new BasicList();
			basicList.setListName("Notes");
			basicList.setListTypeId(Constants.NOTES_LIST_TYP_CODE);
			basicList.setEnabled(Constants.TRUE);
			context.getShoppingListDbHelper().addUpdateShoppingList(basicList);
		}
	}
    private static void initializeConversionDb(HoneyDewApplication context) throws Exception {
    	 

        	ProgramInitializer init = new ProgramInitializer(context, null);
        	init.createDataBase(context);
        	init.close();

    }
    private static void initDbHelpers(HoneyDewApplication context) throws Exception {
    	if(null == context.getCalDbHelper()) {
    		context.setCalDbHelper(new CalendarDbHelper());
    	}
    	if(null == context.getShoppingListDbHelper()) {
    		context.setShoppingListDbHelper(new DbHelperImpl(context));
    	} 
    }
    private static void initLogDirectory(HoneyDewApplication context) throws Exception{
    	 File myDir = new File(context.getStorageDirectory().toString() + "/HoneyDewIt");
    	 if(!myDir.exists()) {
    		 myDir.mkdirs();
    	 }
    	
    }
    private static void initImagesDirectory(HoneyDewApplication context) throws Exception{
   	 File myDir = new File(context.getStorageDirectory().toString() + "/HoneyDewIt/images");
   	 if(!myDir.exists()) {
   		 myDir.mkdirs();
   	 }
   	
   }
    private static void initImportsTempDirectory(HoneyDewApplication context) throws Exception {
      	 File myDir = new File(context.getStorageDirectory().toString() +  "/HoneyDewIt/temp/imports/");
      	 if(!myDir.exists()) {
      		 myDir.mkdirs();
      	 }
      	
      }
    private static void initExportTempDirectory(HoneyDewApplication context) throws Exception {
     	 
     	 File myDir = context.getExportTempDirectory();
     	 if(!myDir.exists()) {
     		 myDir.mkdirs();
     	 }
     	
     }
    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    private void createDataBase(Context context) throws IOException{
 
    	boolean dbExist = checkDataBase(context);
 
    	if(!dbExist){

    			getReadableDatabase();
        		copyDataBase(context);

        }
 
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(Context myContext){
    	File dbFile=myContext.getDatabasePath(DATABASE_NAME);
    	return dbFile.exists();
    	
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase(Context myContext) throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(DATABASE_NAME);
    	
    	// Path to the just created empty db
    	File outFileName = myContext.getDatabasePath(DATABASE_NAME);
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
    public void openDataBase(Context myContext) throws SQLException{
 
    	//Open the database
        String myPath = myContext.getDatabasePath(DATABASE_NAME).getPath();
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    	if (!myDataBase.isReadOnly()) {
            // Enable foreign key constraints
    		myDataBase.execSQL("PRAGMA foreign_keys=ON;");
        }
 
    }
 
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
 
	}
    public synchronized void open(Context myContext) throws Exception {
    	if(myDataBase != null) {
    		if(!myDataBase.isOpen()) {
        		String myPath = myContext.getDatabasePath(DATABASE_NAME).getPath();
            	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        	}
    	}
    	else {
    		String myPath = myContext.getDatabasePath(DATABASE_NAME).getPath();
    		myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    	}
	}
	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}
 
}

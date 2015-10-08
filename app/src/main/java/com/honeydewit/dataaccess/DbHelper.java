package com.honeydewit.dataaccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.honeydewit.ImportError;
import com.honeydewit.pojos.BaseObject;
import com.honeydewit.pojos.Brand;
import com.honeydewit.pojos.Department;
import com.honeydewit.pojos.ImportHeader;
import com.honeydewit.pojos.Links;
import com.honeydewit.pojos.ListEvent;
import com.honeydewit.pojos.ListItem;
import com.honeydewit.pojos.Store;
import com.honeydewit.pojos.Unit;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class DbHelper extends OrmLiteSqliteOpenHelper{

	static final int DATABASE_VERSION = 2;
    static final String DATABASE_NAME = "conversions";
    Context myContext;
    
    /**** DAOS *****/
    private ListDaoImpl listDao;
    private LightListDaoImpl lightListDao;
    private Dao<Brand, Integer> brandDao;
    private Dao<Department, Integer> departmentDao;
    private ListItemDaoImpl listItemDao;
    private Dao<Store, Integer> storeDao;
    private Dao<Unit, Integer> unitsDao;
    private Dao<ListEvent, Integer> listEventsDao;
    private Dao<Links, Integer> linksDao;
    private Dao<ImportHeader, Integer> importHeadersDao;
    private Dao<ImportError,Integer> importErrorsDao;
    
    private DbHelper dbHelper;
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DbHelper(Context context) {
 
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.myContext = context;
    }
    
    public DbHelper getHelper() {
    	
        if (dbHelper == null) {
            dbHelper = OpenHelperManager.getHelper(myContext, DbHelper.class);
        }
        return dbHelper;
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
	public ListDaoImpl getListDao() throws java.sql.SQLException {
		if (listDao == null) {
			listDao = new ListDaoImpl(connectionSource) ;
		}
		return listDao;
	}
	public LightListDaoImpl getLightListDao() throws java.sql.SQLException {
		if (lightListDao == null) {
			lightListDao = new LightListDaoImpl(connectionSource) ;
		}
		return lightListDao;
	}
	public Dao<Brand, Integer> getBrandDao() throws java.sql.SQLException {
		if (brandDao == null) {
			brandDao = getDao(Brand.class);
		}
		return brandDao;
	}
	public Dao<Department, Integer> getDepartmentDao() throws java.sql.SQLException {
		if (departmentDao == null) {
			departmentDao = getDao(Department.class);
		}
		return departmentDao;
	}
	
	public Dao<ListItem, Integer> getListItemDao() throws java.sql.SQLException {
		if (listItemDao == null) {
			listItemDao = new ListItemDaoImpl(connectionSource) ;
		}
		return listItemDao;
	}
	public Dao<Store, Integer> getStoreDao() throws java.sql.SQLException {
		if (storeDao == null) {
			storeDao = getDao(Store.class);
		}
		return storeDao;
	}
	public Dao<Unit, Integer> getUnitsDao() throws java.sql.SQLException {
		if (unitsDao == null) {
			unitsDao = getDao(Unit.class);
		}
		return unitsDao;
	}
	public Dao<ListEvent, Integer> getListEventsDao() throws java.sql.SQLException {
		if(listEventsDao == null) {
			listEventsDao = getDao(ListEvent.class);
		}
		return listEventsDao;
	}
	public Dao<Links, Integer> getLinksDao() throws java.sql.SQLException {
		if(linksDao == null) {
			linksDao = getDao(Links.class);
		}
		return linksDao;
	}
	public Dao<ImportHeader, Integer> getImportHeadersDao() throws java.sql.SQLException {
		if(importHeadersDao == null) {
			importHeadersDao = getDao(ImportHeader.class);
		}
		return importHeadersDao;
	}
	public Dao<ImportError, Integer> getImportErrorsDao() throws java.sql.SQLException {
		if(importErrorsDao == null) {
			importErrorsDao = getDao(ImportError.class);
		}
		return importErrorsDao;
	}
	 public void addAuditData(BaseObject object, boolean isUpdate) {
	    	
	    	if(!isUpdate) {
	    		object.setCreateModule("A");
	        	object.setModifiedModule("A");
	        	object.setDateModified(createDateTimestamp());
	    		object.setDateCreated(createDateTimestamp());
	    	}
	    	object.setDateModified(createDateTimestamp());   	
	    }
	 private String createDateTimestamp() {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US);
			Date date = new Date();
			return dateFormat.format(date);
		}
}

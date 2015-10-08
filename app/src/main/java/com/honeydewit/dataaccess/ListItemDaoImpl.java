package com.honeydewit.dataaccess;

import com.honeydewit.ImportError;
import com.honeydewit.pojos.ListItem;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class ListItemDaoImpl extends BaseDaoImpl<ListItem, Integer>  {
	private final Dao<ImportError, Integer> importErrorDao;
	
	
    public ListItemDaoImpl(ConnectionSource connectionSource,Class<ListItem> tableConfig) throws SQLException {
            super(connectionSource, tableConfig);
    		importErrorDao = DaoManager.createDao(connectionSource, ImportError.class);            
    } 
	protected ListItemDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, ListItem.class);
		importErrorDao = DaoManager.createDao(connectionSource, ImportError.class);
		// TODO Auto-generated constructor stub
	}
	@Override
    public int delete(ListItem listItem) throws SQLException {
    	importErrorDao.delete(listItem.getErrors());
       return super.delete(listItem);
    }
	@Override
	public int update(ListItem listItem) throws SQLException {
		
	    listItem.notifyObservers(listItem);
		return super.update(listItem);
	}
	
	
}

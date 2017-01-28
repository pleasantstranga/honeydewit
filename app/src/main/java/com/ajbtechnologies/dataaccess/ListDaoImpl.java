package com.ajbtechnologies.dataaccess;

import com.ajbtechnologies.ImportError;
import com.ajbtechnologies.pojos.BasicList;
import com.ajbtechnologies.pojos.ListItem;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class ListDaoImpl extends BaseDaoImpl<BasicList, Integer>  {
	private final Dao<ListItem, Integer> listItemDao;
	private final Dao<ImportError, Integer> importErrorDao;
	
	public ListDaoImpl(ConnectionSource connectionSource,Class<BasicList> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
		listItemDao = DaoManager.createDao(connectionSource, ListItem.class);
		importErrorDao = DaoManager.createDao(connectionSource, ImportError.class);
	} 

	protected ListDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, BasicList.class);
		listItemDao = DaoManager.createDao(connectionSource, ListItem.class);
		importErrorDao = DaoManager.createDao(connectionSource, ImportError.class);
	}
	@Override
    public int delete(BasicList list) throws SQLException {
		List<ListItem> items = list.getItems(true);
		List<ImportError> errors = list.getErrorsList();

		super.delete(list);
		
		for(ListItem listItem : items) {
			listItemDao.delete(listItem);
		}
		for(ImportError error : errors) {
			importErrorDao.delete(error);
		}
       return -1;
    }

}

package com.ajbtechnologies.dataaccess;

import com.ajbtechnologies.pojos.BasicListLight;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class LightListDaoImpl extends BaseDaoImpl<BasicListLight, Integer>  {
	
	public LightListDaoImpl(ConnectionSource connectionSource,Class<BasicListLight> tableConfig) throws SQLException {
        super(connectionSource, tableConfig);
        
	} 

	protected LightListDaoImpl(ConnectionSource connectionSource) throws SQLException {
		super(connectionSource, BasicListLight.class);
		
	}
	
}

package com.ajbtechnologies.dataaccess;

import com.ajbtechnologies.pojos.BasicList;
import com.j256.ormlite.dao.Dao;


public interface ListDao extends Dao<BasicList, Integer> {
    // we will just be overriding some of the delete methods
	
}
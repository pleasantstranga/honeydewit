package com.honeydewit.dataaccess;

import com.honeydewit.pojos.BasicList;
import com.j256.ormlite.dao.Dao;


public interface ListDao extends Dao<BasicList, Integer> {
    // we will just be overriding some of the delete methods
	
}
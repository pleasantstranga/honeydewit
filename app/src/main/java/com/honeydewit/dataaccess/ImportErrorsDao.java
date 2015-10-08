package com.honeydewit.dataaccess;

import com.honeydewit.ImportError;
import com.j256.ormlite.dao.Dao;


public interface ImportErrorsDao extends Dao<ImportError, Integer> {
    // we will just be overriding some of the delete methods
	
}
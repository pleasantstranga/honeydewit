package com.honeydewit.dataaccess;


import com.honeydewit.pojos.ImportHeader;
import com.j256.ormlite.dao.Dao;


public interface ImportHeadersDao extends Dao<ImportHeader, Integer> {
    // we will just be overriding some of the delete methods
}
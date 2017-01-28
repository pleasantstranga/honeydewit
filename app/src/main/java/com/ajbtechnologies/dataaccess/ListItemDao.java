package com.ajbtechnologies.dataaccess;


import com.ajbtechnologies.pojos.ListItem;
import com.j256.ormlite.dao.Dao;


public interface ListItemDao extends Dao<ListItem, Integer> {
    // we will just be overriding some of the delete methods
}
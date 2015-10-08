package com.honeydewit.services;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;

import com.honeydewit.dataaccess.DbHelperImpl;
import com.honeydewit.pojos.BasicList;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.List;

/**
 * Created by aaronbernstein on 3/27/15.
 */
@SuppressLint("Registered")
public class BasicListImportService extends IntentService {
    List<BasicList> lists;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *

     * @param name Used to name the worker thread, important only for debugging.
     */
    public BasicListImportService() {
        super("BasicListImportService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *

     * @param name Used to name the worker thread, important only for debugging.
     */
    public BasicListImportService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        DbHelperImpl dbHelperImpl = OpenHelperManager.getHelper(this, DbHelperImpl.class);

        dbHelperImpl.close();
    }

    public List<BasicList> getLists() {
        return lists;
    }

    public void setLists(List<BasicList> lists) {
        this.lists = lists;
    }
}

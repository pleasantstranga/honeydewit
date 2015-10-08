package com.honeydewit.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.honeydewit.HoneyDewApplication;
import com.honeydewit.dataaccess.DbHelperImpl;
import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by aaronbernstein on 3/27/15.
 */
public class DeleteListService extends IntentService {
    Integer id;

    public DeleteListService() {
        super("DeleteListService");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *

     * @param name Used to name the worker thread, important only for debugging.
     */
    public DeleteListService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Integer listId = intent.getIntExtra("listId",-1);
        DbHelperImpl dbHelperImpl = OpenHelperManager.getHelper(this, DbHelperImpl.class);
        dbHelperImpl.toggleEnabledList(listId,false);
        Log.d(DeleteListService.class.getName(), "Disabled list with id: " + listId);
        dbHelperImpl.deleteList(listId);
        Log.d(DeleteListService.class.getName(), "Deleted list with id: " + listId);
        ((HoneyDewApplication)getApplicationContext()).getListsIdsToDelete().remove(listId);

    }
}

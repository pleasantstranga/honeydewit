package com.honeydewit.services;

import android.app.IntentService;
import android.content.Intent;
import android.widget.ArrayAdapter;

import com.honeydewit.HoneyDewApplication;
import com.honeydewit.ListsHomeAdapterData;
import com.honeydewit.adapters.ListsHomeAdapter;

import java.util.HashMap;
import java.util.Map;

public class UpdateListsRowNumService extends IntentService {

    public UpdateListsRowNumService() {
        super("UpdateRowNumbersTask");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Map<Integer, Integer> listPositions = (HashMap)intent.getExtras().get("positions");
            ((HoneyDewApplication)getApplicationContext()).getShoppingListDbHelper().updateListRowNumbersByAdapterPosition(listPositions);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
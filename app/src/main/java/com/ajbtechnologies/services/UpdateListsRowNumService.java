package com.ajbtechnologies.services;

import android.app.IntentService;
import android.content.Intent;

import com.ajbtechnologies.Application;

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
            ((Application)getApplicationContext()).getShoppingListDbHelper().updateListRowNumbersByAdapterPosition(listPositions);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
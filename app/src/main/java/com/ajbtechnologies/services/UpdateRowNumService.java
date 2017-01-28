package com.ajbtechnologies.services;

import android.app.IntentService;
import android.content.Intent;

import com.ajbtechnologies.Application;

import java.util.ArrayList;

public class UpdateRowNumService extends IntentService {

    public UpdateRowNumService() {
        super("UpdateRowNumService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            ArrayList<Integer> adapter = (ArrayList)intent.getSerializableExtra("ids");
            ((Application)getApplicationContext()).getShoppingListDbHelper().updateListItemRowNumbersByListPosition(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
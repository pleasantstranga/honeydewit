package com.honeydewit.services;

import android.app.IntentService;
import android.content.Intent;

import com.honeydewit.HoneyDewApplication;

import java.util.ArrayList;

public class UpdateRowNumService extends IntentService {

    public UpdateRowNumService() {
        super("UpdateRowNumService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            ArrayList<Integer> adapter = (ArrayList)intent.getSerializableExtra("ids");
            ((HoneyDewApplication)getApplicationContext()).getShoppingListDbHelper().updateListItemRowNumbersByListPosition(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
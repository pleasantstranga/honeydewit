package com.honeydewit.calendar;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.honeydewit.dataaccess.DbHelperImpl;
import com.honeydewit.pojos.ListEvent;

import java.sql.SQLException;
import java.util.List;

public class CalendarChangeReceiver extends BroadcastReceiver {
   

    @Override
    public void onReceive(Context context, Intent intent) { 	
    	try {
    		DbHelperImpl dbHelper = new DbHelperImpl(context);
    		CalendarDbHelper calDbHelper = new CalendarDbHelper();
			List<ListEvent> listEvents = dbHelper.getListEventsDao().queryForAll();
			for(ListEvent event : listEvents) {
				if(calDbHelper.isListEventDeleted(context, event)) {
					dbHelper.getListEventsDao().delete(event);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(Throwable t) {
			t.printStackTrace();
		}
    }
}
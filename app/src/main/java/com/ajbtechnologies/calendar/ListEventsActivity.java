package com.ajbtechnologies.calendar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ajbtechnologies.BasicActivity;
import com.ajbtechnologies.Constants;
import com.ajbtechnologies.R;
import com.ajbtechnologies.adapters.ListEventInstancesAdapter;
import com.ajbtechnologies.pojos.BasicList;
import com.ajbtechnologies.pojos.ListEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class ListEventsActivity extends BasicActivity implements OnClickListener {
	private BasicList basicList;
	private CalendarDbHelper calendarDbHelper;
	private ListEventInstancesAdapter listAdapter;
	private Button currentDateBtn;
	private Button previousMonthButton;
	private Button nextMonthButton;
	private GregorianCalendar monthToShow;
	private ListView eventsList;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventlistnav);
        eventsList = (ListView) findViewById(R.id.eventList);
        basicList = getApplicationContext().getCurrentList();
        monthToShow = DateUtil.getBeginningOfMonth(new GregorianCalendar());
        calendarDbHelper = new CalendarDbHelper();
        
        if(isEventDeleted()) {
        	deleteListEvent(basicList.getFirstEvent());
        	setResult(Constants.RESPONSE_ERROR);
        	finish();        	
        }
        else {
            GregorianCalendar today = new GregorianCalendar();
            initialiseNavigation(today);
            initList(basicList.getFirstEvent());
        }
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.action_bar_add_item_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	private void initialiseNavigation(GregorianCalendar today) {
		currentDateBtn = (Button)findViewById(R.id.currentDate);
		currentDateBtn.setText(DateUtil.monthYear.format(today.getTime()));
		previousMonthButton = (Button)findViewById(R.id.previous);
		previousMonthButton.setOnClickListener(this);
		nextMonthButton = (Button)findViewById(R.id.next);
		nextMonthButton.setOnClickListener(this);
		
		previousMonthButton.setText(DateUtil.getPreviousMonthAbbreviated(today));
		nextMonthButton.setText(DateUtil.getNextMonthAbbreviated(today));
	}
	@Override
	public void onClick(View v) {
		if(v == nextMonthButton) {
			monthToShow.add(Calendar.MONTH, 1);
			refreshList(basicList.getFirstEvent(),monthToShow);
		}
		if(v == previousMonthButton) {
			monthToShow.add(Calendar.MONTH, -1);
			refreshList(basicList.getFirstEvent(),monthToShow);
		}
		
	}
	private void deleteListEvent(ListEvent listEvent) {
		try {
			getApplicationContext().getShoppingListDbHelper().getListEventsDao().delete(listEvent);
			basicList.removeEvent(listEvent);
		
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	private boolean isEventDeleted() {
		ListEvent listEvent = basicList.getFirstEvent();
		boolean eventDeleted = false;
		Event event = calendarDbHelper.getCalendarEvent(this, listEvent.getCalendarId(), listEvent.getEventId());
		if(null == event) {
			eventDeleted = true;
		}
		else {
			List<EventInstance> instances = calendarDbHelper.getEventInstances(getApplicationContext(), listEvent.getCalendarId(), listEvent.getEventId());
			if(instances == null || instances.size() == 0) {
				Toast.makeText(this, getString(R.string.eventRemoved), Toast.LENGTH_LONG).show();
				eventDeleted = true;
			}
		}
		
		return eventDeleted;
		
	}
	private void initList(ListEvent listEvent) {
		if(listAdapter == null) {
			listAdapter = new ListEventInstancesAdapter(getApplicationContext(), new ArrayList<EventInstance>());
			eventsList.setAdapter(listAdapter);
		}
		List<EventInstance> events = calendarDbHelper.getEventInstancesByMonth(getApplicationContext(), listEvent.getCalendarId(), listEvent.getEventId(), monthToShow);
		if(events != null && events.size() > 0) {
			listAdapter.clear();
			listAdapter.addAll(events);
		}
		else {
			EventInstance nextInstance = calendarDbHelper.getNextEventInstance(getApplicationContext(), listEvent.getCalendarId(), listEvent.getEventId(), monthToShow);
			TextView nextEventText = (TextView)findViewById(R.id.nextEvent);
			if(null != nextInstance) {
				nextEventText.setText(getResources().getString(R.string.nextInstance).replace(Constants.STRING_REPLACE_CHAR, DateUtil.monthDayYear.format(nextInstance.getDateFrom().getTime())));
			}
			else {
				nextEventText.setText("No Future Events Exist.");
			}
		}
		
	}
	private void refreshList(ListEvent listEvent, GregorianCalendar startMonth) {
		listAdapter.clear();
		listAdapter.notifyDataSetChanged();
		currentDateBtn.setText(DateUtil.monthYear.format(startMonth.getTime()));
		previousMonthButton.setText(DateUtil.getPreviousMonthAbbreviated(startMonth));
		nextMonthButton.setText(DateUtil.getNextMonthAbbreviated(startMonth));
		TextView nextEventText = (TextView)findViewById(R.id.nextEvent);
		List<EventInstance> events = calendarDbHelper.getEventInstancesByMonth(getApplicationContext(), listEvent.getCalendarId(), listEvent.getEventId(), DateUtil.getBeginningOfMonth(startMonth));
		if(events != null && events.size() > 0) {
			listAdapter.addAll(events);
			nextEventText.setText(null);
		}
		else {
			EventInstance nextInstance = calendarDbHelper.getNextEventInstance(getApplicationContext(), listEvent.getCalendarId(), listEvent.getEventId(), startMonth);
			if(nextInstance != null) {
				nextEventText.setText(getResources().getString(R.string.nextInstance).replace(Constants.STRING_REPLACE_CHAR, DateUtil.monthDayYear.format(nextInstance.getDateFrom().getTime())));
			}
			else {
				nextEventText.setText(R.string.noFutureEvent);
			}
		}
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
		int itemId = item.getItemId();
	    if(itemId == R.id.addItem) {
	        	Toast.makeText(this, "Pressed", Toast.LENGTH_LONG).show();
	            return true;
	    }
	    else {
	    	return super.onOptionsItemSelected(item);
	    }
	}

}

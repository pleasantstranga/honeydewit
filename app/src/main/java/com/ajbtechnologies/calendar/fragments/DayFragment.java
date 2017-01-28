package com.ajbtechnologies.calendar.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ajbtechnologies.Application;
import com.ajbtechnologies.R;
import com.ajbtechnologies.adapters.ListEventInstancesAdapter;
import com.ajbtechnologies.calendar.DateUtil;
import com.ajbtechnologies.calendar.EventInstance;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;


public class DayFragment extends Fragment implements OnClickListener {
	private GregorianCalendar currentDate;
	private ListEventInstancesAdapter adapter;
	private Button dateButton;
	private Button previousDay;
	private Button nextDay;
	private Context context;
	private ListView list;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		context = getActivity();		
		setRetainInstance(true);
		
		
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.eventlistnav, container, false);
		dateButton = (Button)layout.findViewById(R.id.currentDate);
		dateButton.setOnClickListener(this);
		previousDay = (Button) layout.findViewById(R.id.previous);
		previousDay.setOnClickListener(this);
		nextDay= (Button) layout.findViewById(R.id.next);
		nextDay.setOnClickListener(this);
		list = (ListView)layout.findViewById(R.id.eventList);
		setAdapter(context);
		
		return layout;
		
	}
	private void setDateButtonTxt() {
		if(currentDate != null) {
			dateButton.setText(DateUtil.dateFormat.format(currentDate.getTime()));
			previousDay.setText(DateUtil.getPreviousDayAbbreviated(currentDate));
			nextDay.setText(DateUtil.getNextDayAbbreviated(currentDate));
		}
	}
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if(!hidden) {
			setDateButtonTxt();
			populateEvents(getDailyEventsInstances(currentDate));
		}
	}
	private void populateEvents(List<EventInstance> listEvents) {
		
		adapter.clear();
		if(null != listEvents) {
			adapter.addAll(listEvents);
		}
		adapter.notifyDataSetChanged();
	    
	}
	public void setAdapter(Context context) {
		if(adapter == null) {
			adapter = new ListEventInstancesAdapter(context, new ArrayList<EventInstance>());
			adapter.setShowDate(false);
			
		}
		list.setAdapter(adapter);
	}
	private List<EventInstance> getDailyEventsInstances(GregorianCalendar cal) {
		List<EventInstance> dailyEvents = new ArrayList<EventInstance>();
		SparseArray<LinkedList<EventInstance>> events = ((Application)context.getApplicationContext()).getCalDbHelper().getMonthlyEventsInstances(this.getActivity(), 1, DateUtil.getBeginningOfDay(cal), DateUtil.getEndOfDay(cal));
		if(null != events.get(cal.get(Calendar.DAY_OF_MONTH))) {
			dailyEvents.addAll(events.get(cal.get(Calendar.DAY_OF_MONTH))) ;
		}
		return dailyEvents;
	}
	
	@Override
	public void onClick(View v) {
		if(v == previousDay) {
			currentDate.add(Calendar.DAY_OF_MONTH, -1);
			setDateButtonTxt();
			adapter.clear();
			adapter.addAll(getDailyEventsInstances(currentDate));
		}
		else if(v == nextDay) {
			currentDate.add(Calendar.DAY_OF_MONTH, 1);
			setDateButtonTxt();
			adapter.clear();
			adapter.addAll(getDailyEventsInstances(currentDate));
		}
		if(v == dateButton) {	
			dateButton.setEnabled(false);
			DatePickerDialog dialog = new DatePickerDialog(getActivity(), datePickerListener,  currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH),currentDate.get(Calendar.DAY_OF_MONTH));
			dialog.setOnDismissListener(datePickerDismissListener);
			dialog.show();
		}
		adapter.notifyDataSetChanged();
		
	}
	public GregorianCalendar getCurrentDate() {
		return currentDate;
	}
	
	public void initDate(GregorianCalendar date) {
		currentDate = date;
	}
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			currentDate.set(Calendar.DAY_OF_MONTH, selectedDay);
			currentDate.set(Calendar.MONTH, selectedMonth);
			currentDate.set(Calendar.YEAR, selectedYear);
			setDateButtonTxt();
			adapter.clear();
			adapter.addAll(getDailyEventsInstances(currentDate));
		}

	};
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setDateButtonTxt();
	}
	private DialogInterface.OnDismissListener datePickerDismissListener =
			new DialogInterface.OnDismissListener() {
		public void onDismiss(DialogInterface dialog) {
			dateButton.setEnabled(true);
		}
	};
}
package com.ajbtechnologies.calendar.fragments;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ajbtechnologies.Application;
import com.ajbtechnologies.R;
import com.ajbtechnologies.adapters.ListEventInstancesAdapter;
import com.ajbtechnologies.calendar.DateUtil;
import com.ajbtechnologies.calendar.EventActivity;
import com.ajbtechnologies.calendar.EventInstance;
import com.ajbtechnologies.calendar.MonthlyEventInstances;
import com.ajbtechnologies.calendar.customviews.CalendarView;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class MonthFragment extends Fragment implements OnClickListener,	OnLongClickListener, OnDateSetListener {

	private CalendarView calendarView;
	private ListEventInstancesAdapter eventAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setRetainInstance(true);

		LinearLayout monthtab = (LinearLayout) inflater.inflate(R.layout.monthtab, container, false);
		ListView listEvents = (ListView) monthtab.findViewById(android.R.id.list);

		eventAdapter = new ListEventInstancesAdapter(getActivity(), new ArrayList<EventInstance>());
		eventAdapter.setShowDate(false);

		calendarView = (CalendarView) monthtab.findViewById(R.id.calendarView);
		calendarView.addDateCellOnClickListener(this);
		calendarView.addDateCellOnLongClickListener(this);
		calendarView.addNavigationOnClickListener(this);
		calendarView.addOnDateSetListener(this);
		calendarView.setMonthlyEvents(getMonthlyEvents());
		listEvents.setAdapter(eventAdapter);

		return monthtab;
	}
	
	public CalendarView getCalendarView() {
		return calendarView;
	}

	private MonthlyEventInstances getMonthlyEvents() {
		return  ((Application)getActivity().getApplicationContext()).getCalDbHelper().getMonthlyEventsInstances(
				getActivity(), 1, calendarView.getMonth(),
				DateUtil.getEndOfMonth(calendarView.getMonth()));
		
		
	}
	@Override
	public void onClick(View v) {
		
		if (calendarView.isNextMonthButtonClicked(v)|| calendarView.isPreviousMonthButtonClicked(v)) {
			resetMonthlyEvents();

		}
		else if (calendarView.isDateView(v)) {
			if (null != getActivity() && getActivity().getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
				showDailyEvents(v);
			}
		}
	}

	public void resetMonthlyEvents() {
		calendarView.setMonthlyEvents(getMonthlyEvents());
		eventAdapter.clear();
		eventAdapter.notifyDataSetChanged();
	}
	public void initDate(GregorianCalendar date) {
		calendarView.setDateSelected(date);
		resetMonthlyEvents();
	}
	

	@Override
	public boolean onLongClick(View calendarView) {
		if(null != getActivity()) {
			Intent eventIntent = new Intent(getActivity(), EventActivity.class);
			GregorianCalendar dateTime = DateUtil.getCalendarFromString(calendarView.getTag().toString());
			dateTime = DateUtil.setCurrentHourDate(dateTime);
			eventIntent.putExtra("dateFrom",dateTime.getTimeInMillis());
			getActivity().startActivity(eventIntent);
			getActivity().finish();
		
		}
		
		return false;
	}

	private void showDailyEvents(View view) {
		Integer dayOfMonth = Integer.valueOf(view.getTag().toString().split("/")[1]);
		List<EventInstance> eventsForDay = calendarView.getMonthlyEvents().get(dayOfMonth);
		eventAdapter.clear();
		if (null != eventsForDay) {
			eventAdapter.addAll(eventsForDay);
		}
		eventAdapter.notifyDataSetChanged();
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
		resetMonthlyEvents();
		
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

}
package com.ajbtechnologies.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.ajbtechnologies.Constants;
import com.ajbtechnologies.ListInfoActivity;
import com.ajbtechnologies.MainMenuActivity;
import com.ajbtechnologies.R;
import com.ajbtechnologies.calendar.fragments.DayFragment;
import com.ajbtechnologies.calendar.fragments.MonthFragment;

public class CalendarActivity extends FragmentActivity {

	private String previousActivity;
	private final String DAY = "DAY";
	private final String MONTH = "MONTH";
	private static MonthFragment monthFragment;
	private static DayFragment dayFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.calendar_fragments);

		if(savedInstanceState != null) {
			previousActivity = savedInstanceState.getString(Constants.PREV_ACTIVITY);
		}
		super.onCreate(savedInstanceState);
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		setMonthFragment(null);
		setDayFragment(null);
		if(null != previousActivity) {
			if(previousActivity.equals(Constants.LIST_INFO) ){
				Intent event = new Intent(getApplicationContext(), ListInfoActivity.class);
		    	startActivity(event);
			}
		}
		else {
			Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenuActivity.class);
	    	startActivity(mainMenuIntent);
		}
		finish();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_bar_calendar_menu, menu);

	    return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		if(item.getTitle().toString().equalsIgnoreCase(DAY)){
			dayFragment.initDate(getMonthFragment().getCalendarView().getSelectedDate());
			ft.show(dayFragment);
			ft.hide(getMonthFragment());
		}else{
			if(null != dayFragment.getCurrentDate()) {
				getMonthFragment().initDate(dayFragment.getCurrentDate());
			}
			ft.show(getMonthFragment());
			ft.hide(dayFragment);
		}
		ft.commit();
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Constants.RESULT_EVENT_SAVED) {
			Toast.makeText(this, getString(R.string.eventSaved), Toast.LENGTH_LONG).show();
			setResult(Constants.RESULT_EVENT_SAVED, data);
			finish();
		}
	}
	private void initialiseFragments() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		if(getDayFragment() == null) {
			setDayFragment(new DayFragment());
			ft.add(R.id.fragment_container, dayFragment, DAY);
			ft.hide(dayFragment);
		}
		if(getMonthFragment() == null ) {
			setMonthFragment(new MonthFragment());
			ft.add(R.id.fragment_container, getMonthFragment(), MONTH);

		}
		ft.commit();

	}
	private static MonthFragment getMonthFragment() {
		return monthFragment;
	}
	private static void setMonthFragment(MonthFragment monthFragment) {
		CalendarActivity.monthFragment = monthFragment;
	}
	private static DayFragment getDayFragment() {
		return dayFragment;
	}
	private static void setDayFragment(DayFragment dayFragment) {
		CalendarActivity.dayFragment = dayFragment;
	}
	private void resetFragments() {
		setDayFragment(null);
		setMonthFragment(null);
	}
	@Override
	public void finish() {
		super.finish();
		resetFragments();

	}

	@Override
	protected void onStart() {
		super.onStart();
		initialiseFragments();
	}
}


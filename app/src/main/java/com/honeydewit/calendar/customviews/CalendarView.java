package com.honeydewit.calendar.customviews;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.honeydewit.R;
import com.honeydewit.calendar.DateUtil;
import com.honeydewit.calendar.MonthlyEventInstances;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarView extends LinearLayout implements OnClickListener {

	private Button currentMonthBtn;
	private Button prevMonth;
	private Button nextMonth;
	private CalendarGridView calendarGridView;
	private boolean saveOnConfigChange = true;
	private List<OnClickListener> calendarNavigationOnClickListeners = new ArrayList<View.OnClickListener>();
	private ArrayList<OnDateSetListener> dateSetListeners = new ArrayList<OnDateSetListener>();
	private Context context;

	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		View layout = inflate(getContext(), R.layout.calendar_view, null);

		prevMonth = (Button) layout.findViewById(R.id.previous);
		prevMonth.setOnClickListener(this);
		

		nextMonth = (Button) layout.findViewById(R.id.next);
		nextMonth.setOnClickListener( this);

		currentMonthBtn = (Button) layout.findViewById(R.id.currentDate);
		currentMonthBtn.setOnClickListener(this);
		calendarGridView = (CalendarGridView)layout.findViewById(R.id.calendar);
		currentMonthBtn.setText(DateUtil.monthYear.format(calendarGridView.getMonth().getTime()));
		prevMonth.setText(DateUtil.getPreviousMonthAbbreviated(calendarGridView.getMonth()));
		nextMonth.setText(DateUtil.getNextMonthAbbreviated(calendarGridView.getMonth()));

		saveOnConfigChange = attrs.getAttributeBooleanValue("http://schemas.android.com/apk/res/com.honeydewit", "saveMonthOnConfigChange", true);

		addView(layout);

	}


	@Override
	public Parcelable onSaveInstanceState() {
		super.onSaveInstanceState();
		if(saveOnConfigChange) {
			Bundle bundle = new Bundle();
			bundle.putParcelable("instanceState", super.onSaveInstanceState());
			bundle.putString("date",DateUtil.monthDayYear.format(calendarGridView.getMonth().getTime()));
			if(calendarGridView.getSelectedDateView() != null) {
				bundle.putString("selectedDate", calendarGridView.getSelectedDateView().getTag().toString());
			}
			bundle.putSerializable("monthlyEvents", calendarGridView.getMonthlyEvents());
			return bundle;
		}
		return BaseSavedState.EMPTY_STATE;
	}

	@Override
	public void onRestoreInstanceState(Parcelable state) {
		if (state instanceof Bundle && saveOnConfigChange ) {
			Bundle bundle = (Bundle) state;
			GregorianCalendar month = DateUtil.getCalendarFromString(bundle.getString("date"));
			if(bundle.getString("selectedDate") == null) {
				bundle.putString("selectedDate", DateUtil.monthDayYear.format(new Date()));
			}
			Integer selectedDate = Integer.valueOf(bundle.getString("selectedDate").split("/")[1]);
			MonthlyEventInstances events = (MonthlyEventInstances) bundle.getSerializable("monthlyEvents");
			calendarGridView.setMonth(month, selectedDate);
			calendarGridView.setMonthlyEvents(events);
			currentMonthBtn.setText(DateUtil.monthYear.format(calendarGridView.getMonth().getTime()));
			prevMonth.setText(DateUtil.getPreviousMonthAbbreviated(calendarGridView.getMonth()));
			nextMonth.setText(DateUtil.getNextMonthAbbreviated(calendarGridView.getMonth()));

			super.onRestoreInstanceState(bundle.getParcelable("instanceState"));
		}
		super.onRestoreInstanceState(BaseSavedState.EMPTY_STATE);
	}
	public void setDateSelected(GregorianCalendar cal) {
		calendarGridView.setMonth(cal, cal.get(Calendar.DAY_OF_MONTH));
		currentMonthBtn.setText(DateUtil.monthYear.format(calendarGridView.getMonth().getTime()));
		prevMonth.setText(DateUtil.getPreviousMonthAbbreviated(calendarGridView.getMonth()));
		nextMonth.setText(DateUtil.getNextMonthAbbreviated(calendarGridView.getMonth()));

	}
	public void setMonth(GregorianCalendar cal) {
		calendarGridView.setMonth(DateUtil.getBeginningOfMonth(cal));
		currentMonthBtn.setText(DateUtil.monthYear.format(calendarGridView.getMonth().getTime()));
		prevMonth.setText(DateUtil.getPreviousMonthAbbreviated(calendarGridView.getMonth()));
		nextMonth.setText(DateUtil.getNextMonthAbbreviated(calendarGridView.getMonth()));

	}
	@Override
	public void onClick(View v) {
		if(null != calendarGridView && null != calendarGridView.getSelectedDateView() && null != calendarGridView.getSelectedDateView().getTag()) {
			String[] cellTag = calendarGridView.getSelectedDateView().getTag().toString().split("/");
			if (v == prevMonth) {
				GregorianCalendar cal = calendarGridView.getMonth();
				cal.add(Calendar.MONTH, -1);
				calendarGridView.setMonth(cal,Integer.valueOf(cellTag[1]));
				currentMonthBtn.setText(DateUtil.monthYear.format(calendarGridView.getMonth().getTime()));
				prevMonth.setText(DateUtil.getPreviousMonthAbbreviated(calendarGridView.getMonth()));
				nextMonth.setText(DateUtil.getNextMonthAbbreviated(calendarGridView.getMonth()));
			}
			if (v == nextMonth) {
				GregorianCalendar cal = calendarGridView.getMonth();
				cal.add(Calendar.MONTH, 1);
				calendarGridView.setMonth(cal,Integer.valueOf(cellTag[1]));
				currentMonthBtn.setText(DateUtil.monthYear.format(calendarGridView.getMonth().getTime()));
				prevMonth.setText(DateUtil.getPreviousMonthAbbreviated(calendarGridView.getMonth()));
				nextMonth.setText(DateUtil.getNextMonthAbbreviated(calendarGridView.getMonth()));

			}
			if(v == currentMonthBtn) {	
				currentMonthBtn.setEnabled(false);
				DatePickerDialog dialog = new DatePickerDialog(getContext(), datePickerListener,  Integer.valueOf(cellTag[2]), Integer.valueOf(cellTag[0])-1,Integer.valueOf(cellTag[1]));
				dialog.setOnDismissListener(datePickerDismissListener);
				dialog.show();
			}

			for(OnClickListener listener : calendarNavigationOnClickListeners) {
				listener.onClick(v);
			}
		}
	}

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			GregorianCalendar newCal = new GregorianCalendar();
			newCal.set(Calendar.MONTH, selectedMonth);
			newCal.set(Calendar.DAY_OF_MONTH, 1);
			newCal.set(Calendar.YEAR, selectedYear);
			calendarGridView.setMonth(newCal, selectedDay);
			currentMonthBtn.setText(DateUtil.monthYear.format(calendarGridView.getMonth().getTime()));
			currentMonthBtn.setEnabled(true);
			prevMonth.setText(DateUtil.getPreviousMonthAbbreviated(calendarGridView.getMonth()));
			nextMonth.setText(DateUtil.getNextMonthAbbreviated(calendarGridView.getMonth()));

			for(OnDateSetListener listener : dateSetListeners) {
				listener.onDateSet(view, selectedYear, selectedMonth, selectedDay);
			}
		}

	};
	
	private DialogInterface.OnDismissListener datePickerDismissListener =
			new DialogInterface.OnDismissListener() {
		public void onDismiss(DialogInterface dialog) {
			currentMonthBtn.setEnabled(true);
		}
	};
	public void addDateCellOnClickListener(OnClickListener listener){
		calendarGridView.addOnClickListener(listener);
	}
	public void addDateCellOnLongClickListener(OnLongClickListener listener){
		calendarGridView.addOnLongClickListener(listener);
	}
	public GregorianCalendar getMonth() {
		return calendarGridView.getMonth();
	}
	
	public void addNavigationOnClickListener(OnClickListener onClickListener) {
		this.calendarNavigationOnClickListeners.add(onClickListener);
	}
	public boolean isPreviousMonthButtonClicked(View view) {
		return view == prevMonth;
	}
	public boolean isNextMonthButtonClicked(View view) {
		return view == nextMonth;
	}
	public GregorianCalendar getSelectedDate() {
		return DateUtil.getCalendarFromString(calendarGridView.getSelectedDateView().getTag().toString());
	}
	public String getSelectedDateString() {
		return calendarGridView.getSelectedDateView().getTag().toString();
	}
	public boolean isDateView(View view) {
		return view.findViewById(R.id.day) != null && null != view.findViewById(R.id.day).getTag();
	}
	public MonthlyEventInstances getMonthlyEvents() {
		return calendarGridView.getMonthlyEvents();
	}
	public GregorianCalendar getCurrentMonthStart() {
		return calendarGridView.getMonth();
	}
	
	public void setMonthlyEvents(MonthlyEventInstances monthlyEvents) {
		calendarGridView.setMonthlyEvents(monthlyEvents);
	}
	public boolean isCurrentMonthButtonClicked(View view) {
		return view == currentMonthBtn;
	}
	public void addOnDateSetListener(OnDateSetListener listener) {
		dateSetListeners.add(listener);
	}
	public boolean isSaveOnConfigChange() {
		return saveOnConfigChange;
	}
	public void setSaveOnConfigChange(boolean saveOnConfigChange) {
		this.saveOnConfigChange = saveOnConfigChange;
	}
}

package com.ajbtechnologies.calendar.customviews;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.ajbtechnologies.R;
import com.ajbtechnologies.R.color;
import com.ajbtechnologies.calendar.DateUtil;
import com.ajbtechnologies.calendar.MonthlyEventInstances;
import com.ajbtechnologies.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

public class CalendarGridView extends GridView {
	private static List<OnClickListener> onClickListeners = new ArrayList<OnClickListener>();
	private static List<OnLongClickListener> onLongClickListeners = new ArrayList<View.OnLongClickListener>();
	private static final int NUM_COLUMNS = 7;
	private static GregorianCalendar adapterMonth;

	public CalendarGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setNumColumns(NUM_COLUMNS);
		setAdapter(new CalendarGridAdapter(context, DateUtil.getBeginningOfMonth((GregorianCalendar)Calendar.getInstance())));
	}
	
	public void setMonthlyEvents(MonthlyEventInstances monthlyEvents) {
		((CalendarGridAdapter)getAdapter()).monthlyEvents = monthlyEvents;
	}
	public MonthlyEventInstances getMonthlyEvents() {
		return ((CalendarGridAdapter)getAdapter()).monthlyEvents;
	}
	private int getDateToSelect(int currentSelectedDate, GregorianCalendar newMonth) {
		int newMonthMaxDate = newMonth.getActualMaximum(Calendar.DAY_OF_MONTH);

		if(currentSelectedDate > newMonthMaxDate) {
			return newMonthMaxDate;
		}
		return currentSelectedDate;
	}

	public GregorianCalendar getMonth() {
		return CalendarGridView.adapterMonth;
	}

	public void addOnClickListener(OnClickListener onCLickListner) {
		((CalendarGridAdapter)getAdapter()).addOnClickListener(onCLickListner);
	}
	public void addOnLongClickListener(OnLongClickListener onLongClickListener) {
		((CalendarGridAdapter)getAdapter()).addLongClickListener(onLongClickListener);
	}
	
	public void setMonth(GregorianCalendar month) {
		setAdapter(new CalendarGridAdapter(getContext(), DateUtil.getBeginningOfMonth(month)));
	}
	public void setMonth(GregorianCalendar month, int currentSelectedDate) {
		int dayToSelect = getDateToSelect(currentSelectedDate, month);	
		setAdapter(new CalendarGridAdapter(getContext(), DateUtil.getBeginningOfMonth(month), dayToSelect));
	}
	public View getSelectedDateView() {
		return ((CalendarGridAdapter)getAdapter()).getSelectedDateView();
	}
	private class CalendarGridAdapter extends BaseAdapter implements OnClickListener, OnLongClickListener  {

		private final int SELECTED_DATE_BG = getResources().getColor(color.calendarSelectedDateBG);
		private final int CURRENT_DATE_BG = getResources().getColor(color.calendarCurrentDateBG);
		private final int CELL_BG = getResources().getColor(color.calendarCellDefaultBG);
		private List<String> calendarCellValues;
		private MonthlyEventInstances monthlyEvents = new MonthlyEventInstances();
		private View selectedDateView = null;

		private GregorianCalendar currentDate = new GregorianCalendar();
		private Integer dayToSelect = null;
		private boolean selectCurrentDate = true;	

		public CalendarGridAdapter(Context context, GregorianCalendar adapterMonth)
		{
			super();

			CalendarGridView.adapterMonth = adapterMonth;
			calendarCellValues = getMonthGridValues();	
			setSelectCurrentDate(true);

		}
		public CalendarGridAdapter(Context context, GregorianCalendar adapterMonth, int initialDateToSelect)
		{
			super();
			CalendarGridView.adapterMonth = adapterMonth;
			calendarCellValues = getMonthGridValues();	
			dayToSelect = initialDateToSelect;
			setSelectCurrentDate(false);
		}
		/**
		 * Prints Month
		 *
		 */
		 private List<String> getMonthGridValues() {
			 List<String> monthGridValues = new LinkedList<String>();
			 int daysInMonth = adapterMonth.getActualMaximum(Calendar.DAY_OF_MONTH);
			 int firstDayOfMonth = adapterMonth.get(Calendar.DAY_OF_WEEK) -1;
			 int dayCount = 0;

			 monthGridValues.addAll(Arrays.asList(DateUtil.daysInWeek));	
			 for(Integer x = -(firstDayOfMonth); x<=daysInMonth;x++) {
				 //preceeding blank spaces
				 if(x<0) {
					 dayCount++;
					 monthGridValues.add("  ");
				 }
				 //days
				 else if(x>0) {

					 dayCount++;
					 monthGridValues.add(String.valueOf(x));
				 }
			 }
			 //trailing blank grid
			 if(dayCount%7 !=0) {
				 while(dayCount%7!=0) {
					 monthGridValues.add(null);
					 dayCount++;
				 }
			 }
			 return monthGridValues;
		 }


		 @Override
		 public long getItemId(int position)
		 {
			 return position;
		 }

		 @Override
		 public View getView(int cellIndex, View cell, ViewGroup gridView)
		 {
			 if (cell == null)
			 {
				 //LayoutInflater inflater = (LayoutInflater) getCon.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				 cell = inflate(getContext(), R.layout.calendar_cell, null);
			 }
			 createCell(cellIndex, cell);

			 return cell;
		 }


		 private void createCell(int cellIndex, View cell) {
			 String cellValue = calendarCellValues.get(cellIndex);
			 cell.setBackgroundColor(CELL_BG);
			 if(null != cellValue) {
				 TextView day = (TextView) cell.findViewById(R.id.day);
				 day.setText(cellValue);
				 day.setTextColor(getResources().getColor(color.edit_text_color));
				 if(StringUtils.isInteger(cellValue)) {
					 day.setTag(createDateTag(cellValue));
					 cell.setTag(createDateTag(cellValue));
					 cell.setOnClickListener(this);
					 cell.setOnLongClickListener(this);
					 if(isCurrentDateCell(cell)) {
						 cell.setBackgroundColor(CURRENT_DATE_BG);
					 }
					 if((isSelectCurrentDate() && isCurrentDateCell(cell)) || !isSelectCurrentDate() && isSelectedDateCell(cellValue)) {
						 cell.performClick();	
					 }
					 addNumEventsToCell(cellIndex, cell);
				 }
				 else if(!StringUtils.isEmpty(cellValue)) {
					 cell.setBackgroundColor(getResources().getColor(color.calendarHeaderCellDefaultBG));
				 }
			 }
		 }
		 private boolean isCurrentDateCell(View cell) {
			 if(cell != null) {
				 if(adapterMonth.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR)) {
					 if(adapterMonth.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH)) {
							 if(currentDate.get(Calendar.DAY_OF_MONTH) == Integer.valueOf(cell.getTag().toString().split("/")[1])) {
								 
								 return true;
							 } 
						 
					 }
				 }
			 }
			
			 return false;
		 }
		 private boolean isSelectedDateCell(String cellValue) {
			 return Integer.valueOf(cellValue) == dayToSelect;
		 }

		 private void addNumEventsToCell(int dayNbr, View row) {
			 Integer dayOfMonth = Integer.valueOf(calendarCellValues.get(dayNbr));
			 if(null != monthlyEvents && null != monthlyEvents.get(dayOfMonth) && monthlyEvents.get(dayOfMonth).size()!= 0) {
				 TextView num_events_per_day = (TextView) row.findViewById(R.id.num_events_per_day);
				 num_events_per_day.setTextColor(Color.RED);
				 num_events_per_day.setText(String.valueOf(monthlyEvents.get(dayOfMonth).size()));	
			 }
		 }
		 public String getItem(int position)
		 {
			 return calendarCellValues.get(position);
		 }

		 @Override
		 public int getCount()
		 {
			 return calendarCellValues.size();
		 }
		 private String createDateTag(String dayNbr) {
			 return adapterMonth.get(Calendar.MONTH) + 	1 + "/" + dayNbr + "/" +  adapterMonth.get(Calendar.YEAR);
		 }

		 @Override
		 public void onClick(View cell) {
			 cell.setClickable(false);
			 cell.setEnabled(false);
			 try {
				 if(null != cell.getTag()) {
					 String day = cell.getTag().toString().split("/")[1];
					 if(StringUtils.isInteger(day)) {
						 setPrevioslySelectedCellBackground();
						 cell.setBackgroundColor(SELECTED_DATE_BG);
						 setSelectedDateView(cell);
						 for(OnClickListener listener : onClickListeners){
							 
							 listener.onClick(cell);
						 }
					 }
				 } 
			 }
			 finally {
				 cell.setClickable(true);
				 cell.setEnabled(true); 
			 }

		 }
		
		 @Override
		 public boolean onLongClick(View cell) {
			 
			 cell.setClickable(false);
			 cell.setEnabled(false);
			 try {
				 if(null != cell.getTag()) {
					 String day = cell.getTag().toString().split("/")[1];
					 if(StringUtils.isInteger(day)) {
						 setPrevioslySelectedCellBackground();
						 cell.setBackgroundColor(SELECTED_DATE_BG);
						 setSelectedDateView(cell);
						 for(OnLongClickListener listener : onLongClickListeners){
							 
							 listener.onLongClick(cell);
						 }
					 }
				 } 
			 }
			 finally {
				 cell.setClickable(true);
				 cell.setEnabled(true); 
			 }
			 
			 return false;
		 }

		 private void setPrevioslySelectedCellBackground() {

			 if(getSelectedDateView() != null) {
				 if(isCurrentDateCell(getSelectedDateView())) {
					 getSelectedDateView().setBackgroundColor(CURRENT_DATE_BG);
				 }
				 else {
					 getSelectedDateView().setBackgroundColor(CELL_BG);
				 }
			}
		 }
		 private void addOnClickListener(OnClickListener listener){
			 onClickListeners.add(listener);
		 }
		 private void addLongClickListener(OnLongClickListener listener){
			 onLongClickListeners.add(listener);
		 }

		 public View getSelectedDateView() {
			 return selectedDateView;
		 }
		 public void setSelectedDateView(View view) {
			 selectedDateView = view;
		 }
		
		public boolean isSelectCurrentDate() {
			return selectCurrentDate;
		}
		public void setSelectCurrentDate(boolean selectCurrentDate) {
			this.selectCurrentDate = selectCurrentDate;
		}
	}
}
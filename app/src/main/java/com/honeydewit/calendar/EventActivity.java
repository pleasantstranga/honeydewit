package com.honeydewit.calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.honeydewit.BasicActivity;
import com.honeydewit.Constants;
import com.honeydewit.HoneyDewDialogFragment;
import com.honeydewit.R;
import com.honeydewit.Reminder;
import com.honeydewit.listeners.OneOffClickListener;
import com.honeydewit.pojos.BasicList;
import com.honeydewit.pojos.ListEvent;
import com.honeydewit.validate.EventValidator;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

public class EventActivity extends BasicActivity {

	private static final String CUSTOM = "Custom";
	private TextView chosenRepeat;
	private EditText titleText;
	private Button fromDateBtn;
	private Button fromTimeBtn;
	private Button toDateBtn;
	private Button toTimeBtn;
	private Button saveBtn;
	private Spinner calendars;
	private Spinner timeZones;
	private Spinner repeatChoices;
	private Calendar dateFrom;
	private Calendar dateTo;
	private Switch allDay;
	private TableLayout reminderTable;
	private TableRow titleHeaderRow;
	private TableRow titleTextRow;
	private LayoutInflater inflater;
	private Integer reminderRowIndex;
	private RecurrenceRule rrule;
	private boolean ignoreRepeatSelection = false;
	private EventValidator eventValidator;
	private static final int CUSTOM_RECURRENCE_REQUEST_CDE = 1;
	private static final int DATE_FROM_DIALOG_ID = 999;
	private static final int TIME_FROM_DIALOG_ID = 998;
	private static final int DATE_TO_DIALOG_ID = 997;
	private static final int TIME_TO_DIALOG_ID = 996;
	private static final String BOTH = "b";
	private static final String FROM = "f";
	private static final String TO = "t";
	private Map<String, com.honeydewit.pojos.Calendar> calendarMap;	
	private BasicList list;
	private Long previousRepeat = 0L;
	private EventInstance eventInstance = null;
	
	private void initCancelOnClick() {
		Button cancelButton = (Button) findViewById(R.id.cancelBtn);
		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	private void addReminder(Reminder reminder) {
		final TableRow tr = (TableRow) inflater.inflate(R.layout.reminder_row, null);
		tr.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tr.setTag("reminderTimeRow" + reminderRowIndex);
		
		initialiseReminderMinutesDropDown(tr, reminder);
		initialiseReminderMethodDropDown(tr, reminder);
		reminderTable.addView(tr, reminderRowIndex++);
		
		ImageButton deleteBtn = (ImageButton)tr.findViewById(R.id.deleteReminderBtn);
		deleteBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				reminderRowIndex += -1;
				reminderTable.removeView(tr);
				
			}
		});
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event);
		eventValidator = new EventValidator();
		initialiseViews();
		list = getApplicationContext().getCurrentList();
		if(null != list) {
			setTitle(list.getListName());
		}
		else {
			setTitle(getText(R.string.newEvent));
			titleHeaderRow.setVisibility(View.VISIBLE);
			titleTextRow.setVisibility(View.VISIBLE);
			titleText = (EditText)findViewById(R.id.titleText);
			titleText.addTextChangedListener(new TextWatcher() {
				public void afterTextChanged(Editable s) {
				}

				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				public void onTextChanged(CharSequence s, int start, int before, int count) {
					if (titleText.getText().length() == 0) {
						setTitle(getText(R.string.newEvent));
					} else {
						setTitle(s.toString());
					}
				}
			});
		}
		if(null != getIntent().getExtras().get("eventInstance")) {
			eventInstance = (EventInstance)getIntent().getExtras().get("eventInstance");
			initReminders();
		}
		if(eventInstance != null && titleText.getVisibility() == View.VISIBLE) {
			titleText.setText(eventInstance.getEvent().getTitle());
		}
		initDates();
		initTimeZone();
		populateDateBtnText(BOTH);
		populateTimeBtnText(BOTH);
		initialiseCalendarDropDown();
		initialiseRecurrenceDropDown();
		initialiseSaveButton();
		initAllDay();

	}

	private void initialiseRecurrenceDropDown() {
		ArrayAdapter<String> repeatChoicesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, RepeatTypes.getRepeatTypes());
		repeatChoicesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		repeatChoices.setAdapter(repeatChoicesAdapter);
		
		repeatChoices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
		    	
		    	if(!ignoreRepeatSelection) {
		    		String selectedItem = null != selectedItemView ? ((TextView)selectedItemView).getText().toString() : "None";
		    		if(!selectedItem.equals("None")) {
		    			
			    		showRepeatDialog(selectedItem);
			    	} 	
			    	else {
			    		chosenRepeat.setTag(null);
			    		chosenRepeat.setText("");		    	
			    	}
		    	}
		    	ignoreRepeatSelection = false;
		    }
		    
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		
			
		});
		
		
	}
	private void initialiseReminderMethodDropDown(TableRow tr, Reminder reminder) {
		Spinner reminderMethodSpinner = (Spinner)tr.findViewById(R.id.reminderMethod);
		ArrayAdapter<String> repeatMethodAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ReminderMethodTypes.getTitles());
		repeatMethodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		reminderMethodSpinner.setAdapter(repeatMethodAdapter);
		if(reminder != null) {
			String title = ReminderMethodTypes.getTitleByCode(reminder.getMethod());
			reminderMethodSpinner.setSelection(getIndex(reminderMethodSpinner, title));
		}
		
		
	}
	private void initialiseReminderMinutesDropDown(TableRow tr, Reminder reminder) {
		Spinner reminderMinutesSpinner = (Spinner)tr.findViewById(R.id.reminderSpinner);
		ArrayAdapter<String> reminderMinutesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ReminderMinutesTypes.getReminderMinutesTypes());
		reminderMinutesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		reminderMinutesSpinner.setAdapter(reminderMinutesAdapter);
		if(reminder != null) {
			String title = ReminderMinutesTypes.getTitleByCode(reminder.getMinutes());
			reminderMinutesSpinner.setSelection(getIndex(reminderMinutesSpinner, title));
		}
		
		
	}

	 private int getIndex(Spinner spinner, String myString)
	 {
	  int index = 0;

	  for (int i=0;i<spinner.getCount();i++){
	   if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
	    index = i;
	   }
	  }
	  return index;
	 } 
	private void initialiseCalendarDropDown() {
		calendarMap = getApplicationContext().getCalDbHelper().getCalendars(this);
		ArrayAdapter<String> calendarAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, new ArrayList<String>(calendarMap.keySet()));
		calendarAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		calendars.setAdapter(calendarAdapter);
		
		if(null == eventInstance) {
			String defaultCalendar = getDefaultCalendar();
			calendars.setSelection(calendarAdapter.getPosition(defaultCalendar));
		}
		else {
			eventInstance.getEvent().getCalendarId();
		}
		calendars.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
	}

	private void initDates() {
		if(null != eventInstance) {
			dateFrom = eventInstance.getDateFrom();
			dateTo = eventInstance.getDateTo();
		}
		else {
			dateFrom = Calendar.getInstance();
			if(getIntent().getExtras().containsKey("dateFrom")) {
				int hour = dateFrom.get(Calendar.HOUR_OF_DAY);
		        dateFrom.setTimeInMillis(getIntent().getLongExtra("dateFrom", -1));
		        dateFrom.set(Calendar.HOUR_OF_DAY, hour);
			}
			dateTo = (GregorianCalendar)dateFrom.clone();
			dateTo.add(Calendar.HOUR, 1);
		}
	}

	private void initialiseViews() {
		titleHeaderRow = (TableRow)findViewById(R.id.titleHeaderRow);
		titleText = (EditText)findViewById(R.id.titleText);
		titleTextRow = (TableRow)findViewById(R.id.titleTextRow);
		allDay = (Switch)findViewById(R.id.allDay);
		chosenRepeat = (TextView)findViewById(R.id.chosenRepeat);
		fromDateBtn = (Button)findViewById(R.id.fromDateBtn);
		fromTimeBtn = (Button)findViewById(R.id.fromTimeBtn);
		toDateBtn = (Button)findViewById(R.id.toDateBtn);
		toTimeBtn = (Button)findViewById(R.id.toTimeBtn);
		saveBtn = (Button)findViewById(R.id.saveBtn);
		timeZones = (Spinner) findViewById(R.id.timeZones);
		repeatChoices = (Spinner)findViewById(R.id.repeatChoices);
		calendars = (Spinner)findViewById(R.id.calendars);
		reminderTable = (TableLayout)findViewById(R.id.reminderTable);
		TableRow reminderRow = (TableRow) reminderTable.findViewById(R.id.reminderRow);
		reminderRowIndex = reminderTable.indexOfChild(reminderRow);
		ImageButton addReminderBtn = (ImageButton) findViewById(R.id.addRemoveReminderBtn);
		addDateTimeButtonListeners();
		inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		addReminderBtn.setOnClickListener(new OneOffClickListener() {

			@Override
			public void onClick(View v) {
				addReminder(null);

			}
		});
		initCancelOnClick();
	}
	
	private void showRepeatDialog(String selectedItem) {
		if(selectedItem.equals(CUSTOM)) {
			Intent customRecIntent = new Intent(this, CustomRecurrenceActivity.class);
			startActivityForResult(customRecIntent, 1);	
		}
		else {
			RecurrenceRule rule = new RecurrenceRule();
			if(selectedItem.equalsIgnoreCase("DAILY")) {
				rule.setFreq("DAILY");
			}
			else if(selectedItem.equalsIgnoreCase("WEEKLY")) {
				rule.setFreq("WEEKLY");
			}
			else if(selectedItem.equalsIgnoreCase("EVERY OTHER WEEK")) {
				rule.setFreq("WEEKLY");
				rule.setInterval(2);
				rule.setWeekStart("SU");
			}
			else if(selectedItem.equalsIgnoreCase("EVERY THREE WEEKS")) {
				rule.setFreq("WEEKLY");
				rule.setInterval(3);
				rule.setWeekStart("SU");
			}
			else if(selectedItem.equalsIgnoreCase("MONTHLY")) {
				rule.setFreq("MONTHLY");
				Set<String> byMonthDay = new HashSet<String>();
				byMonthDay.add(String.valueOf(dateFrom.get(Calendar.DAY_OF_MONTH)));
				rule.setByMonthDay(byMonthDay);
			}
			else if(selectedItem.equalsIgnoreCase("YEARLY")) {
				rule.setFreq("YEARLY");
				Set<String> byMonth = new HashSet<String>();
				byMonth.add(String.valueOf(dateFrom.get(Calendar.MONTH)));
				rule.setByMonth(byMonth);
				Set<String> byMonthDay = new HashSet<String>();
				byMonthDay.add(String.valueOf(dateFrom.get(Calendar.DAY_OF_MONTH)));
				rule.setByMonthDay(byMonthDay);
			}
			RepeatDialog repeatDialog=new RepeatDialog(this, dateFrom, rule);
			repeatDialog.setOnCancelListener(repeatDialogCancelListener);
			repeatDialog.setOnDismissListener(new RepeatDialogDismissListener(rule));
			repeatDialog.show();  
		}
		
		
	
	}
	
	private void populateDateBtnText(String dateType) {

		if(dateType.equals(BOTH)) {
			fromDateBtn.setText(DateUtil.dateFormat.format(dateFrom.getTime()));
			toDateBtn.setText(DateUtil.dateFormat.format(dateTo.getTime()));
		}
		else if(dateType.equals(FROM)) {
			fromDateBtn.setText(DateUtil.dateFormat.format(dateFrom.getTime()));
		}
		else if(dateType.equals(TO)) {
			toDateBtn.setText(DateUtil.dateFormat.format(dateTo.getTime()));
		}
		

	}
	private void populateTimeBtnText(String timeType) {
		
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
		
		if(timeType.equals(BOTH)) {
			fromTimeBtn.setText(timeFormat.format(dateFrom.getTime()));
			toTimeBtn.setText(timeFormat.format(dateTo.getTime()));
		}
		else if(timeType.equals(FROM)) {
			fromTimeBtn.setText(timeFormat.format(dateFrom.getTime()));
		}
		else if(timeType.equals(TO)) {
			toTimeBtn.setText(timeFormat.format(dateTo.getTime()));
		}
	}
	@SuppressWarnings("deprecation")
	public void addDateTimeButtonListeners() {
		fromDateBtn.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View v) {
				showDialog(DATE_FROM_DIALOG_ID);
			}
		});
		fromTimeBtn.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View v) {
				showDialog(TIME_FROM_DIALOG_ID);
			}
		});
		toDateBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				showDialog(DATE_TO_DIALOG_ID);
			}
		});
		toTimeBtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				showDialog(TIME_TO_DIALOG_ID);
			}
		});
	}
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_FROM_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, fromDatePickerListener, dateFrom.get(Calendar.YEAR), dateFrom.get(Calendar.MONTH),dateFrom.get(Calendar.DAY_OF_MONTH));

		case TIME_FROM_DIALOG_ID:
			return new TimePickerDialog(this, fromTimePickerListener, dateFrom.get(Calendar.HOUR_OF_DAY), dateFrom.get(Calendar.MINUTE), false);
		case DATE_TO_DIALOG_ID:
			// set date picker as current date
			return new DatePickerDialog(this, toDatePickerListener, dateTo.get(Calendar.YEAR), dateTo.get(Calendar.MONTH),dateTo.get(Calendar.DAY_OF_MONTH));

		case TIME_TO_DIALOG_ID:
			return new TimePickerDialog(this, toTimePickerListener, dateTo.get(Calendar.HOUR_OF_DAY), dateTo.get(Calendar.MINUTE), false);

		}
		
		return null;
	}

	private DatePickerDialog.OnDateSetListener fromDatePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			dateFrom.set(Calendar.YEAR, selectedYear);
			dateFrom.set(Calendar.MONTH, selectedMonth);
			dateFrom.set(Calendar.DAY_OF_MONTH, selectedDay);
			populateDateBtnText(FROM);
		}

	};
	private TimePickerDialog.OnTimeSetListener fromTimePickerListener = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			dateFrom.set(Calendar.HOUR_OF_DAY, selectedHour);
			dateFrom.set(Calendar.MINUTE, selectedMinute);

			populateTimeBtnText(FROM);
		}
	};
	private DatePickerDialog.OnDateSetListener toDatePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			
			dateTo.set(Calendar.YEAR, selectedYear);
			dateTo.set(Calendar.MONTH, selectedMonth);
			dateTo.set(Calendar.DAY_OF_MONTH, selectedDay);
			
			populateDateBtnText(TO);
		}

	};
	private TimePickerDialog.OnTimeSetListener toTimePickerListener = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {
			dateTo.set(Calendar.HOUR_OF_DAY, selectedHour);
			dateTo.set(Calendar.MINUTE, selectedMinute);

			populateTimeBtnText(TO);
		}
		
	};
	
	
	private Map<String,String> getTimeZoneMap() {
		
		Map<String, String> timezoneMap = new HashMap<String,String>();
		String [] ids = TimeZone.getAvailableIDs();
		for(String id:ids) {
		  TimeZone zone = TimeZone.getTimeZone(id);
		  int offset = zone.getRawOffset()/1000;
		  String dropdownValue = String.format("(GMT%+d:%02d) %s", offset/3600, (offset % 3600)/60, id);
		  timezoneMap.put(dropdownValue, zone.getID());
		} 
		return timezoneMap;
	}
	private void initTimeZone() {
		ArrayAdapter <CharSequence> adapter = new ArrayAdapter <CharSequence> (this, android.R.layout.simple_spinner_item );
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		timeZones.setAdapter(adapter);
		final Map<String, String> timezoneMap = getTimeZoneMap();
		Set<String> dropdownvalues = timezoneMap.keySet();
		List<String> list = new ArrayList<String>(dropdownvalues);
		Collections.sort(list);
		adapter.addAll(list);
		if(null != eventInstance) {	
			String eventTimeZone = eventInstance.getEvent().getEventTimezone();
			for(Map.Entry<String, String> entry : timezoneMap.entrySet()) {
				if(entry.getValue().equals(eventTimeZone)) {
					int position = list.indexOf(entry.getKey());
					timeZones.setSelection(position);
					dateFrom.setTimeZone(TimeZone.getTimeZone(eventTimeZone));
					dateTo.setTimeZone(TimeZone.getTimeZone(eventTimeZone));
				}
			}
		}
		else {
			TimeZone defaultTimeZone = TimeZone.getDefault();
			int defaultZoneOffset = defaultTimeZone.getRawOffset()/1000;
			String defaultTimeZoneValue = String.format("(GMT%+d:%02d) %s", defaultZoneOffset/3600, (defaultZoneOffset % 3600)/60, defaultTimeZone.getID());
			timeZones.setSelection(list.indexOf(defaultTimeZoneValue));
		}
		timeZones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
		    	String selectedValue = timeZones.getSelectedItem().toString();
				if(timezoneMap.containsKey(selectedValue)) {
					dateFrom.setTimeZone(TimeZone.getTimeZone(timezoneMap.get(selectedValue)));
					dateTo.setTimeZone(TimeZone.getTimeZone(timezoneMap.get(selectedValue)));
				}
		    } 

		    public void onNothingSelected(AdapterView<?> adapterView) {
		    }
		}); 
		
		
	}
	/**
	 * Returns the google calendar associated with the phone. If not one then returns the first.
	 * @return Calendar Name.
	 */
	private String getDefaultCalendar() {
		int index = 0;
		String defaultCalendar = null;
		for(com.honeydewit.pojos.Calendar calendar: calendarMap.values()) {
			if(index == 0) {
				defaultCalendar = calendar.getAccountName();
			}
			if(calendar.getAccountName().endsWith("gmail.com")) {
				defaultCalendar = calendar.getAccountName();
				break;
			}
			index++;
			
		}
		return defaultCalendar;
	}
	private void initialiseSaveButton() {
		saveBtn.setOnClickListener(new OneOffClickListener() {
			
			@Override
			public void onClick(View v) {
				
					saveEvent();
					
				
			}
		});
	}
	
	private void initAllDay() {

		allDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					toTimeBtn.setVisibility(View.INVISIBLE);
					fromTimeBtn.setVisibility(View.INVISIBLE);
				}
				else {
					toTimeBtn.setVisibility(View.VISIBLE);
					fromTimeBtn.setVisibility(View.VISIBLE);
				}
			}
		});

	}
	private void saveEvent()  {
		boolean isUpdate = false;
		Event event = null;
		if(eventInstance != null) {
			event = eventInstance.getEvent();
			isUpdate = true;
		}
		else {
			event = new Event();
		}
		event.setCalendarId(calendarMap.get(calendars.getSelectedItem()).getId());
		event.setEventTimezone(dateFrom.getTimeZone().getID());

		event.setTitle(getTitle().toString());

		event.setDateFrom(dateFrom);
		
		if(null != rrule) {
			event.setRrule(rrule);
			//event.setDuration(DurationConverter.convert(dateFrom, dateTo));
			event.setDateTo(null);
		}
		
		else {
			event.setDateTo(dateTo);
			event.setDuration(null);
			
		}

		if(allDay.isChecked()) {
			event.setDateFrom(DateUtil.getBeginningOfDay((GregorianCalendar)dateFrom));
			dateTo = dateFrom;
			dateTo.add(Calendar.DAY_OF_MONTH, 1);
			event.setDateTo(dateTo);
			event.setEventTimezone("UTC");
			event.setAllDay(true);
		}

		addRemindersToEvent(event);

		if(!eventValidator.validate(getBaseContext(), event)) {


			showErrors(eventValidator.getErrorMessages());
		}
		else {
			new SaveEventThread(event, isUpdate).doInBackground();
			HoneyDewDialogFragment dialog = new HoneyDewDialogFragment();
			dialog.show(getSupportFragmentManager(), "editOutside");

			boolean isExec = getSupportFragmentManager().executePendingTransactions();

			dialog.setTitle(getText(R.string.pleaseNote).toString());
			dialog.setMessage(getText(R.string.editEventOutsideMsg).toString());
			dialog.setPositiveButton(getText(R.string.ok).toString(), new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
					startActivity(intent);
					finish();
				}
			});
		}
	}

	private OnCancelListener repeatDialogCancelListener = new OnCancelListener() {
		
		@Override
		public void onCancel(DialogInterface dialog) {
			ignoreRepeatSelection = true;
			repeatChoices.setSelection(previousRepeat.intValue());
		}
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { 
		if(null != data && requestCode == CUSTOM_RECURRENCE_REQUEST_CDE) {
			rrule = (RecurrenceRule)data.getSerializableExtra("rrule");
		}
	}
	private class RepeatDialogDismissListener implements OnDismissListener {
		private RecurrenceRule rule;
		public RepeatDialogDismissListener(RecurrenceRule rule) {
			this.rule = rule;
		}
		@Override
		public void onDismiss(DialogInterface di) {
			RepeatDialog dialog = ((RepeatDialog)di);
			if(!dialog.isCancelClicked()) {
				previousRepeat = repeatChoices.getSelectedItemId();
				String string = dialog.getSelectedRepeat();
				chosenRepeat.setTag(string);
				if(DateUtil.isValidDate(string)) {
					chosenRepeat.setText("Until: " + string);
				}
				else {
					chosenRepeat.setText("Repeating: " + string + " times");
				}
				
				rrule = rule;
			}
			
		}

		
	}

	private void addRemindersToEvent(Event event) {
		List<Reminder> reminders = new ArrayList<Reminder>();
		for(int i = 0; i < reminderTable.getChildCount(); i++){
			TableRow row = (TableRow)reminderTable.getChildAt(i);
		    if(null != row.getTag() && row.getTag().toString().contains("reminderTimeRow")) {
		    	String repeatSpinnerValue = ((Spinner)row.findViewById(R.id.reminderSpinner)).getSelectedItem().toString();
		    	String reminderMethodType = ((Spinner)row.findViewById(R.id.reminderMethod)).getSelectedItem().toString();
		        Reminder reminder = new Reminder();
		        reminder.setEvent(event);
		        reminder.setMinutes(ReminderMinutesTypes.getMinutesByTitle(repeatSpinnerValue));
		        reminder.setMethod(ReminderMethodTypes.getCodeByTitle(reminderMethodType));
		        reminders.add(reminder);
		    }
		}
		event.setReminders(reminders);
	}
	private void initReminders() {
			if(null != eventInstance && null != eventInstance.getEvent()) {
				List<Reminder> reminders = getApplicationContext().getCalDbHelper().getReminders(getApplicationContext(), eventInstance.getEvent());
		    	for(Reminder reminder : reminders) {
		    		addReminder(reminder);
		    	}	
			}
	    	
	}
	public class SaveEventThread extends AsyncTask<String, Void, String> {
		Event event;
		boolean isUpdate;
		
		public SaveEventThread(Event event, boolean isUpdate) {
			this.event = event;
			this.isUpdate = isUpdate;
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				event = getApplicationContext().getCalDbHelper().addUpdateCalendarEvent(getApplicationContext(), event);
				getApplicationContext().getCalDbHelper().addUpdateReminders(getApplicationContext(), event.getEventId(), event.getReminders(), isUpdate);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
			

					ListEvent listEvent = getApplicationContext().getShoppingListDbHelper().saveListEvent(new ListEvent(event.getCalendarId(), event.getEventId(), list));
					if(null != list) {
						list.addEvent(getApplicationContext(),listEvent);
					}

			
			
			}
			setResult(Constants.RESULT_EVENT_SAVED);
			return null;
		}
	   

	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		HoneyDewDialogFragment dialog = new HoneyDewDialogFragment();
		dialog.show(getSupportFragmentManager(), "event");

		boolean isExec = getSupportFragmentManager().executePendingTransactions();

		dialog.setTitle(getText(R.string.saveTitle).toString());
		dialog.setMessage(getText(R.string.saveOnBack).toString());
		dialog.setPositiveButton(getText(R.string.save).toString(), new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				saveEvent();

			}
		});
		dialog.setNegativeButton(getText(R.string.dontSave).toString(), new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
	}
}
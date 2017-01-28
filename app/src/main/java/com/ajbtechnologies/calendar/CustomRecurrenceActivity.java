package com.ajbtechnologies.calendar;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.ajbtechnologies.BasicActivity;
import com.ajbtechnologies.R;
import com.ajbtechnologies.calendar.customviews.ExpandableHeightGridView;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

public class CustomRecurrenceActivity extends BasicActivity implements
android.view.View.OnClickListener, Serializable {

	private static final long serialVersionUID = 1L;
	
	protected Button yes, no;
	protected Spinner frequencies;
	protected GregorianCalendar eventDate;
	protected RecurrenceRule rule;
	protected EditText interval;
	protected TextView everyText;
	protected ExpandableHeightGridView gridView;
	protected RadioGroup radioGroup;
	protected RadioButton eachRadio;
	protected RadioButton onTheRadio;
	protected Spinner weeksSpinner;
	protected Spinner daysSpinner;
	private RecurrenceRule tempRule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.custom_repeat_dialog);

		radioGroup = (RadioGroup)findViewById(R.id.recurrenceRadioGroup);
		eachRadio = (RadioButton)radioGroup.findViewById(R.id.eachRadio);
		onTheRadio = (RadioButton)radioGroup.findViewById(R.id.onTheRadio);
		
		
		weeksSpinner = (Spinner)findViewById(R.id.weeksSpinner);
		daysSpinner = (Spinner)findViewById(R.id.daysSpinner);
		everyText = (TextView)findViewById(R.id.everyText);
		frequencies = (Spinner) findViewById(R.id.frequencies);
		gridView = (ExpandableHeightGridView) findViewById(R.id.frequencyGrid);
		gridView.setExpanded(true);
		
		interval = (EditText)findViewById(R.id.everyNum);
		yes = (Button) findViewById(R.id.btn_yes);
		no = (Button) findViewById(R.id.btn_no);

		yes.setOnClickListener(this);
		no.setOnClickListener(this);

		radioGroup.setOnCheckedChangeListener(onCheckedChangeListener) ;

		frequencies.setOnItemSelectedListener(frequencyItemSelectedListener);
		ArrayAdapter<CharSequence> frequencyAdapter = ArrayAdapter.createFromResource(this, R.array.frequencies_array, android.R.layout.simple_spinner_item);
		frequencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		frequencies.setAdapter(frequencyAdapter);

		ArrayAdapter<CharSequence> weeksAdapter = ArrayAdapter.createFromResource(this, R.array.weeks_array, android.R.layout.simple_spinner_item);
		weeksAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		weeksSpinner.setAdapter(weeksAdapter);

		ArrayAdapter<CharSequence> daysAdapter = ArrayAdapter.createFromResource(this, R.array.day_array, android.R.layout.simple_spinner_item);
		daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		daysSpinner.setAdapter(daysAdapter);

		if(rule == null) {
			rule = new RecurrenceRule();
			eachRadio.performClick();
			frequencies.setSelection(0);
			interval.setText("1");
		}		

	}
	OnItemSelectedListener frequencyItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> frequencyDropDown, View arg1, int arg2,
				long arg3) {
			try {
				if(frequencyDropDown.getSelectedItem().equals("Daily")) {
					everyText.setText(" day(s)");
					gridView.setVisibility(View.INVISIBLE);
					radioGroup.setVisibility(View.INVISIBLE);
				}
				else if(frequencyDropDown.getSelectedItem().equals("Weekly")) {
					everyText.setText(" week(s) on");
					gridView.setVisibility(View.VISIBLE);
					gridView.setNumColumns(7);
					gridView.setAdapter(new CustomRepeatDialogGridAdapter(getBaseContext(), frequencyDropDown.getSelectedItem().toString(), DateUtil.daysInWeek));
					radioGroup.setVisibility(View.INVISIBLE);
				}
				else if(frequencyDropDown.getSelectedItem().equals("Monthly")) {
					everyText.setText(" month(s)");
					gridView.setVisibility(View.VISIBLE);
					gridView.setNumColumns(7);
					gridView.setAdapter(new CustomRepeatDialogGridAdapter(getBaseContext(), frequencyDropDown.getSelectedItem().toString(),DateUtil.daysInMonth));
					radioGroup.setVisibility(View.VISIBLE);
					eachRadio.performClick();
				}
				else if(frequencyDropDown.getSelectedItem().equals("Yearly")) {
					everyText.setText(" year(s) in");
					gridView.setVisibility(View.VISIBLE);
					gridView.setNumColumns(5);
					gridView.setAdapter(new CustomRepeatDialogGridAdapter(getBaseContext(), frequencyDropDown.getSelectedItem().toString(),DateUtil.monthsInYear));
					radioGroup.setVisibility(View.VISIBLE);
					eachRadio.performClick();

				}

			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	};
	@Override
	public void onClick(View v) {


		if(v == yes) {
			if(validate()) {
				tempRule = convertToRule(eventDate);
				
				RepeatDialog dialog = new RepeatDialog(this, eventDate, tempRule);
				dialog.setOnDismissListener(repeatDialogDismissListener);
				dialog.setOnCancelListener(repeatDialogCancelListener);
				dialog.show();

			}

		}
		else if(v == no) {
			//cancel();
		}

	}
	protected Set<String> getSelectedFrequencies() {
		
		Set<String> selectedFrequencies = new HashSet<String>();
		for(int i = 0; i < gridView.getChildCount(); i++) {
			ViewGroup gridChild = (ViewGroup) gridView.getChildAt(i);
			for(int k = 0; k < gridChild.getChildCount(); k++) {
				if( gridChild.getChildAt(k) instanceof Button ) {
					if(gridChild.getChildAt(k).isSelected()) {
						selectedFrequencies.add(gridChild.getChildAt(k).getTag().toString());
					}
				}
			}
		}
		return selectedFrequencies;
	}
	
	private boolean validate() {
		return true;
	}
	private OnDismissListener repeatDialogDismissListener = new OnDismissListener() {

		@Override
		public void onDismiss(DialogInterface dialog) {
		
				tempRule = convertToRule((RepeatDialog)dialog, tempRule);
				Intent intent = new Intent();
				intent.putExtra("rrule", tempRule);
				setResult(RESULT_OK, intent);
				finish();
				dialog.dismiss();
			
			
		}
	};
	

	private OnCancelListener repeatDialogCancelListener = new OnCancelListener() {

		@Override
		public void onCancel(DialogInterface dialog) {
			dialog.cancel();
			//repeatChoices.setSelection(previousRepeat.intValue());
		}
	};
	private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {

			if(eachRadio.getId() == checkedId) {
				gridView.setVisibility(View.VISIBLE);
				weeksSpinner.setVisibility(View.INVISIBLE);
				daysSpinner.setVisibility(View.INVISIBLE);
			}
			if(onTheRadio.getId() == checkedId) {
				gridView.setVisibility(View.INVISIBLE);
				weeksSpinner.setVisibility(View.VISIBLE);
				daysSpinner.setVisibility(View.VISIBLE);
			}
		}
	};
	public RecurrenceRule convertToRule(GregorianCalendar dateStart)  {
		
		RecurrenceRule rule = new RecurrenceRule();
		String frequency = frequencies.getSelectedItem().toString();
		int radio = radioGroup.getCheckedRadioButtonId();
		rule.setFreq(frequency);
		rule.setInterval(Integer.parseInt(interval.getText().toString()));
		if(frequency.equalsIgnoreCase("WEEKLY")) {
			rule.setWeekStart("SU");
			
			rule.setByDay(getSelectedFrequencies());
		}
		else if(frequency.equalsIgnoreCase("MONTHLY")) {
			if(radio == eachRadio.getId()) {
				
				rule.setByMonthDay(getSelectedFrequencies());
				
			}
			if(radio == onTheRadio.getId()) {
				int numWeek = weeksSpinner.getSelectedItemPosition() == 5 ? -1 : weeksSpinner.getSelectedItemPosition();
				int day = daysSpinner.getSelectedItemPosition();
				if(day <=7) {
					String dayValue = daysSpinner.getSelectedItem().toString().substring(1,2).toUpperCase();
					Set<String> byDay = new HashSet<String>();
					byDay.add(numWeek + dayValue);
					rule.setByDay(byDay);
				}
				else {
					String dayValue = daysSpinner.getSelectedItem().toString();
					Set<String> byDay = null;
					if(dayValue.equals("Day")) {
						byDay = new HashSet<String>(Arrays.asList(DateUtil.daysInWeek));
					}
					else if(dayValue.equals("Weekday")) {
						byDay = new HashSet<String>(Arrays.asList(DateUtil.weekdays));
					}
					else if(dayValue.equals("Weekend Day")) {
						byDay = new HashSet<String>(Arrays.asList(DateUtil.weekendDays));
					}
					rule.setByDay(byDay);
					rule.setBySetPosition(numWeek);
				}	
			}
		}
		else if(frequency.equalsIgnoreCase("YEARLY")) {
			if(radio == eachRadio.getId()) {
				rule.setByMonth(getSelectedFrequencies());
				Set<String> byDay = new HashSet<String>();
				byDay.add(String.valueOf(dateStart.get(Calendar.DAY_OF_MONTH)));
				
			}
		}
		return rule;
		
	}
	private RecurrenceRule convertToRule(RepeatDialog dialog, RecurrenceRule tempRule) {
		if(dialog.repeatRepRadio.isChecked()) {
			tempRule.setCount(Integer.valueOf(dialog.repeatRepEdit.getText().toString()));
		}
		else if(dialog.repeatUntilRadio.isChecked()) {
			tempRule.setUntil(DateUtil.convertDateToRRULE(dialog.repeatUntilDate.toString()));
		}
		return tempRule;
	}
	
}
package com.ajbtechnologies.calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.ajbtechnologies.BasicActivity;
import com.ajbtechnologies.R;
import com.ajbtechnologies.utils.StringUtils;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class RepeatDialog extends Dialog implements
    android.view.View.OnClickListener {
  private boolean cancelClicked = false;
  public boolean isCancelClicked() {
	return cancelClicked;
}

public void setCancelClicked(boolean cancelClicked) {
	this.cancelClicked = cancelClicked;
}
private static final String REPEAT = "Repeat";
  public BasicActivity activity;
  public Dialog d;
  public Button yes, no;
  public RadioButton repeatRepRadio, repeatUntilRadio;
  public TableRow repeatRepRow, repeatUntilRow;
  public TextView repeatUntilDate;
  public EditText repeatRepEdit;
  public Calendar eventDate;
  public View previousSelectedRadio;
  public String previousSelectedValue;
  private RecurrenceRule rule;

  public RepeatDialog(BasicActivity activity, Calendar eventDate, RecurrenceRule rule) {
    super(activity);
    // TODO Auto-generated constructor stub
    this.activity = activity;
    this.eventDate = eventDate;
    this.rule = rule;
  }
 
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.repeat_dialog);
    setTitle(REPEAT);
    repeatRepEdit = (EditText)findViewById(R.id.repeatRepEdit);
    repeatUntilDate = (TextView)findViewById(R.id.repeatUntilDate);
    repeatRepEdit.setOnClickListener(this);
    repeatUntilDate.setOnClickListener(this);
    repeatRepRow = (TableRow)findViewById(R.id.repeatRepRow);
    repeatUntilRow = (TableRow) findViewById(R.id.repeatUntilRow);
    repeatRepRow.setOnClickListener(this);
    repeatUntilRow.setOnClickListener(this);
    repeatRepRadio = (RadioButton)findViewById(R.id.repeatRepRadio);
    repeatUntilRadio = (RadioButton) findViewById(R.id.repeatUntilRadio);
    repeatRepRadio.setOnClickListener(this);
    repeatUntilRadio.setOnClickListener(this);
    yes = (Button) findViewById(R.id.btn_yes);
    no = (Button) findViewById(R.id.btn_no);
    yes.setOnClickListener(this);
    no.setOnClickListener(this);
    if(null != rule && rule.getUntil() == null && rule.getCount() == null) {
    	repeatRepEdit.performClick();
    	repeatRepEdit.setText("1");
    }

  }
  
  @Override
  public void onClick(View v) {
	
    	if(v == yes) {
    		if(validate()) {
    			if(repeatRepRadio.isChecked()) {
    				rule.setCount(Integer.parseInt(repeatRepEdit.getText().toString()));
    				rule.setUntil(null);
    			}
    			else {
    				rule.setCount(null);
    				rule.setUntil(DateUtil.convertDateToRRULE(repeatUntilDate.getText().toString()));
    			}
    			
    			dismiss();
    		}
    			
        }
        else if(v == no) {
        	cancelClicked = true;
            cancel();
        }
        else if(v == repeatRepEdit || v == repeatRepRadio || v == repeatRepRow) {

        	 repeatUntilRadio.setChecked(false);
             repeatRepRadio.setChecked(true);
             repeatRepEdit.setEnabled(true);
             repeatRepEdit.setFocusable(true);
             previousSelectedRadio = repeatRepRadio;
             
        }	
        else if(v == repeatUntilDate || v == repeatUntilRadio || v == repeatUntilRow) {    	
            repeatRepRadio.setChecked(false);
            repeatUntilRadio.setChecked(true);
            repeatRepEdit.setEnabled(false);
            DatePickerDialog dialog = null;
            if(!StringUtils.isEmpty(repeatUntilDate.getText().toString())) {
            	GregorianCalendar cal = DateUtil.getCalendarFromString(repeatUntilDate.getText().toString());
            	dialog = new DatePickerDialog(activity, datePickerListener,  cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
            }
            else {
            	 dialog = new DatePickerDialog(activity, datePickerListener,  eventDate.get(Calendar.YEAR), eventDate.get(Calendar.MONTH),eventDate.get(Calendar.DAY_OF_MONTH));
            }
            
        	dialog.setOnDismissListener(datePickerDismissListener);
        	
        	dialog.show();
        	
        }
     
    
  }
  private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			GregorianCalendar newCal = new GregorianCalendar();
			newCal.set(Calendar.MONTH, selectedMonth);
			newCal.set(Calendar.DAY_OF_MONTH, selectedDay);
			newCal.set(Calendar.YEAR, selectedYear);
			repeatUntilDate.setText(DateUtil.monthDayYear.format(newCal.getTime()));
		}

	};
	
	private DialogInterface.OnDismissListener datePickerDismissListener =
			new DialogInterface.OnDismissListener() {
		public void onDismiss(DialogInterface dialog) {
			
		}
	};
	private boolean validate() {
		 if(repeatRepRadio.isChecked()) {
			try {
				Integer.valueOf(repeatRepEdit.getText().toString());
			}
			catch(NumberFormatException ex) {
				activity.showError(R.string.selectionContinueMessage);
				
				return false;
				
			}
			return true;
		 }
		 else if(repeatUntilRadio.isChecked()) {
			 return DateUtil.isValidDate(repeatUntilDate.getText().toString());
		 }
		 activity.showError(R.string.selectionContinueMessage);
		 return false;
	}

	public String getSelectedRepeat() {

		if (repeatRepRadio.isChecked()) {
			return repeatRepEdit.getText().toString();
		}
		return repeatUntilDate.getText().toString();

	}
}
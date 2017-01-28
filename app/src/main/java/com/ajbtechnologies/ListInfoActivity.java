package com.ajbtechnologies;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ajbtechnologies.calendar.CalendarActivity;
import com.ajbtechnologies.pojos.BasicList;
import com.ajbtechnologies.validate.ListValidator;

import java.util.concurrent.Callable;


public class ListInfoActivity extends OptionsMenuActivity implements OnClickListener {


	Button reminderBtn;
	Button saveButton;
	Button cancelBtn;
	Button reminderButton;
	EditText name;
	BasicList list;
	
	ListValidator validator = new ListValidator();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_info);
		if(null == list) {
			initialiseList();
		}
		reminderBtn = (Button)findViewById(R.id.reminderButton);
		reminderBtn.setOnClickListener(this);
		name = (EditText) findViewById(R.id.listName);
		name.setText(list.getListName());
		
		name.addTextChangedListener(new TitleTextWatcher(this));
		if(list.getEvents() != null && list.getEvents().size() > 0) {
			reminderBtn.setText(R.string.editReminder);
		}
		
		saveButton = (Button)findViewById(R.id.saveBtn);
		saveButton.setOnClickListener(this);
		
		cancelBtn = (Button)findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(this);

	}
	private void initialiseList() {
		list = getApplicationContext().getCurrentList();
		
		if(null == list) {
			list = new BasicList();				
		}
		if(list.getListTypeId() == 0) {
			int listTypeCode = getIntent().getIntExtra(Constants.LIST_TYPE, Constants.TODO_LIST_TYPE_CDE);
			list.setListTypeId(listTypeCode);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		list = getApplicationContext().getCurrentList();
		boolean isEventExists = list.getEvents() != null && list.getEvents().size() > 0;
		if(isEventExists) {
			reminderBtn.setText(R.string.editReminder);
		}
	}
	public Integer save() {
		list.setListName(name.getText().toString());
		if(!validator.validate(getApplicationContext(), list)) {
			showErrors(validator.getErrorMessages());
		}
		else {
			saveBasicList(null != list.get_id());
			
			Intent listHomeIntent = new Intent(getBaseContext(), ListHomeActivity.class);
			listHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(listHomeIntent);	
			finish();
		}	
		return 1;
	}
	@Override
	public void onClick(View v) {
		if(saveButton == v) {
			save();
		}
		else if(cancelBtn == v) {
			showCancelMessageDialog(R.string.confirmCancel,null);
		}

		else if(reminderBtn == v) {

			list.setListName(name.getText().toString());

			if(validator.validate(getApplicationContext(), list)) {
				showErrors(validator.getErrorMessages());
				
			}
			else {					
				saveBasicList(null != list.get_id());
				Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
				startActivity(intent);
				finish();

			}
		
		}

	}
	private void saveBasicList(boolean isUpdate) {
		getApplicationContext().getShoppingListDbHelper().addUpdateShoppingList(list);
		getApplicationContext().setCurrentList(list);
	}
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.action_bar_save_item_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
    	int itemId = item.getItemId();
    	
       if(itemId == R.id.optionSaveButton) {
    	   save();
           return true;
       }
       else if(itemId ==  R.id.optionCancelButton) {
            	showCancelMessageDialog(R.string.confirmCancel,null);
                return true;
       } else {
    	   return super.onOptionsItemSelected(item);
        }
    }
    @Override
	public void onBackPressed() {
		
		showSaveOnBackButtonDialog(this,new Callable<Integer>() {
			 public Integer call() {
			        return save();
			   }
		});
	}
	private void addEvent(BasicList list) {
		Intent calEventIntent = new Intent(Intent.ACTION_EDIT);
		calEventIntent.setType("vnd.android.cursor.item/event");
		//calEventIntent.putExtra("calendar_id", m_selectedCalendarId);  //this doesn't work
		calEventIntent.putExtra("title", list.getListName() );
		calEventIntent.putExtra("description", getText(R.string.app_name) + " List Reminder for list name " + list.getListName() );

		//calEventIntent.putExtra("eventLocation", list.getStore_id());
		calEventIntent.putExtra("beginTime", System.currentTimeMillis());
		calEventIntent.putExtra("endTime", System.currentTimeMillis() + 1800 * 1000);
		calEventIntent.putExtra("allDay", 0);
		//status: 0~ tentative; 1~ confirmed; 2~ canceled
		calEventIntent.putExtra("eventStatus", 1);
		//0~ default; 1~ confidential; 2~ private; 3~ public
		calEventIntent.putExtra("visibility",1);
		//0~ opaque, no timing conflict is allowed; 1~ transparency, allow overlap of scheduling
		calEventIntent.putExtra("transparency", 0);
		//0~ false; 1~ true
		calEventIntent.putExtra("hasAlarm", 1);
		try {
			startActivity(calEventIntent);
		} catch (Exception e) {
			Toast.makeText(this.getApplicationContext(), "Sorry, no compatible calendar is found!", Toast.LENGTH_LONG).show();
		}
	}
}

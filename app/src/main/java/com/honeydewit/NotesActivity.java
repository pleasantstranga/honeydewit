package com.honeydewit;


import android.database.SQLException;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.honeydewit.listeners.OneOffClickListener;
import com.honeydewit.pojos.ListItem;
import com.honeydewit.validate.NoteValidator;

import java.util.concurrent.Callable;


public class NotesActivity extends OptionsMenuActivity {
		
	Button saveButton;
	Button cancelButton;
	EditText note;
	ListItem listItem;
	NoteValidator noteValidator;

	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.note);
	        noteValidator = new NoteValidator();
	       
	        
	        note = (EditText)findViewById(R.id.note);
	        
	        initialiseListItem();

	        
	        saveButton = (Button) findViewById(R.id.saveBtn);
	        saveButton.setOnClickListener(new OneOffClickListener() {
				
				@Override
				public void onClick(View v) {
					
					save();
				}

				
			});
	        cancelButton = (Button)findViewById(R.id.cancelBtn);
	        cancelButton.setOnClickListener(new OneOffClickListener(){
				
				@Override
				public void onClick(View v) {
					showCancelMessageDialog(R.string.confirmCancel,null);
					
				}
			});
	             
	 }
	private void initialiseListItem() {

			ListItem obj = null;
			if(null == obj && null != getIntent().getExtras() && getIntent().getExtras().containsKey("itemId")) { 
				int id = getIntent().getIntExtra("itemId", -1);
				obj = getApplicationContext().getShoppingListDbHelper().getListItemById(id);
			}
			if(null != obj) {
				listItem = obj;
			    note.setText(listItem.getName());				
			}
			else {
				listItem = new ListItem(); 
				listItem.setList(getApplicationContext().getCurrentList());
			}
		
		
	}
	@Override
	public void onBackPressed() {
		
		showSaveOnBackButtonDialog(new Callable<Integer>() {
			 public Integer call() {
				
			        return save();
			        
			   }
		},listItem.getRowNumber());
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
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
		else if(itemId == R.id.optionCancelButton) {
            	showCancelMessageDialog(R.string.confirmCancel,null);
                return true;
        }
		else {
                return super.onOptionsItemSelected(item);
        }
    }
	private Integer save() {
		listItem.setName(note.getText().toString());
		

		
		if(noteValidator.validate(getBaseContext(), listItem)) {

			boolean isUpdate = listItem.get_id() != null;

			int listId =getApplicationContext().getShoppingListDbHelper().addUpdateListItem(listItem);

			getApplicationContext().setCurrentItem(listItem);
			getIntent().putExtra("isUpdate", isUpdate);
			setResult(RESULT_OK, getIntent());


			finish();
		    					
		}
		else {
			showErrors(noteValidator.getErrorMessages());
		}
		return 1;
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
		super.onSaveInstanceState(outState, outPersistentState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

	}
}

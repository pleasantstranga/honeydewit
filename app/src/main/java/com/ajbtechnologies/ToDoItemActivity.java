package com.ajbtechnologies;

import android.database.SQLException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ajbtechnologies.converter.InvalidListItem;
import com.ajbtechnologies.listeners.OneOffClickListener;
import com.ajbtechnologies.pojos.ListItem;
import com.ajbtechnologies.validate.ToDoItemValidator;

import java.util.concurrent.Callable;


public class ToDoItemActivity extends OptionsMenuActivity {

	private EditText itemName;
	private ListItem listItem;
	private EditText description;
	private ToDoItemValidator itemValidator;
	boolean isFromImport = false;

	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.todoitem);
	        
	        itemValidator = new ToDoItemValidator();
	       
	        
	        itemName = (EditText)findViewById(R.id.itemName);
	        description = (EditText)findViewById(R.id.description);
	        
	        initialiseListItem();
	        itemName.addTextChangedListener(new TitleTextWatcher(this));
		Button saveButton = (Button) findViewById(R.id.saveBtn);
	        saveButton.setOnClickListener(new OneOffClickListener() {

				@Override
				public void onClick(View v) {

					save();
				}
			});
		Button cancelButton = (Button) findViewById(R.id.cancelBtn);
	        cancelButton.setOnClickListener(new OneOffClickListener() {
				@Override
				public void onClick(View v) {
					showCancelMessageDialog( R.string.confirmCancel,null);
				}
			});
	       
	
	             
	 }
	
	private void setProperties() {
		
		listItem.setName(itemName.getText().toString());
		listItem.setDescription(description.getText().toString());
	}
	private void initialiseListItem() {
			
			listItem = handleInvalidImport();
			if(null == listItem && null != getIntent().getExtras() && getIntent().getExtras().containsKey("itemId")) { 
				int id = getIntent().getIntExtra("itemId", -1);
				listItem =  getApplicationContext().getShoppingListDbHelper().getListItemById(id) ;
			
			}
			if(null != listItem) {
			    itemName.setText(listItem.getName());
			    description.setText(listItem.getDescription());  	
			}
			else {
				listItem = new ListItem(); 
				listItem.setList(getApplicationContext().getCurrentList());
			}
		
		
	}
	private ListItem handleInvalidImport() {
		ListItem invalidImportedListItem = null;
		if(null != getIntent().getParcelableExtra("invalidRow")) {
			InvalidListItem row = getIntent().getParcelableExtra("invalidRow");
			getIntent().removeExtra("invalidRow");
			showErrors(row.getErrorMessages());
			invalidImportedListItem = row.getListItem();
			isFromImport = true;
		}
		return invalidImportedListItem;
	}
	@Override
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

	private Integer save() {
		setProperties();
		

		
		if(itemValidator.validate(getBaseContext(), listItem)) {
			 
			 try {
				 getApplicationContext().getShoppingListDbHelper().addUpdateListItem(listItem);
			 }
			 catch(SQLException e) {
				e.printStackTrace(); 
			 }
			 finally {
				 
				 finish();
			 }
		    					
		}
		else {
			showErrors(itemValidator.getErrorMessages());
		}
		return 1;
	}
	@Override
	public void onBackPressed() {
		showSaveOnBackButtonDialog(new Callable<Integer>() {
			 public Integer call() {
			        return save();
			   }
		},listItem.getRowNumber());
	}
}

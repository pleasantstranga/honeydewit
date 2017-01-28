package com.ajbtechnologies;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ajbtechnologies.adapters.CheckListAdapter;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class CheckBoxListActivity extends OptionsMenuActivity implements OnClickListener {
	

	private CheckListAdapter listAdapter;
	private Button cancelBtn;
	private Button importButton;
	private CompoundButton checkbox;
    private ArrayList<ImportedList> items;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.import_lists_home);
		((TextView)findViewById(R.id.headerTxt)).setText("Select the lists to import.");

		//noinspection unchecked
		items = (ArrayList<ImportedList>)getIntent().getSerializableExtra("importedListSheets");
		cancelBtn = (Button)findViewById(R.id.cancelBtn);
		cancelBtn.setOnClickListener(this);
		importButton = (Button)findViewById(R.id.importBtn);
		importButton.setOnClickListener(this);
		
		ListView listView = (ListView)findViewById(R.id.importList);
		listAdapter = new CheckListAdapter(this, R.layout.checkboxlistrow,items);
		listView.setAdapter(listAdapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	   // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.action_bar_import_lists_menu, menu);
	    return super.onCreateOptionsMenu(menu);
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        if(item.getItemId() == R.id.optionSaveButton) {
        	 if(!listAdapter.isRowChecked()) {
				 Toast.makeText(this, "Please select a list to import", Toast.LENGTH_LONG).show();
			 }
			 else {
                 getIntent().putExtra("importedListSheets",items);
                 setResult(RESULT_OK,getIntent());
                 finish();

             }
        }
        else if(item.getItemId() == R.id.optionCancelButton) {
            showCancelMessageDialog(R.string.confirmCancel,null);
        }
        return true;
    }
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if(resultCode == RESULT_CANCELED) {
            if(requestCode == Constants.LIST_NAME_CHOICE_REQ_CODE) {
                if(data.hasExtra("isUnCheckParentCheckbox")) {
                    boolean isUncheck = data.getBooleanExtra("isUnCheckParentCheckbox",true);
                    this.checkbox.setChecked(!isUncheck);
                }
            }
		}
        else if(requestCode == Constants.LIST_NAME_CHOICE_REQ_CODE) {
              ImportedList listInfo = (ImportedList)data.getSerializableExtra("listInfo");
              for(ImportedList list : items) {
                  if(list.getCurrentName().equals(listInfo.getCurrentName())) {
                      if(listInfo.getNewName() != null) {
                          list.setNewName(listInfo.getNewName());
                      }
                  }
              }

        }
		super.onActivityResult(requestCode, resultCode, data);
		listAdapter.notifyDataSetChanged();
	
	}
	public void showImportChoicesDialog(ImportedList listInfo, boolean isEmpty,boolean isExists, boolean isUnCheckParentOnExit, CompoundButton checkbox) {

		this.checkbox = checkbox;
		Intent intent = new Intent(this, ImportedListNameChoiceActivity.class);
		intent.putExtra("isExists", isExists);
		intent.putExtra("isEmpty", isEmpty);
		intent.putExtra("sheetInfo", listInfo);
        intent.putExtra("isUnCheckParentCheckbox", isUnCheckParentOnExit);
		startActivityForResult(intent,Constants.LIST_NAME_CHOICE_REQ_CODE);
	
	}
	@Override
	public void onClick(View v) {
		if(v == importButton) {
			 
			 if(!listAdapter.isRowChecked()) {
				 Toast.makeText(this, "Please select a list to import", Toast.LENGTH_LONG).show();
			 }
			 else {
                 getIntent().putExtra("importedListSheets",items);
				 setResult(RESULT_OK,getIntent());
				 finish(); 
			 }
		}
		else if(v == cancelBtn)
			showCancelMessageDialog();
		 }
	@Override
	public void onBackPressed() {
        showCancelMessageDialog();
		
		
	}
	public void showCancelMessageDialog() {
		final DialogFragment dialog = new DialogFragment();
		dialog.show(getSupportFragmentManager(), "cancel");
		boolean isExec = getSupportFragmentManager().executePendingTransactions();

		String string = getText(R.string.confirmCancel).toString();
		dialog.setTitle(getText(R.string.cancel).toString());
		dialog.setMessage(string);
		dialog.setPositiveButton("Cancel", new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				setResult(RESULT_CANCELED);
				dialog.dismiss();
				finish();
			}
		});
		dialog.setNegativeButton("Continue", new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				dialog.dismiss();
			}
		});

	}

}
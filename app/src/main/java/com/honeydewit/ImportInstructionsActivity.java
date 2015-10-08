package com.honeydewit;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableRow;
import android.widget.TextView;

import com.honeydewit.listeners.OneOffClickListener;

public class ImportInstructionsActivity extends OptionsMenuActivity  {
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.importing);
			setTitle(R.string.importInstructions);

			//TextView headerText = (TextView)findViewById(R.id.headerTxt);
			TextView importMessage =  (TextView)findViewById(R.id.importMessage);
			//headerText.setText(getResources().getText(R.string.importing));
			importMessage.setText(importMessage.getText().toString().replace(Constants.STRING_REPLACE_CHAR, Constants.SPLIT));
			
			TableRow checkboxRow = (TableRow)findViewById(R.id.checkBoxRow);
			checkboxRow.setVisibility(View.GONE);
			
			Button okButton = (Button) findViewById(R.id.ok);
			// if button is clicked, close the custom dialog
			okButton.setOnClickListener(new OneOffClickListener() {
				@Override
				public void onClick(View v) {
					CheckBox hideDialog = (CheckBox)findViewById(R.id.hideImporting);
					getApplicationContext().saveDialogVisibilityPreferences(Constants.HIDE_IMPORTING_MESSAGE, hideDialog.isChecked());
					onBackPressed();
				}
			});
	    }

	 @Override
	    public void onBackPressed() {
	    	// TODO Auto-generated method stub
		 getApplicationContext().setCurrentList(null);
		 Intent intent = new Intent(this, CreateEditListsActivity.class);
		 startActivity(intent);
		 super.onBackPressed();
	    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_bar_back, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		int itemId = item.getItemId();

		if(itemId == R.id.back) {
			getApplicationContext().setCurrentList(null);
			finish();
		}
		return true;
	}
}

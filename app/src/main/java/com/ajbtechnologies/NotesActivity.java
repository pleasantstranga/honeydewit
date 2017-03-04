package com.ajbtechnologies;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ajbtechnologies.pojos.ListItem;
import com.ajbtechnologies.validate.BaseImportValidator;
import com.ajbtechnologies.validate.BaseValidator;
import com.ajbtechnologies.validate.NoteValidator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


public class NotesActivity extends OptionsMenuActivity implements OnClickListener {
	private Button saveButton;
	private Button cancelButton;
	private EditText itemName;
	private ListItem listItem;
	private EditText description;
	private BaseValidator noteValidator;
	private ListView errorListView;
	private ArrayAdapter<String> errorsAdapter = null;
	private List<String> errors = new ArrayList<>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note);

		itemName = (EditText) findViewById(R.id.itemName);
		description = (EditText) findViewById(R.id.description);
		saveButton = (Button) findViewById(R.id.saveBtn);
		saveButton.setOnClickListener(this);
		cancelButton = (Button) findViewById(R.id.cancelBtn);
		cancelButton.setOnClickListener(this);
		noteValidator = new NoteValidator();
		itemName.addTextChangedListener(new TitleTextWatcher(this));
		errorListView = (ListView) findViewById(R.id.errorList);
	}

	@Override
	public void onClick(View v) {
		if (v == saveButton) {
			save();
		} else if (v == cancelButton) {
			showCancelMessageDialog(R.string.areYouSure, listItem.getRowNumber());
		}
	}

	private void save() {
		populateListItem();

		if (noteValidator.validate(this, listItem)) {
            boolean isUpdate = listItem.isUpdate();

			int listId = getApplicationContext().getShoppingListDbHelper().addUpdateListItem(listItem);

			if (isUpdate) {
				getApplicationContext().getShoppingListDbHelper().deleteImportErrors(listItem);
			}

			getApplicationContext().setCurrentItem(listItem);
			getIntent().putExtra(Constants.IS_UPDATE, isUpdate);
			setResult(RESULT_OK, getIntent());


			finish();
		} else {
			errors = noteValidator.getErrorMessages();
			if (errorsAdapter == null) {
				errorsAdapter = new ArrayAdapter<>(this, R.layout.error_layout, errors);
				errorListView.setAdapter(errorsAdapter);
			}
			else {

				errorsAdapter.notifyDataSetChanged();
			}

		}
	}

	private void populateListItem() {
		listItem.setList(getApplicationContext().getCurrentList());
		listItem.setName(itemName.getText().toString());
		listItem.setDescription(description.getText().toString());
	}

	private void initForm() {

		itemName.setText(listItem.getName());
		description.setText(listItem.getDescription());

		try {
			if (listItem.getErrors() != null && listItem.getErrors().size() > 0) {

				errorListView.setVisibility(View.VISIBLE);
				findViewById(R.id.rowTableRow).setVisibility(View.VISIBLE);
				((TextView) findViewById(R.id.rowNumber)).setText(String.format(listItem.getImportRow().toString()));

				ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
						R.layout.error_layout, listItem.getErrorsAsStrings());

				errorListView.setAdapter(adapter);
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
			Toast.makeText(this, "There was an error with your request. Please try again.", Toast.LENGTH_LONG).show();
		}


	}

	private void initialiseListItem() {
		if (null == listItem) {
			if (null != getIntent().getExtras() && getIntent().getExtras().containsKey("itemId")) {
				int id = getIntent().getIntExtra("itemId", -1);
				listItem = getApplicationContext().getCurrentList().getItemById(id);
			} else {
				listItem = new ListItem();
				listItem.setQuantity(1D);
				listItem.setList(getApplicationContext().getCurrentList());
			}
		}
	}

	@Override
	public void onBackPressed() {
		showSaveOnBackButtonDialog(new Callable<Integer>() {
			public Integer call() {
				try {
					save();
					return 1;
				} catch (Exception e) {
					e.printStackTrace();
					return -1;
				}

			}
		}, listItem.getRowNumber());
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
		super.onOptionsItemSelected(item);
		// Handle presses on the action bar items
		int itemId = item.getItemId();

		if(itemId == R.id.optionSaveButton) {
			save();
			return true;
		}
		else if(itemId == R.id.optionCancelButton) {

			showCancelMessageDialog(R.string.areYouSure, listItem.getRowNumber());
			return true;
		}
		return false;
	}


	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		populateListItem();
		outState.putSerializable("listItem", listItem);
		outState.putSerializable(Constants.CALC_TYPE, getIntent().getStringExtra(Constants.CALC_TYPE));

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onRestoreInstanceState(savedInstanceState);
		listItem = (ListItem) savedInstanceState.get("listItem");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initialiseListItem();
		initForm();
	}

	public void showErrors(BaseImportValidator validator) {
		final DialogFragment dialog = new DialogFragment();
		dialog.show(getSupportFragmentManager(), "errors");

		boolean isExec = getSupportFragmentManager().executePendingTransactions();

		StringBuilder string = new StringBuilder();
		for (String errorMessage : validator.getErrorMessages().get(listItem.getList().getListName())) {
			string.append("* ").append(errorMessage).append("\n");
		}
		dialog.setTitle(getText(R.string.error).toString());
		dialog.setMessage(string.toString());
		dialog.setNeutralButton("Ok", new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				dialog.dismiss();

			}
		});
		validator.getErrorMessages().clear();

	}
}

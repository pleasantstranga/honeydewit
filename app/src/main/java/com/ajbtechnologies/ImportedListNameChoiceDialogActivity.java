package com.ajbtechnologies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ajbtechnologies.validate.ImportedListNameValidator;

public class ImportedListNameChoiceDialogActivity extends BasicActivity implements android.view.View.OnClickListener {

	private RadioButton renameListBtn;
	private RadioButton replaceListBtn;
	private EditText fileNameInput;
	private Button okButton;
	private Button cancelButton;
	private ImportedListNameValidator validator;
	private ImportedList listInfo;
	private boolean isRename = false;
	private Activity context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.duplicate_list);
		context = this;
		validator = new ImportedListNameValidator();
		listInfo = (ImportedList)getIntent().getSerializableExtra("sheetInfo");
		boolean sheetExists = getIntent().getBooleanExtra("isExists", false);
		TextView message = (TextView) findViewById(R.id.message);
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.duplicateListRadioGroup);
		renameListBtn = (RadioButton)findViewById(R.id.renameRadio);
		replaceListBtn = (RadioButton)findViewById(R.id.replaceRadio);
		fileNameInput = (EditText)findViewById(R.id.listNameInput);
		okButton = (Button)findViewById(R.id.okBtn);
		okButton.setOnClickListener(this);
		cancelButton = (Button)findViewById(R.id.cancelBtn);
		cancelButton.setOnClickListener(this);
		replaceListBtn.setOnClickListener(this);
		renameListBtn.setOnClickListener(this);
				

		if(sheetExists) {
            setTitle("Please choose");
			String listsExistsMessage = getText(R.string.listExistsMessage).toString();
			listsExistsMessage = listsExistsMessage.replace("^", listInfo.getCurrentName());
			message.setText(listsExistsMessage);
			radioGroup.setVisibility(View.VISIBLE);
			replaceListBtn.setChecked(true);
			isRename = false;
            if(null != listInfo.getNewName() && listInfo.getNewName().length() > 0) {
                fileNameInput.setText(listInfo.getNewName());
            }
            else {
                fileNameInput.setText(listInfo.getCurrentName());
            }
		}
        else {
            setTitle("Please enter a name.");
            radioGroup.setVisibility(View.GONE);
            fileNameInput.setVisibility(View.VISIBLE);
            isRename = true;
            if(null != listInfo.getNewName() && listInfo.getNewName().length() > 0) {
                fileNameInput.setText(listInfo.getNewName());
            }
            else {
                fileNameInput.setText(listInfo.getCurrentName());
            }
        }
		okButton.setText("OK");
		cancelButton.setText("Cancel");

	}

	@Override
	public void onClick(View v) {
		if(v == okButton) {
			if(isRename) {
				
				if(validator.validate(this, fileNameInput.getText().toString()))  {
					if(listInfo.isFromExcelFile()) {
						listInfo.setRename(true);
						listInfo.setReplace(false);
						listInfo.setNewName(fileNameInput.getText().toString());
						getIntent().putExtra(Constants.LIST_INFO, listInfo);
						setResult(RESULT_OK, getIntent());
						finish();
					}
					else {
						listInfo.setRename(true);
						listInfo.setReplace(false);
						listInfo.setNewName(fileNameInput.getText().toString());
						getIntent().putExtra(Constants.LIST_INFO, listInfo);
						setResult(RESULT_OK, getIntent());
						finish();
					}
					
					
				}
				else {
					showErrors(validator.getErrorMessages());
				}
			}
			else {
				listInfo.setRename(false);
				listInfo.setReplace(true);
				Intent intent = new Intent();
				intent.putExtra(Constants.LIST_INFO, listInfo);
				
				setResult(RESULT_OK, intent);
				finish();
			}
			
		}
		else if(v == renameListBtn) {
			isRename = true;
			fileNameInput.setVisibility(View.VISIBLE);
		}
		else if(v == replaceListBtn) {
			isRename = false;
			listInfo.setNewName("");	
			fileNameInput.setVisibility(View.INVISIBLE);
		}
		else if(v == cancelButton) {

            setResult(RESULT_CANCELED, getIntent());
            finish();
		}
	}
	@Override
	public void onBackPressed() {
		showCancelMessageAlertDialog(R.string.cancel, R.string.cancelImportMessage);
		
	}


}
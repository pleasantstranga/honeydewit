package com.ajbtechnologies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ajbtechnologies.validate.ListNameValidator;

public class SheetNameInputDialogActivity extends BasicActivity implements android.view.View.OnClickListener {
	
    private TextView error;
	private Button okButton;
	private Button cancelButton;
	private ImportedList importedList;
	private EditText userInput;
    private ListNameValidator validator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prompt_single_input);
		setTitle("Enter a new name for this item:");
        validator = new ListNameValidator();
        error = (TextView)findViewById(R.id.error);
        importedList = (ImportedList)getIntent().getSerializableExtra("importedList");
		userInput = (EditText)findViewById(R.id.user_input);
        okButton = (Button)findViewById(R.id.okBtn);
		cancelButton = (Button)findViewById(R.id.cancelBtn);
		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);	

        if(importedList.getNewName() != null) {
            userInput.setText(importedList.getNewName());
        }
        else {
            userInput.setText(importedList.getCurrentName());
        }

	}
	@Override
	public void onClick(View v) {
		
		if(v == okButton) {
            if(validator.validate(this,userInput.getText())) {
                error.setVisibility(View.GONE);
                importedList.setNewName(userInput.getText().toString());

                setResult(RESULT_OK, getIntent());
                finish();
            }
            else {
               error.setVisibility(View.VISIBLE);
               error.setText(validator.getErrorMessages().get(0));
            }
		}
		else if(v == cancelButton) {
			Intent intent = getIntent();
			setResult(RESULT_CANCELED, intent);
			finish();
		}
	}
}
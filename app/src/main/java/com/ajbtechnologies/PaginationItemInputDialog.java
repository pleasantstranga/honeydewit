package com.ajbtechnologies;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PaginationItemInputDialog extends Dialog implements android.view.View.OnClickListener {
	

	private Button okButton;
	private Button cancelButton;
	private ImportedList item;
	private EditText userInput;
	
	public EditText getUserInput() {
		return userInput;
	}

	public void setUserInput(EditText userInput) {
		this.userInput = userInput;
	}

	public PaginationItemInputDialog(Context context, ImportedList item) {
		super(context);
		this.item = item;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prompt_single_input);
		setTitle("Enter a new name for this item:");
		
		userInput = (EditText)findViewById(R.id.user_input);
		okButton = (Button)findViewById(R.id.okBtn);
		cancelButton = (Button)findViewById(R.id.cancelBtn);
		okButton.setOnClickListener(this);
		cancelButton.setOnClickListener(this);	
		
		if(item.getNewName() != null) {
			userInput.setText(item.getNewName());
		}
		else {
			userInput.setText(item.getCurrentName());
		}
	}
	@Override
	public void onClick(View v) {
		
		if(v == okButton) {
			if(userInput.getText() != null && !userInput.getText().toString().equals(item.getCurrentName())) {
				item.setNewName(userInput.getText().toString());
			}
			else {
				item.setNewName(null);
			}
			dismiss();
		}
		else if(v == cancelButton) {
			cancel();
		}
	}
}
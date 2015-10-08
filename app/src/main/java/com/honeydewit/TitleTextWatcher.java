package com.honeydewit;

import android.text.Editable;
import android.text.TextWatcher;

public class TitleTextWatcher implements TextWatcher {
	private BasicActivity context;
	public TitleTextWatcher(BasicActivity context) {
		this.context = context;
	}
	public void afterTextChanged(Editable s) {}
	public void beforeTextChanged(CharSequence s, int start, int count, int after){


	}
	public void onTextChanged(CharSequence s, int start, int before, int count){
		if(s != null && s.length() > 0) {
			context.setTitle(s.toString());
		}
		else {
			context.setTitle(context.getText(R.string.app_name));
		}
	}

}

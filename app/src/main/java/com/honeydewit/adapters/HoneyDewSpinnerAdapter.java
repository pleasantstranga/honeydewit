package com.honeydewit.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.honeydewit.R;

public class HoneyDewSpinnerAdapter extends ArrayAdapter<CharSequence> {
	Context context;
	
	public HoneyDewSpinnerAdapter(Context context, int resource) {
		super(context, resource);
		this.context = context;
	}
	public View getView(int position, View convertView,
			ViewGroup parent) {
		View v = super.getView(position, convertView, parent);
		v.setBackground(context.getResources().getDrawable(R.drawable.button_background));
		
		return v;
	}

	public View getDropDownView(int position, View convertView,
			ViewGroup parent) {
				return parent;
		// this is for each of the drop down resource that is created. 
		// you can style these things too
	}
}
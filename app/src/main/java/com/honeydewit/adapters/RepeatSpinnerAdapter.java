package com.honeydewit.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.GregorianCalendar;

public class RepeatSpinnerAdapter extends ArrayAdapter<String> {
	
	public RepeatSpinnerAdapter(Context context, int textViewResourceId, GregorianCalendar date) {
		super(context, textViewResourceId);
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		return convertView;
		
	}

}

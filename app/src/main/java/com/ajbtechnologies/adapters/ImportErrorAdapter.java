package com.ajbtechnologies.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ajbtechnologies.R;

import java.util.List;

@SuppressLint("ViewConstructor")
public class ImportErrorAdapter extends ArrayAdapter<String> {

	private RelativeLayout parentLayout;


    public ImportErrorAdapter(Context context, int resourceId, List<String> errors) {
        super(context, resourceId, errors);
    }

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {


        View linearLayout = convertView;
        if (linearLayout == null) {
            linearLayout = LayoutInflater.from(getContext()).inflate(R.layout.import_error_row, parent, false);
        }
        final int errorIndex = getItem(position).indexOf(":");
        final String sheetName = getItem(position).substring(0,errorIndex+1);
        final String error = getItem(position).substring(errorIndex+2);

        TextView sheetNameTextView = (TextView) linearLayout.findViewById(R.id.topRow);
		TextView errorTextView = (TextView) linearLayout.findViewById(R.id.bottomRow);

        sheetNameTextView.setText(sheetName);
        sheetNameTextView.setTypeface(null,Typeface.BOLD);
        errorTextView.setText(error);

        return linearLayout;

	}
}
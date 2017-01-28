package com.ajbtechnologies.calendar;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.ajbtechnologies.R;
 
public class CustomRepeatDialogGridAdapter extends BaseAdapter {
	private Context context;
	private final String[] gridValues;
	private final int SELECTED_DATE_BG = Color.rgb(103, 210, 255);
	private final int CURRENT_DATE_BG = Color.rgb(160, 160, 160);
	private String type;
	
	public CustomRepeatDialogGridAdapter(Context context, String type, String[] gridValues) {
		this.context = context;
		this.gridValues = gridValues;
		this.type = type;
		
	}
 
	public View getView(int cellIndex, View cell, ViewGroup gridView) {
		if (cell == null)
		 {
			 LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			 cell = inflater.inflate(R.layout.reoccurance_cell, gridView, false);
		 }
		 createCell(cellIndex, cell);

		 return cell;
	}
	 private void createCell(int cellIndex, View cell) {
		 Button cellTextView = (Button)cell.findViewById(R.id.reoccuranceCellText);
		 cellTextView.setText(gridValues[cellIndex]);
		 
		 if(type.equals("YEARLY")) {
			 cellTextView.setTag(cellIndex);
		 }
		 else {
			 cellTextView.setTag(gridValues[cellIndex]); 
		 }
		 setSelectedBackgroundColor(cellTextView);
		 cellTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.setSelected(!v.isSelected());
				setSelectedBackgroundColor(v);
			}

			
		});
		 
		 
	 }
	 private void setSelectedBackgroundColor(View v) {
			
			if(!v.isSelected()) {
				v.setBackgroundColor(CURRENT_DATE_BG);
			}
			else {
				v.setBackgroundColor(SELECTED_DATE_BG);
			}
		}
	@Override
	public int getCount() {
		return gridValues.length;
	}
 
	@Override
	public Object getItem(int position) {
		return null;
	}
 
	@Override
	public long getItemId(int position) {
		return 0;
	}
 
}
package com.ajbtechnologies.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.ajbtechnologies.R;
import com.ajbtechnologies.listeners.OneOffClickListener;
import com.ajbtechnologies.pojos.Links;

import java.util.List;

public class LinksButtonGridAdapter extends BaseAdapter {

	private List<Links> links;
	private Context context;


	public LinksButtonGridAdapter(Context context, List<Links> links) {
		this.links = links;
		this.context = context;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.menu_item_button, null);
		}
		final Links link = links.get(position);
		Button button = (Button)v.findViewById(R.id.button);
		button.setText(link.getLinkTxt());
		button.setOnClickListener(new OneOffClickListener() {


			@Override
			public void onClick(View v) {
				super.onClick(v);
				Class<?> intentClass = null;
				try {
					intentClass = Class.forName(link.getIntent());
	
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				if(null != intentClass) {
					Intent intent = new Intent(context, intentClass);
					intent.putExtra(com.ajbtechnologies.Constants.SUB_CAT_CODE, link.getSubCategoryCode());
					context.startActivity(intent);
					if(link.isFinishOnNewInent()) {

						((Activity)context).finish();
					}
				}
				
			} 
		});
		return v;
	}


	@Override
	public int getCount() {
		if(links != null) {
			return links.size();
		}
		return 0;
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return links.get(position);
	}


	@Override
	public long getItemId(int position) {
		return links.get(position).get_id();
	}
}
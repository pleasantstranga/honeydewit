package com.honeydewit.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.honeydewit.R;
import com.honeydewit.listeners.OneOffClickListener;
import com.honeydewit.pojos.Links;

import java.util.List;

public class LinksGridAdapter extends BaseAdapter {

	private List<Links> links;
	private Context context;


	public LinksGridAdapter(Context context, List<Links> links) {
		this.links = links;
		this.context = context;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.menu_item, parent);
		}
		final Links link = links.get(position);
		TextView linkTxt = (TextView)v.findViewById(R.id.itemTxt);
		linkTxt.setText(link.getLinkTxt());
		
		if(null != link.getImageName()) {
			initializeImageButton(v, link);
		}
		return v;
	}
	private void initializeImageButton(View v, final Links link) {
		ImageButton button = (ImageButton)v.findViewById(R.id.buttonImg);
		String sub = link.getImageName().substring(0, link.getImageName().indexOf("."));
		int imageId = context.getResources().getIdentifier(sub, "drawable", context.getPackageName());
		button.setBackgroundResource(imageId);
		button.setImageDrawable(context.getResources().getDrawable(R.drawable.no_background));
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
					intent.putExtra(com.honeydewit.Constants.SUB_CAT_CODE, link.getSubCategoryCode());
					context.startActivity(intent);
					
				}
				
			} 
		});
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return links.size();
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